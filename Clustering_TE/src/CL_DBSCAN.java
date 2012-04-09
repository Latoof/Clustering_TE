import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;


public class CL_DBSCAN extends CL_algo {

	private static float default_epsilon;
	private static int default_minNbPoints = 2;
	
	private float epsilon;
	private int minNbPoints;

	public CL_DBSCAN(DataSet d) {
		super(d);
		
		this.epsilon = default_epsilon;
		this.minNbPoints = default_minNbPoints;
	}
	
	public CL_DBSCAN(DataSet d, float e, int nbPts ) {
		super(d);
		
		this.epsilon = e;
		this.minNbPoints = nbPts;
	}

	public void runAlgo() {
		
		int current_cluster = 0;
		Iterator<Entry<Integer, DataElement>> it = this.data.iterator();
		
		while ( it.hasNext() ) {
			
			Entry<Integer, DataElement> e = it.next();
			
			if ( e.getValue().getClusterID() == DataElement.UNCLASSIFIED ) {
				
				expandCluster( e.getKey(), current_cluster );
				current_cluster++;
			}
			
			
		}
		
		
	}
	
    private boolean expandCluster( int idElement, int currentClusterId ) {

    	
    	return true;
    }
    
    /** Inspired from Markus
     * This method queries the dataset for the neighbours of the passed element (id) that
     * satisfie  [dist (id, x) <= epsilon for x element of dataset] .
     * The method returns the ids of the points that fullfill the condition
     * @param dataset
     * @param featureVector
     * @param id
     * @return list of ids that are in the epsilon neighborhood
     */
    private Collection<Integer> getNeighbours( int idElement ) {
    	
		SortedMap<Double,Integer> neigbourList = this.data.getDistanceMap().get( idElement );
	    List<Integer> result = new LinkedList<Integer>();
	    
	    // Because the api returns strictly smaller we add  a small value. 
	    Collection<Integer> closepoints = neigbourList.headMap( (double)this.epsilon +0.0000000000000001f).values();
	    for (Integer col : closepoints) {
	    	result.add(col);
	    }
	    
	    return result;                                                                                                    
            
    }
    
}
