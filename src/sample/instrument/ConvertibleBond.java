package sample.instrument;

public class ConvertibleBond {

	private double convertRatio;
	private double notionalAmt;
	private double yearsToMaturity;
	private double coupon;
	private Stock underlyingStock;
	
	public ConvertibleBond(double convertRatio, double notionalAmt, double yrsToMat , double coupon, Stock underlyingStock) {
		this.convertRatio = convertRatio;
		this.notionalAmt = notionalAmt;
		this.coupon = coupon;
		this.yearsToMaturity = yrsToMat;
		this.underlyingStock = underlyingStock;
	}

	public double getConversionPrice() {
		return notionalAmt / convertRatio;
	}
	
	public double getParity() {
		return underlyingStock.getCurrentPrice() * convertRatio;
	}
	
	public double getConvertRatio() {
		return convertRatio;
	}

	public void setConvertRatio(double convertRatio) {
		this.convertRatio = convertRatio;
	}

	public double getNotionalAmt() {
		return notionalAmt;
	}

	public void setNotionalAmt(double notionalAmt) {
		this.notionalAmt = notionalAmt;
	}

	public double getCoupon() {
		return coupon;
	}

	public void setCoupon(double coupon) {
		this.coupon = coupon;
	}

	public Stock getUnderlyingStock() {
		return underlyingStock;
	}

	public void setUnderlyingStock(Stock underlyingStock) {
		this.underlyingStock = underlyingStock;
	}

	@Override
	public String toString() {
		return "ConvertibleBond [convertRatio=" + convertRatio
				+ ", notionalAmt=" + notionalAmt + ", coupon=" + coupon + ", underlyingStock=" + underlyingStock
				+ "]";
	}

	public double getYearsToMaturity() {
		return yearsToMaturity;
	}

	public void setYearsToMaturity(double yearsToMaturity) {
		this.yearsToMaturity = yearsToMaturity;
	}	
	

}
