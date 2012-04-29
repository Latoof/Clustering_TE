package DataModel;

import java.util.Iterator;
import java.util.LinkedList;

public class ClusterElement extends DataElement {

	LinkedList<DataElement> sons;
	
	public ClusterElement( int dimension ) {
		super( new double[dimension] );
		sons = new LinkedList<DataElement>();

	}
	
	public void addElement( DataElement e ) {
		this.sons.add(e);
	}
	
	public LinkedList<DataElement> getSons() {
		return this.sons;
	}
	
	public void computeMean() {
		
		for ( int i=0; i<this.dimension; i++ ) {
			
			int indicatives_elements = 0;
			double total = 0;
			Iterator<DataElement> iterSons = sons.iterator();
			
			while ( iterSons.hasNext() ) {
				
				DataElement e = iterSons.next();
				if ( e.getDimension() >= i ) {
					total += e.getData(i);
					indicatives_elements++;
				}

				
			}
			
			if ( indicatives_elements > 0 ) {
				this.data[i] = total / indicatives_elements;
			}
			
		}
		
	}
	
	public String toString() {
		String str = "[Dim:" + this.dimension + "] - Mean : ";
		
		for ( int i=0; i<this.dimension; i++ ) {
			str += this.data[i] + ",";
		}
		str += " ";
		
		str += "Cluster ID : X --> ";
		
		Iterator<DataElement> iterElement = this.sons.iterator();
		while ( iterElement.hasNext() ) {
			str += iterElement.next().toString();
		}
		
		
		return str;
	}
	
	
	
}
