package tk.irishat.controller;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.sonarsource.scanner.api.EmbeddedScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tk.irishat.config.ConfigProperties;
import tk.irishat.utils.GitUtils;
import tk.irishat.utils.SonarProperties;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

/**
 * @author Rishat Ibrahimov
 */
@RestController
@RequestMapping("/api/sonar/scan")
public class SonarRunnerController {

  @Autowired
  private CredentialsProvider credentialsProvider;

  @Autowired
  private EmbeddedScanner embeddedScanner;

  @Autowired
  private ConfigProperties configProperties;

  @RequestMapping(method = RequestMethod.GET)
  public String scan() throws IOException, GitAPIException {
    File repo = null;
    try {
      String repoUrl = "https://github.com/RishatIbrahimov/cpp-tasks.git";
      String projectKey = getProjectKey(repoUrl);
      repo = GitUtils.clone(repoUrl, credentialsProvider);

      Properties properties = new Properties();
      properties.setProperty(SonarProperties.PROJECT_KEY, projectKey);
      properties.setProperty(SonarProperties.PROJECT_NAME, projectKey);
      properties.setProperty(SonarProperties.PROJECT_VERSION, "1");
      properties.setProperty(SonarProperties.SOURCES, repo.getAbsolutePath());
      properties.setProperty(SonarProperties.PROJECT_BASE_DIR, repo.getAbsolutePath());

      embeddedScanner.runAnalysis(properties);

      return decorateLink(projectKey);
    } finally {
      if (repo != null && !repo.delete()) {
        System.out.println("can not delete temporary directory: " + repo.getAbsolutePath());
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
