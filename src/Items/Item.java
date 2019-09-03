/*  
 * Class: Item (abstract)  
 * Description:This class is an Abstract class, use in conjunction with Movie or Game for full functionality
 * deals with most business logic (except for returning item).
 * Author: Lachlan Gower - s3723825  
 */
package Items;

import movieMaster.BorrowException;
import movieMaster.DateTime;
import movieMaster.IdException;
import movieMaster.ReturnException;

public abstract class Item {
	private String id;
	private String title;
	private String genre;
	private String description;
	private double fee;
	protected HiringRecord currentlyBorrowed = null;
	private HiringRecord[] hireHistory = new HiringRecord [10];
	
	public Item(String id, String title, String genre, String description, double fee) throws IdException
	{
		if(id.length() != 5)
		{
			throw new IdException("Error, Id must be 3 character long!");
		}
		this.id = id;
		this.title = title;
		this.genre = genre;
		this.description = description;
		this.fee = fee;
	}

	public String getId()
	{
		//Quality of life method (could use to String)
		return id.substring(2, 5);
	}
	public boolean borrowed()
	{
		//Quality of life method
		return !(currentlyBorrowed == null);
	}
	
	public double borrow(String memberId) throws BorrowException, IdException
	{
		/*
		 * BEGIN
		 * 		ID Validation
		 * 		Can this item be borrowed?
		 * 		YES
		 * 			BORROW AND UPDATE HIRING RECORD/CURRENTLYBORROWED
		 * 		NO
		 * 			RETURN BORROW ERROR
		 */
		if(memberId.length() != 3 )
		{
			throw new IdException("Error -- member Id is not correct length of 3");
		}
		if(!borrowed())
		{
			for(int x = hireHistory.length - 1; x > 0; x--)
			{
				hireHistory[x] = hireHistory[x - 1];
			}
			currentlyBorrowed = new HiringRecord(id + "_"+memberId + "_", fee, new DateTime());
			hireHistory[0] = currentlyBorrowed;
			return fee;
		}
		else //is on loan
		{
			throw new BorrowException("Item "+ title + "is already on loan!");
		}
	}
	public void borrowInDays(int days) {
		//A communication method for setting borrow date
		currentlyBorrowed.setBorrowDate(days);
	}
	
	public String getBorrowStatement(int dueInDays)
	{
		//This statement requires private instance information which would otherwise be unavailable in other classes.
		return "The item "+title+" costs $"+fee+" and is due on: "+ new DateTime(dueInDays).getFormattedDate();
	}
	
	
	public String getDetails(String objectDetails)
	{
		
		String details = String.format("%-25s%s\n%-25s%s\n%-25s%s\n%-25s%.70s...\n",
				"ID:", id,
				"Title:", title,
				"Genre:", genre,
				"Description:", description);

		details += objectDetails;
		details += String.format("%24s %25s\n", " ", "____________________________________________________________");
		for(int x = 0; x < hireHistory.length; x++)
		{
			if(hireHistory[x] != null) 
			{
				details += hireHistory[x].getDetails();
			}
		}
		return details;
	}

	//All children of Item must be able to be returned but there is no logic the Item class can carry out.
	public abstract double returnItem(int daysBorrowed) throws ReturnException;
	
	public String toString(String x)
	{
		//Edits to the assignment toString() 
		//this to String prints the entire Item, including hireHistory in blocks to data so its easier to parse.
		String details;
		details = (id+":"+ title +":"+genre+":"+description+":"+x);
		//PRINT HIRE HISTORY FROM OLDEST TO NEWEST
		for(int i = hireHistory.length -1;i > -1; i--)
		{
			if(hireHistory[i] != null)
			{
				details += "\n" + hireHistory[i].toString();
			}
		}
		return details + "\n";
	}
	
}
