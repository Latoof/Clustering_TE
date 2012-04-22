
import Algorithms.CL_DBSCAN;
import DataModel.DataSet;
import GMOL.GMOL_Image;
import GMOL.TestParser;

public class Application {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		GMOL_Image[] images = TestParser.GMOL_ImageFromFile( "res/test.txt" );
		
		if ( images != null ) {
			
			for (int i=0; i<images.length; i++) {
				System.out.println(images[i]);
			}
			
		}
		
		DataSet dsTime = DataSet.ImagesToDataSet( images, DataSet.DS_TIME );
		System.out.println("Count : "+dsTime.count());
		System.out.println(dsTime);
		
		CL_DBSCAN<Integer> dbs = new CL_DBSCAN<Integer>( dsTime );
		dbs.runAlgo();
				
		System.out.println("Done");
		
		System.out.println(dsTime.get(0).getClusterID());
		System.out.println(dsTime.get(1).getClusterID());
		System.out.println(dsTime.get(2).getClusterID());
		System.out.println(dsTime.get(3).getClusterID());
		System.out.println(dsTime.get(4).getClusterID());

		DataSet dsLoc = DataSet.ImagesToDataSet( images, DataSet.DS_LOC );
		System.out.println("Count : "+dsLoc.count());
		System.out.println(dsLoc);

		CL_DBSCAN<Integer> dbs2 = new CL_DBSCAN<Integer>( dsLoc );
		dbs2.runAlgo();
				
		System.out.println("Done");
		
		System.out.println(dsLoc.get(0).getClusterID());
		System.out.println(dsLoc.get(1).getClusterID());
		System.out.println(dsLoc.get(2).getClusterID());
		System.out.println(dsLoc.get(3).getClusterID());
		System.out.println(dsLoc.get(4).getClusterID());
		
		
		DataSet dsTimeLoc = DataSet.ImagesToDataSet( images, (DataSet.DS_LOC | DataSet.DS_TIME) );
		System.out.println("Count : "+dsTimeLoc.count());
		System.out.println(dsTimeLoc);

		CL_DBSCAN<Integer> dbs3 = new CL_DBSCAN<Integer>( dsTimeLoc );
		dbs3.runAlgo();
				
		System.out.println("Done");
		
		System.out.println(dsTimeLoc.get(0).getClusterID());
		System.out.println(dsTimeLoc.get(1).getClusterID());
		System.out.println(dsTimeLoc.get(2).getClusterID());
		System.out.println(dsTimeLoc.get(3).getClusterID());
		System.out.println(dsTimeLoc.get(4).getClusterID());
	}

}
