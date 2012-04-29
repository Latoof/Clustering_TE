package DataModel;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import Algorithms.DistanceCalculator;
import Algorithms.EuclideanDistanceCalculator;
import GMOL.GMOL_Image;

/***
 * 
 * @author latoof
 * Regroupe les DataElement (elements a etudier) du systeme.
 * Inclut une methode statique ImagesToDataSet qui permet de convertir automatiquement
 * un jeu de GMOL_Image en un DataSet (jeu de DataElement), en prenant une ou plusieures 
 * dimensions parmis la Date, le Lieu et les Tags.
 * 
 */
public class DataSet implements Iterable<Entry<Integer,DataElement>> {

	
	
	public static int DS_TIME = 1;
	public static int DS_LOC = 2;
	public static int DS_KEYWORDS = 4;

	
	private TreeMap<Integer, DataElement> data;
	private int dimension;
	
	/* To make settable */
	private DistanceCalculator distance_calculator;
	
	//private double[][] distance_matrix;
	private TreeMap<Integer,SortedMap<Double,Integer>> distance_map;
	
	public DataSet( int d ) {
		
		this.dimension = d;
		this.data = new TreeMap<Integer,DataElement>();
		
		this.distance_calculator = new EuclideanDistanceCalculator();
		
	}
	
	public DataSet( int d, DistanceCalculator calc ) {
		
		this.dimension = d;
		this.data = new TreeMap<Integer,DataElement>();
		
		this.distance_calculator = calc;
		
	}
	
	public DataSet( int d, TreeMap<Integer, DataElement> set ) {
		this.dimension = d;
		this.data = set;
	}
	
	public void reset_tmp() {
		this.distance_map = null;
		for ( Iterator<Entry<Integer, DataElement>> iterD = this.iterator(); iterD.hasNext(); ) {
			iterD.next().getValue().setClusterID( DataElement.UNCLASSIFIED );
		}
	}
	
	public void add( DataElement e ) {
		
		if ( e.getDimension() > this.dimension ) {
			throw new NumberFormatException();
		}
		else {
			data.put( e.getID(), e );
		}
		
	}
	
	public DataElement get( int id) {
		return this.data.get( id );
	}

	public Iterator<Entry<Integer,DataElement>> iterator() {
		return this.data.entrySet().iterator();
	}
	
	public int count() {
		return this.data.size();
	}
	
	
	/* Specifique a notre cas. Pour facilier la conversion Images <-> Donnees Brut */
	/* C'est egalement ici qu'on pourra jouer sur le poids de chaque parametre (pour laisser les algos generiques) */
	public static DataSet ImagesToDataSet( GMOL_Image[] imgs, int mode ) {

		int dim = 0;
		if ( (mode & DS_TIME) == DS_TIME ) {
			dim++;
		}
		if ( (mode & DS_LOC) == DS_LOC ) {
			dim+=2;
		}
		if ( (mode & DS_KEYWORDS) == mode ) {
			dim++;
		}
		
		DataSet ds = new DataSet(dim);
		
		for ( int i=0; i<imgs.length; i++ ) {
			
			int offset = 0;
			
			double tab[] = new double[dim];
			
			if ( (mode & DS_TIME) == DS_TIME ) {
				tab[offset++] = (double) imgs[i].getDate().getTime() / 60000;
			}
			if ( (mode & DS_LOC) == DS_LOC  ) {
				tab[offset++] = (double)imgs[i].getLocation().getX()*1.3;
				tab[offset++] = (double)imgs[i].getLocation().getY()*1.3;

			}
			if ( mode == DS_KEYWORDS ) {
				
			}
			
			ds.add( new DataElement( tab ) );
			
			
		}
		
		return ds;
		
	}
	
	public static DataSet ClustersToDataSet( Collection<ClusterElement> clusters, int mode ) {
		
		int min_dimension = 99;
		
		Iterator<ClusterElement> iterClusters = clusters.iterator();
		while ( iterClusters.hasNext() ) {
			
			ClusterElement c = iterClusters.next();
			if ( c.getDimension() < min_dimension ) {
				min_dimension = c.getDimension();
			}
			
			c.computeMeans();
			
		}
		
		DataSet ds = new DataSet(min_dimension);
		
		iterClusters = clusters.iterator();
		while ( iterClusters.hasNext() ) {
			
			ClusterElement c = iterClusters.next();
			ds.add( c );
			
		}
		
		return ds;
	}
	
    /**
     * For every element in the dataset this method calculates the distance to all
     * the other elements. It is assumed that the used distance measurement is symetric.
     * Therefore only an upper diagonal matrix is created. The distances are
     * then stored in a sorted map so that it is clear which distance belongs 
     * to which element (via the index).
     * This method is useful for finding the n-nearest neighbours of an element
     * of finding all elements that are within a certain radius to the element.
     * 
     * @return
     */
	/*
    private void createSortedDistanceList() {
            double dist;
            if (this.distance_matrix == null) {
                    this.createDistanceMatrix();
            }
            SortedMap<Double, List<Integer>>[] sortedMap = new SortedMap[this.count()];
            for (int i = 0; i < this.count(); i++) {
                sortedMap[i] = new TreeMap<Double,List<Integer>>();
                for (int j = 0; j < this.count(); j++) {
                    if (i != j) {
                        dist = this.getDistance(i, j);
                        List <Integer >l = sortedMap[i].get(dist);
                        if (l == null){
                                sortedMap[i].put(dist, l = new ArrayList<Integer>());
                        }
                        l.add(j);
                    }
                }

            }
            this.neighbourMatrix = sortedMap;

    }
    */
	
	public TreeMap<Integer,SortedMap<Double,Integer>> getDistanceMap() {
		
		if ( this.distance_map == null ) {
			this.createDistanceMap();
		}
		
		return this.distance_map;
		
	}
	

    /* Pour chaque donne, on calcule les distances par rapport aux autres */
    private void createDistanceMap() {
    	
    	this.distance_map = new TreeMap<Integer, SortedMap<Double,Integer>>();
    	
        double distance;
        Iterator<Entry<Integer, DataElement>> itFrom = this.iterator();
        
        while ( itFrom.hasNext() ) {
        	
        	Entry<Integer, DataElement> eFrom = itFrom.next();

        	this.distance_map.put( eFrom.getKey() , new TreeMap<Double, Integer>() );
        	
        	Iterator<Entry<Integer, DataElement>> itTo = this.iterator();
            while ( itTo.hasNext() ) {

            	Entry<Integer, DataElement> eTo = itTo.next();
            	
                distance = this.distance_calculator.calculateDistance( eFrom.getValue(), eTo.getValue() );
                this.distance_map.get( eFrom.getKey() ).put( distance, eTo.getKey() );
                //this.distance_map.putAll( new SortedMap<Double,Integer>() );
            }
        }

    }
    
    public String toString() {
    	
    	String str = "";
    	
    	Iterator<DataElement> iterData = data.values().iterator();
    	while ( iterData.hasNext() ) {
    		str += iterData.next() + "\n";
    	}
    	
    	return str;
    }
    
    public void print_cluster_ids() {
    	
    	Iterator<DataElement> iterData = data.values().iterator();
    	while ( iterData.hasNext() ) {
    		System.out.println( iterData.next().getClusterID() );
    	}    	
    }


	
}
