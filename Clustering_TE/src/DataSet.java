import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;


public class DataSet implements Iterable<Entry<Integer,DataElement>> {

	
	
	public static int DS_TIME = 1;
	public static int DS_LOC = 2;
	public static int DS_KEYWORDS = 4;

	
	private TreeMap<Integer, DataElement> data;
	private int dimension;
	
	/* To make settable */
	private DistanceCalculator distance_calculator;
	
	private double[][] distance_matrix;
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
	
	public void add( int id, DataElement e ) {
		
		if ( e.getDimension() > this.dimension ) {
			throw new NumberFormatException();
		}
		else {
			data.put( id, e );
		}
		
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
		if ( mode == DS_TIME ) {
			dim++;
		}
		if ( mode == DS_LOC ) {
			dim+=2;
		}
		if ( mode == DS_KEYWORDS ) {
			dim++;
		}
		
		DataSet ds = new DataSet(dim);
		
		for ( int i=0; i<imgs.length; i++ ) {
			
			int offset = 0;
			
			double tab[] = new double[dim];
			
			if ( mode == DS_TIME ) {
				tab[offset++] = (double) imgs[i].getDate().getTime() / 1000;
			}
			if ( mode == DS_LOC ) {
				tab[offset++] = (double)imgs[i].getLocation().getX();
				tab[offset++] = (double)imgs[i].getLocation().getY();

			}
			if ( mode == DS_KEYWORDS ) {
				
			}
			
			ds.add(imgs[i].getID(), new DataElement( tab ) );
			
			
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
	
    private void createSortedDistanceList() {
        double dist;
        if (this.distance_matrix == null) {
                this.createDistanceMap();
        }
    }
    
    /**
     * This method is used to normalize querys on the distance Matirx. That Way only
     * the upper half of the matrix has to be stored since we expect the
     * distance measurement to be symetrical.
     * @param pointId1 index of one of the points that we are interested in in the dataset
     * @param pointId2 index of the other of the points that we are interested in in the dataset
     * @return distance between the two points
     */
    private double getDistance (int pointId1, int pointId2){
            if (pointId1 < pointId2){
                    return this.distance_matrix[pointId1][pointId2];
            } else {
                    return this.distance_matrix[pointId2][pointId1];
            }
    }
    
    /* Pour chaque donne, on calcule les distances par rapport aux autres */
    private void createDistanceMap() {
    	
        double distance;
        this.distance_matrix = new double[this.count()][this.count()];
        Iterator<Entry<Integer, DataElement>> itFrom = this.iterator();
        
        while ( itFrom.hasNext() ) {
        	
        	Iterator<Entry<Integer, DataElement>> itTo = this.iterator();
            while ( itTo.hasNext() ) {

            	Entry<Integer, DataElement> eFrom = itFrom.next();
            	Entry<Integer, DataElement> eTo = itTo.next();
            	
                distance = this.distance_calculator.calculateDistance( eFrom.getValue(), eTo.getValue() );
                this.distance_map.get( eFrom.getKey() ).put( distance, eTo.getKey() );
            }
        }

    }
	
}
