/*  
 * Class: Movie  
 * Description: Subclass of Item. Deals with movie specific logic like new release fees
 * Author: Lachlan Gower - s3723825  
 */
package Items;

import movieMaster.IdException;
import movieMaster.ReturnException;

public class Movie extends Item{
	
	private boolean isNewRelease;
	private final double NEW_RELEASE_LATECHARGE = 2.5;
	private final double WEEKLY_LATECHARGE = 1.5;
	
	public Movie(String id, String title, String genre, String description, boolean isNewRelease) throws IdException
	{
		super(("M_"+id), title, genre, description, ((isNewRelease) ? 5 : 3));
		this.isNewRelease = isNewRelease;
	}
	
	public double returnItem(int daysBorrowed) throws ReturnException
	{
		/*
		 * CHECK BORROW STATUS
		 * CHECK VALID RETURN DATE
		 * CALCULATE LATE FEE
		 * UPDATE HIRING RECORD
		 */
		double fee = (isNewRelease ? NEW_RELEASE_LATECHARGE : WEEKLY_LATECHARGE);
		int dueDate = (isNewRelease ? 3 : 7);
		if(currentlyBorrowed == null || daysBorrowed < 0)
		{
			throw new ReturnException("Item currently not on loan");
		}
		else if(daysBorrowed > dueDate)
		{
			fee *= daysBorrowed - dueDate;
		}
		else if(daysBorrowed <= dueDate)
		{
			fee = 0;
		}
		
		fee = currentlyBorrowed.returnItem(daysBorrowed, fee);
		currentlyBorrowed = null;
		return fee;
	}
	//get details and to string return class specific details to the super class Item
	public String getDetails(String temp)
	{
		String details = String.format("%-25s%s\n%-25s%s\n%-25s%s\n%-25s%s\n",
				"Standard Rental fee:", ((isNewRelease) ? "$5.00" : "$3.00"),
				"Movie Type:", ((isNewRelease) ? "New Release" : "Weekly"),
				"Rental Period:", ((isNewRelease) ? "3 Days" : "7 Days"),
				"On loan:", ((currentlyBorrowed == null) ? "NO" : "YES"));
		return super.getDetails(details);
	}
	public String toString() {
		return super.toString(((isNewRelease) ? "5.0":"3.0") + ":" + ((isNewRelease) ? "NR" : "WK") + ":" + ((currentlyBorrowed == null) ? "N" : "Y"));
	}
	//A String statement that requires private variable details in Item when borrowing

	public String getBorrowStatement(int days)
	{
		return super.getBorrowStatement((((isNewRelease)? 3:7) + days));
	}
}
