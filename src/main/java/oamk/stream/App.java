package oamk.stream;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class App {
    public static void main(String[] args) {

        // First task

        /*
         * try {
         * VideoFile video = new
         * VideoFile("Ludwig van Beethoven - Ode an die Freude.mp4");
         * Metadata metadata = video.getVideoFileData();
         * System.out.println("Author: " + metadata.author());
         * System.out.println("Title: " + metadata.name());
         * System.out.println("File type: " + metadata.fileType());
         * } catch (Exception e) {
         * System.err.println("Error: " + e.getMessage());
         * }
         */

        // Second task
        try {
            ReadProductFile readProductFile = new ReadProductFile("products.csv");
            readProductFile.readCSV();

            // output
            for (Product product : readProductFile.getProducts()) {
                System.out.println("Name: " + product.name());
                System.out.println("Price: " + product.price() + " EUR");
                System.out.println("Category: " + product.category());
                System.out.println("Expire date: " + product.bestBefore());
                System.out.println("------------------------");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        // Third task //

        /*
         * Stock stock = new Stock();
         * stock.addProduct(new Product("fish", 14.99, "food", LocalDateTime.of(2024, 9,
         * 1, 0, 0)));
         * stock.addProduct(new Product("shirt", 12.99, "clothes",
         * LocalDateTime.of(2024, 11, 5, 0, 0)));
         * stock.addProduct(new Product("apple", 1.50, "fruit", LocalDateTime.of(2024,
         * 10, 1, 0, 0)));
         * 
         * System.out.println(stock.toString());
         * 
         * stock.setExpireTime(LocalDateTime.now());
         * List<Product> expiredProducts = stock.reportExpired(LocalDateTime.now());
         * 
         * System.out.println("Expired Products:");
         * if (expiredProducts.isEmpty()) {
         * System.out.println("No expired products.");
         * } else {
         * expiredProducts.forEach(product -> System.out
         * .println(product.formatProduct(DateTimeFormatter.ofPattern("yyyy.MM.dd",
         * Locale.US))));
         * }
         */
    }
}
