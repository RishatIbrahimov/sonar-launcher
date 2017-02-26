package tk.irishat.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import tk.irishat.config.SonarLauncherConfig;
import tk.irishat.config.SwaggerConfig;

/**
 * @author Rishat Ibrahimov
 */
@SpringBootApplication(scanBasePackages = {"tk.irishat.controller"})
@Import({
        SonarLauncherConfig.class,
        SwaggerConfig.class
})
@EnableConfigurationProperties
public class SonarLauncherApp {
  public static void main(String[] args) {
    new SpringApplication(SonarLauncherApp.class).run(args);
  }
}
