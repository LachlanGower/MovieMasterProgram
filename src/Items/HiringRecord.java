/*  
 * Class: HiringRecord 
 * Description: This class is basically a receipt class for borrowing and returning movies
 * Author: Lachlan Gower - s3723825  
 */
package Items;

import movieMaster.DateTime;
import movieMaster.IdException;
import movieMaster.ReturnException;

public class HiringRecord {
	private String id;
	private double rentalFee;
	private double lateFee;
	private DateTime borrowDate;
	private DateTime returnDate;
	
	public HiringRecord(String id, double rentalFee, DateTime borrowDate) throws IdException
	{
		
		this.rentalFee = rentalFee;
		lateFee = Double.NaN;
		returnDate = null;
		this.borrowDate = borrowDate;
		this.id = id + borrowDate.getEightDigitDate();
		if(this.id.length() != 18)
		{
			throw new IdException("Error -- There is a problem with the id length");
		}
	}
	
	public double returnItem(int daysBorrowed, double lateFee) throws ReturnException
	{
		/*	BEGIN
		 * 		VALIDATE LATEFEE
		 * 			THROW EXCEPTION
		 * 		UPDATE HIRING RECORD
		 */
		//VALIDATE
		if(lateFee < 0)
		{
			throw new ReturnException("Error -- lateFee is less than 0");
		}
		//UPDATE RECORD
		this.returnDate = new DateTime(borrowDate, daysBorrowed);
		this.lateFee = lateFee;
		return lateFee;
	}

	public void setBorrowDate(int days)
	{
		/* BEGIN
		 * 	SET A NEW BORROW DATE
		 * 	UPDATE ID DETAILS
		 */
		borrowDate = new DateTime(days);
		id = id.substring(0, 9) + borrowDate.getEightDigitDate();
	}
	
	public String getDetails()
	{
		/*BEGIN
		 * 	CHECK IF ON LOAN
		 * 	RETURN STRING
		 */
		String details;
		if(returnDate == null)
		{
			details = String.format("%25s%-25s%s\n%25s%-25s%s\n%25s%.100s\n",
					" ","Hire ID:", id,
					" ", "Borrow Date:", borrowDate.getFormattedDate(),
					" ", "________________________________________________________");
			return details;
		}
		else
		{
			details = String.format("%25s%-25s%s\n%25s%-25s%s\n%25s%-25s%s\n%25s%-25s%s\n%25s%-25s%s\n%25s%-25s%s\n%25s%.100s\n",
					" ","Hire ID:", id,
					" ", "Borrow Date:", borrowDate.getFormattedDate(),
					" ", "Return Date:", returnDate.getFormattedDate(),
					" ", "Fee:", rentalFee,
					" ", "Late Fee:", lateFee,
					" ", "Total Fees:", (rentalFee + lateFee),
					" ", "_________________________________________________");
			return details;
		}
	}
	
	public String toString() 
	{
		/*
		 * CHECK IF ONLOAN
		 * RETURN RELEVANT DETAILS
		 */
		if(returnDate == null)
		{
			return id + ":" + borrowDate.getEightDigitDate() + ":none:"+rentalFee+":none";
		}
		else
		{
		return id + ":" + borrowDate.getEightDigitDate() + ":" + returnDate.getEightDigitDate() + ":" + rentalFee + ":" + lateFee;
		}
	}
}
