package DataModel;

import java.util.LinkedList;

/***
 * 
 * @author latoof
 * Element de donnees abstraites. Avant l'execution de chaque algorithme, on va transformer
 * les donnees concretes (GMOL_Image) en donnees plus generiques.
 * Ce type d'objet peut se résumer comme etant un tableau a n dimensions.
 * 
 * Notre convention pour 3 dimensions : { temps, longitude, latitude }
 * 
 * La classe supportera des drapeaux (visited, noise, etc) lors de l'execution des algorithmes
 *
 */
public class DataElement implements Comparable {
	
	public static int elt_counter;

	protected int id;
	protected double[] data;
	protected int dimension;
	
	/* Assigned during algos */
	protected int clusterID;
	protected ClusterElement cluster;
	
	public static int NOISE = -2;
	public static int UNCLASSIFIED = -1;
	
	public DataElement( double[] d ) {
		this.data = d;
		this.dimension = d.length;
		this.id = elt_counter++;
	}
	
	public int getID() {
		return this.id;
	}
	
	public int getDimension() {
		return this.dimension;
	}
	
	public int getClusterID() {
		return this.clusterID;
	}
	
	public ClusterElement getCluster() {
		return this.cluster;
	}
	
	public void setClusterID( int id ) {
		this.clusterID = id;
	}
	
	public void setCluster( ClusterElement c ) {
		this.cluster = c;
	}
	
	
	public boolean isNoise() {
		return this.cluster.getDimension() == 0;
	}
	
	
	public double getData( int i ) {
		if ( i < this.getDimension() ) {
			return this.data[i];
		}
		else {
			throw new IndexOutOfBoundsException();
		}
	}
	
	public String toString() {
		String str = "[";
		
		for ( int i=0; i<this.dimension; i++ ) {
			str += this.data[i] + ",";
		}
		
		str += "]";
		
		return str;
	}

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return this.id - ((DataElement) arg0).getID();
	}
	
}
