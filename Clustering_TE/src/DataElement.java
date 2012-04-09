
/* Element de donnees abstraites. Permet de mettre facilement des drapeaux.
 * (visited, noise, etc)
 *  */
public class DataElement {

	private double[] data;
	private int dimension;
	
	public DataElement( double[] d ) {
		this.data = d;
		this.dimension = d.length;
	}
	
	public int getDimension() {
		return this.dimension;
	}
	
}
