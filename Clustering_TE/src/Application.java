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

		System.out.println("Hello xxx");
		GMOL_Image img = new GMOL_Image(new Date(2012-1900,04,06,15,45));
		//Calendar.set(2012+1900,04,06,15,45) --> BULLSHIT (Date works great)
		
		GMOL_Image img2 = new GMOL_Image(new Date(2012-1900,04,06,15,47), new Point(150,249));
		
		TreeMap<Integer,GMOL_Image> map = new TreeMap<Integer,GMOL_Image>();
		map.put(img.getID(), img);
		map.put(img2.getID(), img2);
		
		System.out.println(map.get(img.getID()));
		System.out.println(map.get(img2.getID()));

		List<TreeSet<GMOL_Image>> set ;//= DBSCAN;
		
		
		DataSet ds = DataSet.ImagesToDataSet( new GMOL_Image[] { img, img2 }, DataSet.DS_TIME );
		System.out.println("Count : "+ds.count());
		
	}

}
