package Algorithms;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.Map.Entry;

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


	private static float default_epsilon = 30; /* Mins */
	private static int default_minNbPoints = 1;
	
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
		
		this.data.reset_tmp();
		
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

        LinkedList<Integer> seeds = (LinkedList<Integer>) this.getNeighbours( idElement );
        if ( seeds.size() < this.minNbPoints ){ //no core point
        	this.data.get(idElement).setClusterID(DataElement.NOISE);
            return false;
        }
        else {
	        for (Integer i : seeds) {
	        	this.data.get(i).setClusterID(currentClusterId);
	        	this.addElementToCluster( currentClusterId, this.data.get(i) );
	        }
	        seeds.remove((Integer)idElement);
	        while (!seeds.isEmpty()) {
	            int currentPoint = seeds.getFirst();
	            Collection<Integer> result = this.getNeighbours( currentPoint );
	    
	            if (result.size() >= this.minNbPoints){
                    for (Integer resultPId : result) {
                    	
                        DataElement resultP = this.data.get(resultPId);
                        
                        if (resultP.getClusterID() == DataElement.UNCLASSIFIED) {
	                        seeds.addLast(resultPId);
	                        resultP.setClusterID(currentClusterId);
	                        
	                        // T
	        	        	this.addElementToCluster( currentClusterId, (resultP) );

                        }
                        
                        if (resultP.getClusterID() == DataElement.NOISE) {
                            resultP.setClusterID(currentClusterId);
                            
                            // T
            	        	this.addElementToCluster( currentClusterId, (resultP) );

                        }
                        
                    }
	            }
	            
	            seeds.remove((Integer)currentPoint);
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
    private Collection<Integer> getNeighbours( int idElement ) {
    	
		SortedMap<Double,Integer> neigbourList = this.data.getDistanceMap().get( idElement );
	    List<Integer> result = new LinkedList<Integer>();
	    
	    // Because the api returns strictly smaller we add  a small value. 
	    Collection<Integer> closepoints = neigbourList.headMap( (double)this.epsilon + 0.0000000000000001f).values();
	    for (Integer col : closepoints) {
	    	result.add(col);
	    }
	    
	    return result;                                                                                                    
            
    }
    
}
