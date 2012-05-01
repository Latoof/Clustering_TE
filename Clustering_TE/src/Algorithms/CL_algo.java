package Algorithms;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import DataModel.ClusterElement;
import DataModel.DataElement;
import DataModel.DataSet;

/***
 * 
 * @author latoof
 * 
 * Classe generique pour les algorithmes de clustering. Nous definissons un CL_algo
 * comme un moyen de convertir un DataSet en un ensemble de Clusters contenant les memes
 * donnees, mais de maniere classee.
 * 
 * @param <T> Elements de sorties (on aura des clusters de T) 
 * (La plupart du temps, on aura T == Integer, pour + de clarte)
 * 
 */
public abstract class CL_algo<T> {
	
	protected DataSet data;
	protected TreeSet<ClusterElement> clusters;

	public CL_algo( DataSet d ) {
		this.data = d;
		this.clusters = new TreeSet<ClusterElement>();
	}
	
	public CL_algo( DataSet d, int mode ) {
		this.data = d;
		
		this.clusters = new TreeSet<ClusterElement>( new DimComparator(mode) );
		//this.clusters = new TreeSet<ClusterElement>();


	}
	
	public abstract void runAlgo();
	
	public TreeSet<ClusterElement> getClusters() {
		return this.clusters;
	}
	
	
	public void addElementToCluster( ClusterElement c, DataElement e ) {
		
		c.addElement(e);
		c.computeMeans();
		if ( !this.clusters.contains(c) ) {
			this.clusters.add(c);
		}
		// Lazy
		//this.newCluster(e.getDimension());
		
		//this.clusters.
		
	}
	
	/*
	public void newCluster( int dimension ) {
		
		if ( this.clusters.get(cluster_id) == null ) {
			this.clusters.put( cluster_id, new ClusterElement(dimension) );
		}
		
	}
	*/
	
    public static double EuclidianDistance (double[] vect1, double[] vect2) {
        if(vect1.length != vect2.length){
                throw new NumberFormatException();
        }
        double result = 0;
        double d;
        for (int i = 0; i < vect1.length; i++) {
                d = (vect1[i] - vect2[i]);
                result += ( d * d ); // pow(2)
        }
        return Math.sqrt(result);
    }
	
	
}
