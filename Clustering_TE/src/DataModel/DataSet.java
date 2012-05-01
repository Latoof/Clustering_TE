package DataModel;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import Algorithms.DistanceCalculator;
import Algorithms.EuclideanDistanceCalculator;
import Algorithms.MeterDistanceCalculator;
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
public class DataSet implements Iterable<DataElement> {

	
	public static int DS_TIME = 1;
	public static int DS_LOC = 2;
	public static int DS_KEYWORDS = 4;

	
	private TreeSet<DataElement> data;
	private int dimension;
	
	/* To make settable */
	// From time
	private final DistanceCalculator euclidean_distance_calculator = new EuclideanDistanceCalculator();
	
	// From location
	private final DistanceCalculator meter_distance_calculator = new MeterDistanceCalculator();

	//private double[][] distance_matrix;
	private TreeMap<DataElement,SortedMap<Double,DataElement>> distance_map;
	
	public DataSet( int d ) {
		
		this.dimension = d;
		this.data = new TreeSet<DataElement>();
				
	}
	
	public DataSet( int d, DistanceCalculator calc ) {
		
		this.dimension = d;
		this.data = new TreeSet<DataElement>();
				
	}
	
	public DataSet( int d, TreeSet<DataElement> set ) {
		this.dimension = d;
		this.data = set;
	}
	
	public void reset_tmp() {
		this.distance_map = null;
		for ( Iterator<DataElement> iterD = this.iterator(); iterD.hasNext(); ) {
			iterD.next().setClusterID( DataElement.UNCLASSIFIED );
		}
	}
	
	public void add( DataElement e ) {
		
		if ( e.getDimension() > this.dimension ) {
			throw new NumberFormatException();
		}
		else {
			data.add( e );
		}
		
	}
	
	/*
	public DataElement get( int id) {
		//return this.data.get( id );
		return new DataElement();
	}
	*/
	
	public Iterator<DataElement> iterator() {
		return this.data.iterator();
	}
	
	public int count() {
		return this.data.size();
	}
	
	public int getDimension() {
		return this.dimension;
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
				tab[offset++] = (double)imgs[i].getLocation().getX();
				tab[offset++] = (double)imgs[i].getLocation().getY();

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
	
	public TreeMap<DataElement,SortedMap<Double,DataElement>> getDistanceMap( int mode ) {
		
		if ( this.distance_map == null ) {
			this.createDistanceMap( mode );
		}
		
		return this.distance_map;
		
	}
	

    /* Pour chaque donne, on calcule les distances par rapport aux autres */
    private void createDistanceMap( int mode ) {
    	
    	this.distance_map = new TreeMap<DataElement, SortedMap<Double,DataElement>>();
    	
        double distance = -1;
        Iterator<DataElement> itFrom = this.iterator();
        
        while ( itFrom.hasNext() ) {
        	
        	DataElement eFrom = itFrom.next();

        	this.distance_map.put( eFrom , new TreeMap<Double, DataElement>() );
        	
        	Iterator<DataElement> itTo = this.iterator();
            while ( itTo.hasNext() ) {

            	DataElement eTo = itTo.next();
        		if ( (mode & DS_TIME) == DS_TIME ) {
                    distance = this.euclidean_distance_calculator.calculateDistance( eFrom, eTo );
        		}
        		if ( (mode & DS_LOC) == DS_LOC ) {
                    distance = this.meter_distance_calculator.calculateDistance( eFrom, eTo );
        		}
                this.distance_map.get( eFrom ).put( distance, eTo );
                //this.distance_map.putAll( new SortedMap<Double,Integer>() );
            }
        }

    }
    
    public String toString() {
    	
    	String str = "";
    	
    	Iterator<DataElement> iterData = data.iterator();
    	while ( iterData.hasNext() ) {
    		str += iterData.next() + "\n";
    	}
    	
    	return str;
    }
    
    public void print_cluster_ids() {
    	
    	Iterator<DataElement> iterData = data.iterator();
    	while ( iterData.hasNext() ) {
    		System.out.println( iterData.next().getCluster().getID() );
    	}    	
    }


	
}
