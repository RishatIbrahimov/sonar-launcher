package tk.irishat.config;

import java.util.Map;

/**
 * @author Rishat Ibrahimov
 */
public class ConfigProperties {
  private Map<String, String> scanner;
  private String accessToken;

  public Map<String, String> getScanner() {
    return scanner;
  }

  public void setScanner(Map<String, String> scanner) {
    this.scanner = scanner;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }
}
