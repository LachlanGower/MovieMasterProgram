/*  
 * Class: Game  
 * Description: Subclass of Item. deals with game specific logic (such as extended loan and platforms).
 * Author: Lachlan Gower - s3723825  
 */
package Items;

import movieMaster.IdException;
import movieMaster.ReturnException;

public class Game extends Item
{
	String platforms[];
	boolean extended;
	
	public Game(String id, String title, String genre, String description, String[] platforms) throws IdException
	{
		super(("G_"+id), title, genre, description, 20);
		this.platforms = platforms;
	}
	
	public double returnItem(int daysBorrowed) throws ReturnException {
		/*
		 * CHECK BORROW STATUS
		 * CHECK VALID RETURN DATE
		 * CALCULATE LATE FEE
		 * UPDATE HIRING RECORD
		 */
		double fee = 0;
		int dueDate = 22;
		//Check borrow Status & validate return date
		if(currentlyBorrowed == null || daysBorrowed < 0)
		{
			throw new ReturnException("This Item isn't currently on Loan");
		}
		//Calculate late fee
		else if(daysBorrowed > dueDate)
		{
			fee += daysBorrowed - dueDate;
			fee += 5 * ((daysBorrowed - 22) / 7);
			fee = (extended ? ((double)fee/2) : ((double)fee));
		}
		else if(daysBorrowed <= dueDate)
		{
			fee = 0;
		}
		//Update hiring record
		fee = currentlyBorrowed.returnItem(daysBorrowed, fee);
		currentlyBorrowed = null;
		return fee;
	}
	//get details and to string return class specific details to the super class Item
	public String getDetails(String temp)
	{
		String details = String.format("%-25s%s\n%-25s%s\n%-25s%s\n%-25s%s\n",
				"Standard Rental fee:", "$20.00",
				"Platforms:", getPlatformsAsString(),
				"Rental Period:", "22 days",
				"On loan:", ((currentlyBorrowed == null) ? "NO": (extended) ? "EXTENDED" : "YES"));
		return super.getDetails(details);
	}

	public String toString() {
		return super.toString((getPlatformsAsString() + ":" + "20.0:" + ((currentlyBorrowed == null) ? "N": (extended) ? "E" : "Y")));
	}
	
	private String getPlatformsAsString() {
		String platformString = "";
		for(int x = 0; x < platforms.length; x++)
		{
			if(x == platforms.length -1)
			{
				platformString += platforms[x];
			}
			else
			{
				platformString += platforms[x] + ", ";
			}
		}
		return platformString;
	}

	//A String statement that requires private variable details in Item when borrowing
	public String getBorrowStatement(int days)
	{
		return super.getBorrowStatement(days+22);
	}
	
	public void setExtended(boolean b) {
		extended = b;
	}
}
