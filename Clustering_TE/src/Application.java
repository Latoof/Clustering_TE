import java.awt.Point;
import java.util.Date;


public class Application {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println("Hello xxx");
		GMOL_Image img = new GMOL_Image(new Date(2012-1900,04,06,15,45));
		//Calendar.set(2012+1900,04,06,15,45) --> BULLSHIT (Date works great)

		System.out.println(img);
		
		GMOL_Image img2 = new GMOL_Image(new Date(2012-1900,04,06,15,45), new Point(150,249));
		
		System.out.println(img2);
	}

}
