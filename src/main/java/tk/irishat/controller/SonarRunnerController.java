package tk.irishat.controller;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonarsource.scanner.api.EmbeddedScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.irishat.config.ConfigProperties;
import tk.irishat.utils.GitUtils;
import tk.irishat.utils.SonarProperties;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Rishat Ibrahimov
 */
@RestController
@RequestMapping("/api/sonar/scan")
public class SonarRunnerController {

  private static final Logger LOGGER = LoggerFactory.getLogger(SonarRunnerController.class);

  @Autowired
  private CredentialsProvider credentialsProvider;

  @Autowired
  private EmbeddedScanner embeddedScanner;

  @Autowired
  private ConfigProperties configProperties;

  private final ExecutorService executorService = Executors.newSingleThreadExecutor();

  @RequestMapping(method = RequestMethod.POST)
  public String scan(@RequestParam("URL") String repoUrl) throws IOException, GitAPIException {
    String projectKey = getProjectKey(repoUrl);
    executorService.submit(() -> performAnalysis(repoUrl, projectKey));
    return decorateLink(projectKey);
  }

  private void performAnalysis(String repoUrl, String projectKey) {
    File repo = null;
    try {
      LOGGER.info("Cloning " + repoUrl);
      repo = GitUtils.clone(repoUrl, credentialsProvider);
      LOGGER.info("Repository has been cloned.");

      Properties properties = new Properties();
      properties.setProperty(SonarProperties.PROJECT_KEY, projectKey);
      properties.setProperty(SonarProperties.PROJECT_NAME, projectKey);
      properties.setProperty(SonarProperties.PROJECT_VERSION, "1");
      properties.setProperty(SonarProperties.SOURCES, repo.getAbsolutePath());
      properties.setProperty(SonarProperties.PROJECT_BASE_DIR, repo.getAbsolutePath());

      LOGGER.info("Starting analysis...");
      properties.forEach((k, v) -> LOGGER.debug("########## " + k + ": " + v));
      embeddedScanner.runAnalysis(properties);
      LOGGER.info("Analysis has been finished.");
    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      LOGGER.debug("Error while processing " + repoUrl, e);
    } finally {
      if (repo != null && !repo.delete()) {
        LOGGER.warn("Can not delete temporary directory: " + repo.getAbsolutePath());
      }
    }
  }

  private static String getProjectKey(String repoUrl) throws MalformedURLException {
    return new URL(repoUrl).getPath().replaceAll("/", "_");
  }

  private String decorateLink(String projectKey) {
    return configProperties.getScanner().get(SonarProperties.HOST_URL) + "/dashboard/index/" + projectKey;
  }
}
