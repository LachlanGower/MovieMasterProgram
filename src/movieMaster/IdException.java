/*  
 * Class: IdException  
 * Description: Exception class. Returns errors when There are length or matching exceptions with the ID
 * Author: Lachlan Gower - s3723825  
 */
package movieMaster;

public class IdException extends Exception{

	public IdException(String error) 
	{
		super(error);
	}

}
