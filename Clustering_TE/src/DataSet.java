import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;


public class DataSet implements Iterable<Entry<Integer,DataElement>> {

	public static int DS_TIME = 1;
	public static int DS_LOC = 2;
	public static int DS_KEYWORDS = 4;

	
	private TreeMap<Integer, DataElement> data;
	private int dimension;
	
	public DataSet( int d ) {
		
		this.dimension = d;
		this.data = new TreeMap<Integer,DataElement>();
		
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
	
}
