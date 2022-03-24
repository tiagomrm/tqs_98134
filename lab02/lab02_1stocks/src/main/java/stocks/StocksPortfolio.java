package stocks;

import java.util.ArrayList;
import java.util.List;

public class StocksPortfolio {
    private List<Stock> stocks;
    private IStockmarketService stockmarket;

    StocksPortfolio(IStockmarketService stockmarket) {
        stocks = new ArrayList<>();
        this.stockmarket = stockmarket;
    }

    void addStock(Stock s) {
        stocks.add(s);
    }

    double getTotalValue() {
        return stocks.stream()
                .mapToDouble(stock -> stock.getQuantity() * this.stockmarket.lookUpPrice(stock.getLabel()))
                .sum();
    }
}
