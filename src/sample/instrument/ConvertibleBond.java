package sample.instrument;

import org.jfree.date.SerialDate;
import org.jfree.date.SerialDateUtilities;

public class ConvertibleBond {

	private double convertRatio, notionalAmt, coupon;
	private SerialDate maturity;
	private Stock underlyingStock;
	
	public ConvertibleBond(double convertRatio, double notionalAmt, SerialDate maturity, double coupon, Stock underlyingStock) {
		this.convertRatio = convertRatio;
		this.notionalAmt = notionalAmt;
		this.coupon = coupon;
		this.maturity = maturity;
		this.underlyingStock = underlyingStock;
	}

	public int getDaysToMaturity(SerialDate analysisDate) {
		return SerialDateUtilities.dayCountActual(analysisDate, getMaturity());
	}
	
	public double getYearsToMaturity(SerialDate analysisDate) {
		return ((double) getDaysToMaturity(analysisDate))/365.0;
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

	public SerialDate getMaturity() {
		return maturity;
	}

	public void setMaturity(SerialDate maturity) {
		this.maturity = maturity;
	}

	@Override
	public String toString() {
		return "ConvertibleBond [convertRatio=" + convertRatio
				+ ", notionalAmt=" + notionalAmt + ", coupon=" + coupon
				+ ", maturity=" + maturity + ", underlyingStock="
				+ underlyingStock + "]";
	}

}
