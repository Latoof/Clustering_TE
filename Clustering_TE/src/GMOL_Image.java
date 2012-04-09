import java.awt.Point;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;


public class GMOL_Image {

	public static int counter;
	
	
	private int id;
	private Point location;
	private Date date;
	
	private TreeSet<String> keywords;
	/* OR 
	 * private SortedSet<Integer> keyword_ids;
	 * --> Should be better ( with a static method containing a Map id<->word )
	 */
	
	public GMOL_Image( Date d ) {
		this.date = d;
		this.location = new Point(-1,-1);
		this.keywords = new TreeSet<String>();
		
		this.id = counter++;
	}
	
	public GMOL_Image( Date d, Point l ) {
		this.date = d;
		this.location = l;
		this.keywords = new TreeSet<String>();
		
		this.id = counter++;

	}
	
	public GMOL_Image( Date d, TreeSet<String> k ) {
		this.date = d;
		this.location = new Point(-1,-1);
		this.keywords = k;
		
		this.id = counter++;
	}
	
	public GMOL_Image( Date d, Point l, TreeSet<String> k ) {
		this.date = d;
		this.location = l;
		this.keywords = k;
		
		this.id = counter++;
	}
	

	public Point getLocation() {
		return this.location;
	}

	public Date getDate() {
		return this.date;
	}
	
	public int getID() {
		return this.id;
	}
	
		
	public void setLocation(Point location) {
		this.location = location;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setKeywords(TreeSet<String> keywords) {
		this.keywords = keywords;
	}

	public TreeSet<String> getKeywords() {
		return this.keywords;
	}
	
	public String toString() {
		return this.id+" :\n"+this.date+" ("+this.date.getTime()+")\n"+this.location+"\n"+this.keywords;
	}
	

	
}