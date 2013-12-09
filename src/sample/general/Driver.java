package sample.general;

import java.util.Date;

import sample.instrument.ConvertibleBond;
import sample.instrument.Stock;

public class Driver {

	public static void main(String[] args) {
		
		// 58 und px, 0 div, 30% vol
		Stock stock = new Stock(58.0, 0.0,0.3);
		
		@SuppressWarnings("deprecation")
		// conv ratio = 15, matures 12/31/15, cpn = 5
		ConvertibleBond cb = new ConvertibleBond(15,1000, new Date("12/31/2015"), 5.0, stock);
		
		System.out.println(cb);
	}

}
