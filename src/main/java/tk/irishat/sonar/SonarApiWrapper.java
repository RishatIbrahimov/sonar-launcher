package tk.irishat.sonar;

import org.sonarsource.scanner.api.EmbeddedScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author Rishat Ibrahimov
 */
@Service
public class SonarApiWrapper {

  @Autowired
  private EmbeddedScanner embeddedScanner;


  @PostConstruct
  public void init() {
    embeddedScanner.runAnalysis(embeddedScanner.globalProperties());
  }
}
