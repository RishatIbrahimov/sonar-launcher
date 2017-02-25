package tk.irishat.config;

import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.sonarsource.scanner.api.EmbeddedScanner;
import org.sonarsource.scanner.api.LogOutput;
import org.sonarsource.scanner.api.StdOutLogOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author Rishat Ibrahimov
 */
@Configuration
public class SonarLauncherConfig {

  @Bean(initMethod = "start", destroyMethod = "stop")
  @Autowired
  public EmbeddedScanner embeddedScanner(LogOutput logOutput, ConfigProperties configProperties) {
    Properties properties = new Properties();
    properties.putAll(configProperties.getScanner());
    return EmbeddedScanner.create(logOutput).addGlobalProperties(properties);
  }

  @Bean
  public LogOutput logOutput() {
    return new StdOutLogOutput();
  }

  @Bean
  @Autowired
  public CredentialsProvider credentialsProvider(ConfigProperties config) {
    return new UsernamePasswordCredentialsProvider(config.getAccessToken(), "");
  }

  @Bean
  @ConfigurationProperties
  public ConfigProperties sonarScannerProperties() {
    return new ConfigProperties();
  }
}
