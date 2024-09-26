package course.concurrency.m2_async.cf.min_price;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class PriceAggregator {

  private static final int MAX_WAIT_TIMEOUT = 2900;
  private final ExecutorService executor = Executors.newFixedThreadPool(100);
  private PriceRetriever priceRetriever = new PriceRetriever();

  public void setPriceRetriever(PriceRetriever priceRetriever) {
    this.priceRetriever = priceRetriever;
  }

  private Collection<Long> shopIds = Set.of(10l, 45l, 66l, 345l, 234l, 333l, 67l, 123l, 768l);

  public void setShops(Collection<Long> shopIds) {
    this.shopIds = shopIds;
  }

  public double getMinPrice(long itemId) {
    List<CompletableFuture<Double>> futureList = shopIds.stream()
        .map(shopId ->
            CompletableFuture
                .supplyAsync(() -> priceRetriever.getPrice(itemId, shopId), executor)
                .orTimeout(MAX_WAIT_TIMEOUT, TimeUnit.MILLISECONDS)
                .handle(handleResult(shopId)))
        .collect(Collectors.toList());
    CompletableFuture.allOf(futureList.toArray(CompletableFuture[]::new)).join();
    return futureList.stream()
        .map(CompletableFuture::join)
        .filter(v -> !Double.isNaN(v))
        .min(Double::compareTo)
        .orElse(Double.NaN);
  }

  private BiFunction<Double, Throwable, Double> handleResult(Long shopId) {
    return (price, e) -> {
      if (e != null) {
        System.out.printf(
            "Error on getting price for shop %s: %s%n", shopId, e.getClass().getSimpleName());
        return Double.NaN;
      }
      System.out.printf("Price for shop %s: %s%n", shopId, price);
      return price;
    };
  }
}
