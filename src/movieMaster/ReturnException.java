/*  
 * Class: ReturnException  
 * Description: Exception class. Returns errors when Return method throws exceptions
 * Generally if returned before borrowdate or when a fee cant be calculated
 * Author: Lachlan Gower - s3723825  
 */
package movieMaster;

public class ReturnException extends Exception{
	public ReturnException(String error)
	{
		super(error);
	}
	public ReturnException()
	{
		super();
	}
}
