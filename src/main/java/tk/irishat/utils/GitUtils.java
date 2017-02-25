package tk.irishat.utils;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.util.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author Rishat Ibrahimov
 */
public final class GitUtils {

  private GitUtils() {
    throw new UnsupportedOperationException("Utility class can not be instantiated");
  }

  public static File clone(String repo, CredentialsProvider credentialsProvider) throws IOException, GitAPIException {
    File randomFolder = FileUtils.createTempDir(repo, "_sonar-launcher-repos", null);

    try (Git git = Git.cloneRepository()
            .setURI(repo)
            .setDirectory(randomFolder)
            .setCredentialsProvider(credentialsProvider)
            .call()
    ) {
      git.fetch().setCredentialsProvider(credentialsProvider).call();
      return randomFolder;
    }
  }
}
