package com.ait.stock;

public interface StockBroker {
	void buy(Stock stock, int quantity);
	void sell(Stock stock, int quantity);
	double getQoute(Stock stock);
}
