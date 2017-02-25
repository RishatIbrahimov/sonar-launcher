package tk.irishat.config;

import java.util.Map;

/**
 * @author Rishat Ibrahimov
 */
public class ConfigProperties {
  private Map<String, String> scanner;

  public Map<String, String> getScanner() {
    return scanner;
  }

  public void setScanner(Map<String, String> scanner) {
    this.scanner = scanner;
  }
}
