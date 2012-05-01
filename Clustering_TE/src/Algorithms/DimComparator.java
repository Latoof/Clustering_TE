package Algorithms;

import java.util.Comparator;
import DataModel.DataElement;

public class DimComparator implements Comparator<DataElement> {

	private int dimension;
	
	public DimComparator( int d ) {
		this.dimension = d;
	}
	
	@Override
	public int compare(DataElement arg0, DataElement arg1) {
		// TODO Auto-generated method stub
		if ( this.dimension > 0 && arg0.getDimension() > this.dimension && arg1.getDimension() >= this.dimension ) {
			return (int) (arg0.getData(this.dimension-1) - arg1.getData(this.dimension-1));
		}
		else {
			System.out.println("Boggus");
			return 0;
		}
	}

}
