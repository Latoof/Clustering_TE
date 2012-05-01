package Algorithms;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.Map.Entry;

import DataModel.ClusterElement;
import DataModel.DataElement;
import DataModel.DataSet;

/***
 * 
 * @author latoof
 * 
 * Algorithme DBSCAN : Effectue le Clustering par densite, en utilisant :
 * @param epsilon Definit la distance minimale entre 2 pts d'un meme cluster
 * @param minNbPoints Parametre de densite minimale --> Nombre de points minimum pour que n points forment un cluster.
 *
 * Avantages : Peu sensible au bruit (on peut mettre "minNbPoints" a 1). Constants dans les partitionnements.
 * Inconvenients : Les parametres epsilon et minNbPoints doivent etres renseignes. Necessite parfois plusieures passes.
 */
public class CL_DBSCAN<T> extends CL_algo<T> {

	private final ClusterElement noise_cluster = new ClusterElement(0);

	private static float default_epsilon = 60; /* Mins */
	private static int default_minNbPoints = 1;
	
	private float epsilon;
	private int minNbPoints;
	
	private int mode;

	public CL_DBSCAN(DataSet d) {
		super(d);
		
		this.epsilon = default_epsilon;
		this.minNbPoints = default_minNbPoints;
		this.mode = DataSet.DS_TIME;

	}
	
	public CL_DBSCAN(DataSet d, int mode ) {
		super(d,mode);
		
		this.epsilon = default_epsilon;
		this.minNbPoints = default_minNbPoints;
		this.mode = mode;
	}
	
	public CL_DBSCAN(DataSet d, float e, int nbPts ) {
		super(d);
		
		this.epsilon = e;
		this.minNbPoints = nbPts;
		this.mode = DataSet.DS_TIME;
	}

	public void runAlgo() {
		
		this.data.reset_tmp();
		
		//int current_cluster = 0;
		Iterator<DataElement> it = this.data.iterator();
		ClusterElement current_cluster = new ClusterElement( this.data.getDimension() );

		while ( it.hasNext() ) {
			
			DataElement e = it.next();

			if ( e.getCluster() == null ) {
				
				expandCluster( e, current_cluster );
				//current_cluster++;
				 current_cluster = new ClusterElement( this.data.getDimension() );
			}
				
		}
				
	}
	
    private boolean expandCluster( DataElement e, ClusterElement currentCluster ) {

        LinkedList<DataElement> seeds = (LinkedList<DataElement>) this.getNeighbours( e );
        if ( seeds.size() < this.minNbPoints ){ //no core point
        	e.setClusterID(DataElement.NOISE);
            return false;
        }
        else {
	        for (DataElement i : seeds) {
	        	i.setCluster(currentCluster);
	        	this.addElementToCluster( currentCluster, i );
	        }
	        seeds.remove( (DataElement) e );
	        while (!seeds.isEmpty()) {
	        	DataElement currentElement = seeds.getFirst();
	            Collection<DataElement> result = this.getNeighbours( currentElement );
	    
	            if (result.size() >= this.minNbPoints){
                    for (DataElement resultPId : result) {
                    	
                        DataElement resultP = resultPId;
                        
                        
                        if (resultP.getCluster() == null ) {
	                        seeds.addLast(resultPId);
	                        resultP.setCluster(currentCluster);
	                        
	                        // T
	        	        	this.addElementToCluster( currentCluster, (resultP) );

                        }
                        
                        if ( resultP.getCluster() != null && resultP.isNoise() ) {
                            resultP.setCluster(currentCluster);
                            
                            // T
            	        	this.addElementToCluster( currentCluster, (resultP) );

                        }
                        
                    }
	            }
	            
	            seeds.remove( (DataElement) currentElement );
	        }
	        return true;
        }
    	
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
    private Collection<DataElement> getNeighbours( DataElement e ) {
    	
		SortedMap<Double,DataElement> neigbourList = this.data.getDistanceMap( this.mode ).get( e );
	    List<DataElement> result = new LinkedList<DataElement>();
	    
	    // Because the api returns strictly smaller we add  a small value. 
	    Collection<DataElement> closeElements = neigbourList.headMap( (double)this.epsilon + 0.0000000000000001f).values();
	    for (DataElement col : closeElements) {
	    	result.add(col);
	    }
	    
	    return result;                                                                                                    
            
    }
    
}
