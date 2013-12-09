package sample.general;

import sample.instrument.ConvertibleBond;
import sample.instrument.Stock;
import sample.tree.calculators.TreeAnalyticProcessor;

public class Driver {

	public static void main(String[] args) {
		
		// 100 px, 3% div, 20% vol
		Stock stock = new Stock(100, 0.03, 0.2);
		
		// CvR=0.8, Per 100, 5 Yrs to Mat, 5% Cpn
		ConvertibleBond cb = new ConvertibleBond(0.8, 100, 5 , 5.0, stock);
		
		TreeAnalyticProcessor p = new TreeAnalyticProcessor(cb);		
		
		System.out.println(cb);
		
		System.out.println("Computed Price: "+p.computePrice());
	}

}
