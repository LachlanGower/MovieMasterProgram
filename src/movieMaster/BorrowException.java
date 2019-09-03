/*  
 * Class: BorrowException  
 * Description: Exception class. Returns errors when borrow method throws exceptions
 * Author: Lachlan Gower - s3723825  
 */
package movieMaster;

public class BorrowException extends Exception{
	public BorrowException(String error)
	{
		super(error);
	}
}
