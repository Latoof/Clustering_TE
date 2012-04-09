import java.awt.Point;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;


public class Application {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		/*
		System.out.println("Hello xxx");
		GMOL_Image img = new GMOL_Image(new Date(2012-1900,04,06,15,45));
		//Calendar.set(2012+1900,04,06,15,45) --> BULLSHIT (Date works great)
		
		GMOL_Image img2 = new GMOL_Image(new Date(2012-1900,04,06,16,10), new Point(150,249));
		GMOL_Image img3 = new GMOL_Image(new Date(2012-1900,04,06,23,20), new Point(150,249));
*/
		
		GMOL_Image[] images = TestParser.GMOL_ImageFromFile( "res/test.txt" );
		
		if ( images != null ) {
			
			for (int i=0; i<images.length; i++) {
				System.out.println(images[i]);
			}
			
		}
		
		DataSet ds = DataSet.ImagesToDataSet( images, DataSet.DS_TIME );
		System.out.println("Count : "+ds.count());
		
		CL_DBSCAN dbs = new CL_DBSCAN( ds );
		dbs.runAlgo();
				
		System.out.println("Done");
		
		System.out.println(ds.get(0).getClusterID());
		System.out.println(ds.get(1).getClusterID());
		System.out.println(ds.get(2).getClusterID());
		System.out.println(ds.get(3).getClusterID());
		System.out.println(ds.get(4).getClusterID());

	}

}
