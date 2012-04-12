package DataModel;

/***
 * 
 * @author latoof
 * Element de donnees abstraites. Avant l'execution de chaque algorithme, on va transformer
 * les donnees concretes (GMOL_Image) en donnees plus generiques.
 * Ce type d'objet peut se résumer comme etant un tableau a n dimensions.
 * 
 * Il permet de mettre facilement des drapeaux (visited, noise, etc) lors de l'execution des algorithmes
 *
 */
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
