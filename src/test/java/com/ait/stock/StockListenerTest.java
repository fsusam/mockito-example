package com.ait.stock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StockListenerTest {
    private Stock stock = null;
    private StockListener stockListener = null;
    private final StockBroker stockBroker = mock(StockBroker.class);

    @BeforeEach
    public void setUp() {
        stockListener = new StockListener(stockBroker);
        stock = new Stock("FDI", 100.0);
    }

    @Test
    public void sellStocksWhenPriceGoesUp() {
        when(stockBroker.getQoute(stock)).thenReturn(150.0);
        stockListener.takeAction(stock);
        verify(stockBroker).sell(stock, 10);
    }

    @Test
    public void sellStocksWhenPriceGoesDown() {
        when(stockBroker.getQoute(stock)).thenReturn(100.0);
        stockListener.takeAction(stock);
        verify(stockBroker).buy(stock, 100);
    }
}
