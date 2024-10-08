package oamk.stream;

import java.time.format.DateTimeFormatter;

public interface OutputFormatter {
  String formatProduct(DateTimeFormatter dt);
}
