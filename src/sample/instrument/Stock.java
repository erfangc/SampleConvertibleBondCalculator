package sample.instrument;

public class Stock {

	private double currentPrice;
	private double divYld;
	private double volatility;
	
	public double getCurrentPrice() {
		return currentPrice;
	}
	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}
	public double getDivYld() {
		return divYld;
	}
	public void setDivYld(double divYld) {
		this.divYld = divYld;
	}
	public Stock(double currentPrice, double divYld, double volatility) {
		this.currentPrice = currentPrice;
		this.divYld = divYld;
		this.volatility = volatility;
	}
	
	@Override
	public String toString() {
		return "Stock [currentPrice=" + currentPrice + ", divYld=" + divYld
				+ ", volatility=" + volatility + "]";
	}
	public double getVolatility() {
		return volatility;
	}
	public void setVolatility(double volatility) {
		this.volatility = volatility;
	}
	
}
