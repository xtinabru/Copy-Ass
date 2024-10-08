package oamk.stream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;
import java.util.List;

public class ReadProductFile {
  private File dataFile;
  private List<Product> products = new LinkedList<>();

  // Конструктор класса
  public ReadProductFile(String fileName) {
    this.dataFile = new File(fileName);
    // Проверка на существование файла
    if (!dataFile.exists()) {
      throw new IllegalArgumentException("File does not exist: " + fileName);
    }
  }

  // Геттер для списка продуктов
  public List<Product> getProducts() {
    return products;
  }

  public void readCSV() {
    try (BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
      String line;
      int lineNumber = 0;

      while ((line = br.readLine()) != null) {
        lineNumber++;
        line = line.trim(); // Удаляем пробелы в начале и конце строки

        // Проверка на пустую строку
        if (line.isEmpty()) {
          System.err.println("Skipped empty line " + lineNumber);
          continue; // Пропустить пустую строку
        }

        String[] fields = line.split(",");

        // Проверка на количество полей (должно быть не менее 4)
        if (fields.length < 4) {
          System.err.println("Error in " + dataFile.getName() + ", skipped line " + lineNumber + ": missing parameter");
          continue; // Пропустить эту строку
        }

        // Преобразование строки в объект Product
        String name = fields[0].trim();
        if (name.isEmpty()) {
          System.err.println("Error in " + dataFile.getName() + ", line " + lineNumber + ": missing name");
          continue; // Пропустить эту строку
        }

        Double price = null;
        if (!fields[1].trim().isEmpty()) {
          try {
            price = Double.parseDouble(fields[1].trim());
          } catch (NumberFormatException e) {
            System.err.println("Error parsing price in line " + lineNumber + ": " + e.getMessage());
            continue; // Пропустить эту строку
          }
        }

        String category = fields[2].trim();
        if (category.isEmpty()) {
          System.err.println("Error in " + dataFile.getName() + ", line " + lineNumber + ": missing category");
          continue; // Пропустить эту строку
        }

        LocalDateTime bestBefore = null;
        if (!fields[3].trim().isEmpty()) {
          try {
            bestBefore = LocalDateTime.parse(fields[3].trim(), DateTimeFormatter.ISO_DATE_TIME);
          } catch (DateTimeParseException e) {
            System.err.println("Error parsing date in line " + lineNumber + ": " + e.getMessage());
            continue; // Пропустить эту строку
          }
        }

        // Добавление продукта в список
        if (price != null && bestBefore != null) {
          products.add(new Product(name, price, category, bestBefore));
        } else {
          System.err.println(
              "Error in " + dataFile.getName() + ", skipped line " + lineNumber + ": missing price or bestBefore date");
        }
      }
    } catch (IOException e) {
      System.err.println("IOException occurred: " + e.getMessage());
      e.printStackTrace(); // Обработка IOException
    }
  }

}
