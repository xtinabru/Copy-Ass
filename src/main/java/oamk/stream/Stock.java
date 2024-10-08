package oamk.stream;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Stock {
  private List<Product> products = new LinkedList<>();
  private LocalDateTime expireTime;
  private DateTimeFormatter stockFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd", Locale.US);

  public List<Product> getProducts() {
    return products;
  }

  public void addProduct(Product p) {
    products.add(p);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("List of Products\n");
    sb.append("----------------\n");

    double totalPrice = 0;

    for (Product product : products) {
      sb.append(product.formatProduct(stockFormat)).append("\n");
      totalPrice += product.price();
    }

    sb.append(String.format("-> Total products: %d\n", products.size()));
    sb.append(String.format("-> Total price: %.2f\n", totalPrice)); //

    return sb.toString();
  }

  public List<Product> reportExpired(LocalDateTime dt) {
    return products.stream()
        .filter(product -> product.bestBefore().isBefore(dt))
        .collect(Collectors.toList());
  }

  public void setExpireTime(LocalDateTime expireTime) {
    this.expireTime = expireTime;
  }
}
