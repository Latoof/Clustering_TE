
/* Element de donnees abstraites. Permet de mettre facilement des drapeaux.
 * (visited, noise, etc)
 *  */
public class DataElement {

	private double[] data;
	private int dimension;
	
	/* Assigned during algos */
	private int clusterID;
	
	public static int NOISE = -2;
	public static int UNCLASSIFIED = -1;
	
	public DataElement( double[] d ) {
		this.data = d;
		this.dimension = d.length;
	}
	
	public int getDimension() {
		return this.dimension;
	}
	
	public int getClusterID() {
		return this.clusterID;
	}
	
	public void setClusterID( int id ) {
		this.clusterID = id;
	}
	
	
	
	public double getData( int i ) {
		if ( i < this.getDimension() ) {
			return this.data[i];
		}
		else {
			throw new IndexOutOfBoundsException();
		}
	}
	
}
