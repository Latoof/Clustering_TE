import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class TestParser {

	public static GMOL_Image parseLine( String line ) {
		
		GMOL_Image img = null;
		
		if ( line.length() > 5 ) {
			
			String strParams[] = line.split("#");
			Date d = new Date( Date.parse( strParams[0] ) );
			img = new GMOL_Image(d);
			
			if ( strParams.length > 1 ) {

				String[] strCoords = strParams[1].split(",");
				if ( strCoords.length > 1 ) {
					img.setLocation( 
						new Point( Integer.parseInt( strCoords[0].replace(" ", "") ), Integer.parseInt( strCoords[1].replace(" ", "") ) )
					);
				}
				
			}
	
			if ( strParams.length > 2 ) {
				/* Comming son */
			}
		}
		
		return img;
		
	}
	
	public static GMOL_Image[] GMOL_ImageFromFile( String filename ) {
		
		ArrayList<GMOL_Image> images = new ArrayList<GMOL_Image>();
		
		FileReader fileR = null;
		try {
			fileR = new FileReader(filename);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		BufferedReader br = null;
				
		br = new BufferedReader(fileR);
		
		String line;
		try {
			
			while ( ( line = br.readLine() ) != null ) {
				
				GMOL_Image img = parseLine( line );
				if ( img != null ) {
					images.add(img);
				}
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		if ( images.size() > 0 ) {
			return (GMOL_Image[]) images.toArray( new GMOL_Image[images.size()]);
		}
		else {
			return null;
		}
	}
	
}
