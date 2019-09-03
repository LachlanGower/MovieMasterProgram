/*  
 * Class: Driver  
 * Description: This will start the MovieMaster Class. 
 * To stop loading data comment out the try catch
 * Author: Lachlan Gower - s3723825  
 */
package movieMaster;

public class Driver {

	public static void main(String[] args)
	{
		MovieMaster mm = new MovieMaster();
		try {
			mm.loadData();
		} catch (IdException e) {
			System.out.println(e);
		}
		mm.start();
	}
}
