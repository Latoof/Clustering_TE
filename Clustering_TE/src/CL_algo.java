import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

/* T --> Elements de sorties (on aura des clusters de T) 
 * On va fait au plus simple la plupart du temps et faire T->Integer (ID des images)
 * */
public abstract class CL_algo<T> {

	/*
	private Collection<T> input;
	
	public CL_algo( Collection<T> i ) {
		
		this.input = i;
		this.clusters = new LinkedList<TreeSet<T>>();
		
	}
	*/
	
	private DataSet data;
	private List<TreeSet<T>> clusters;

	public CL_algo( DataSet d ) {
		this.data = d;
	}
	
	public abstract void runAlgo();
	
	List<TreeSet<T>> getClusters() {
		return this.clusters;
	}
	
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
