// VideoFile.java
package oamk.stream;

import java.util.regex.Pattern;

public class VideoFile implements Playable {
  private Metadata videoFileData;
  private String fileName;

  public VideoFile(String videoFileName) {
    this.fileName = videoFileName;
    parseFileName(videoFileName);
  }

  private void parseFileName(String videoFileName) {
    String regex = "^(.*) - (.*)\\.(\\w+)$";
    var matcher = Pattern.compile(regex).matcher(videoFileName);

    if (matcher.matches()) {
      String author = matcher.group(1).trim();
      String name = matcher.group(2).trim();
      String fileType = matcher.group(3).trim();
      this.videoFileData = new Metadata(author, name, fileType);
    } else {
      throw new IllegalArgumentException("Incorrect");
    }
  }

  public Metadata getVideoFileData() {
    return this.videoFileData;
  }

  @Override
  public void play() {

    System.out.println("Play video: " + fileName);
  }
}
