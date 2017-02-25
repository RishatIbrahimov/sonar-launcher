package tk.irishat.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Rishat Ibrahimov
 */
@SpringBootApplication
public class SonarLauncherApp {
  public static void main(String[] args) {
    new SpringApplication(SonarLauncherApp.class).run(args);
  }
}
