
public class EuclideanDistanceCalculator implements DistanceCalculator {

	@Override
	public double calculateDistance(DataElement e1, DataElement e2) {

	    if( e1.getDimension() != e1.getDimension() ){
	    	throw new NumberFormatException();
	    }
	    
	    double result = 0;
	    double dist;
	    for (int i = 0; i < e1.getDimension(); i++) {
            dist = ( e1.getData(i) - e2.getData(i) );
            result += Math.pow(dist,2);
	    }
	    
	    return (float) Math.sqrt(result);

	}

}
