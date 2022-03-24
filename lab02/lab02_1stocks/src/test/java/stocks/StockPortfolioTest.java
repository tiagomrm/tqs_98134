package stocks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.is;


@ExtendWith(MockitoExtension.class)
public class StockPortfolioTest {

    @Mock
    IStockmarketService stockmarket;

    @InjectMocks
    StocksPortfolio stocksPortfolio;

    @Test
    void totalValueTest() {

        when( stockmarket.lookUpPrice("AMAZON")).thenReturn(5.0);
        when( stockmarket.lookUpPrice("OLX")).thenReturn(2.0);

        stocksPortfolio.addStock(new Stock("AMAZON", 3));
        stocksPortfolio.addStock(new Stock("OLX", 5));

        assertThat(stocksPortfolio.getTotalValue(), is(25.0));

    }
}
