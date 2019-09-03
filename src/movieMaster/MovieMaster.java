/*  
 * Class: MovieMaster  
 * Description: The Menu System, This is the main method for using all other methods.
 * start() method will create menu and start the program. 
 * loadData() will restore the menu to a previous state
 * Other methods used in this class are called only by the Menu   
 * Author: Lachlan Gower - s3723825  
 *
 */
package movieMaster;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;
import Items.*;

public class MovieMaster {
	Scanner scan = new Scanner(System.in);
	Item items[] = new Item [100];

	public void start()
	{
		ShowMenuOptions();
		System.out.println();
		switch(getSelectedMenu())
		{
			case 0:
			try {
				addItem();
			} catch (IdException e) {
				System.out.println(e);
				start();
			}		
				break;
				
				
			case 1:
				try {
					borrowItem();
				}
				catch(BorrowException | IdException e)
				{
					System.out.println(e);
					start();
				} 
				break;
				
				
			case 2:
			try {
				returnItem();
			} catch (ReturnException e) {
				System.out.println(e);
				start();
			}
				break;
				
				
			case 3:
				viewItemList();
				break;
				
				
			case 4:
			try {
				seedData();
			} catch (BorrowException | ReturnException | IdException e) {
				System.out.println(e);
				start();
			}
				break;
				
				
			case 5:
				saveData();
				System.exit(0);
				break;
		}
	}
	
	public void loadData() throws IdException
	{
		/* BEGIN
		 * 		FIND FILE (SET UP SCANNER)
		 * 		LOOP
		 * 			SCAN NEXT LINE
		 * 			BRANCH
		 * 				INITIALISE SPACE FOR NEXT ITEM
		 * 				MAKE MOVIE
		 * 				MAKE GAME
		 * END
		 * 
		 */
		//SET UP SCANER
		Scanner load;
		FileInputStream input = null;
		try {
			input = new FileInputStream("data.txt");
		} catch (FileNotFoundException e) {
			try {
				System.out.println("Data file not found, loading from Backup file");
				input = new FileInputStream("data_backup.txt");
			} catch (FileNotFoundException e1) {
				System.out.println("Backup file not found, reverting to factory settings");
				start();
			}
		}
		
		load = new Scanner(input);
		int itemCounter = 0;
		boolean itemMade = false;
		
		//LOOP while items still in document
		while(load.hasNextLine())
		{
			StringTokenizer item = new StringTokenizer(load.nextLine());
			//FIND NEXT ITEM (BASED ON NO TOKENS)
			if(!item.hasMoreTokens())
			{
				itemCounter++;
				itemMade = false;
			}
			
			else 
			{
				String id = item.nextToken(":");
				//MAKE MOVIE
				if(id.charAt(0) == ('M'))
				{
					//MAKE MOVIE USING TOKENS
					if(!itemMade)
					{	
						items[itemCounter] = new Movie(id.substring(2, 5), item.nextToken(":"), item.nextToken(":"), item.nextToken(":"), (item.nextToken(":").equals("5.0")));
						itemMade = true;
					}
					//MAKE MOVIES HIRING RECORD USING TOKENS
					else
					{
						loadHiringRecord(id, item, itemCounter);
					}
				}
				
				//MAKE GAME
				if(id.charAt(0) == ('G'))
				{
					//MAKE GAME USING TOKENS
					if(!itemMade)
					{	
						items[itemCounter] = new Game(id.substring(2, 5), item.nextToken(":"), item.nextToken(":"), item.nextToken(":"), item.nextToken().split(", "));
						item.nextToken(":");
						//DEALS WITH EXTENDED 
						if(item.nextToken().equals("E"))
						{
							((Game) items[itemCounter]).setExtended(true);
						}
						itemMade = true;
					}
					//CREATE HIRING RECORD FOR INDIVIDUAL GAME
					else
					{
						loadHiringRecord(id, item, itemCounter);
					}
				}
			}
		}
		load.close();
		
	}
	
	private void loadHiringRecord(String id, StringTokenizer item, int itemCounter)
	{
		try {
			/*
			 * TO RESTORE PREVIOUS STATE
			 * BEGIN
			 * 		FIND MEMBER ID TOKEN
			 * 		BORROW WITH MEMBER ID
			 * 
			 * 		FIND BORROW DATE TOKEN
			 * 		FIND DIFFERENCE IN BORROWDATE FROM CURRENT DATE
			 * 		BORROW IN DAYS USING DIFFERENCE
			 * 		
			 * 		FIND RETURN DATE TOKEN
			 * 		CHECK IF THERE WAS A RETURNDATE (IF ITEM WAS RETURNED)
			 * 			CREATE RETURNDATE
			 * 			FIND DIFFERENCE IN DAYS FROM BORROW TO RETURN DATE
			 * 			RETURN ITEM
			 * 
			 * 		
			 */
			items[itemCounter].borrow(id.substring(6,9));
			
			String date = item.nextToken();
			DateTime borrowDate = new DateTime (Integer.parseInt(date.substring(0, 2)), Integer.parseInt(date.substring(2, 4)), Integer.parseInt(date.substring(4, 8)));	
			int daysSinceBorrow = DateTime.diffDays(borrowDate, new DateTime());
			items[itemCounter].borrowInDays(daysSinceBorrow + 1);
			
			date = item.nextToken();
			if(!date.equals("none"))
			{
				DateTime returnDate = new DateTime (Integer.parseInt(date.substring(0, 2)), Integer.parseInt(date.substring(2, 4)), Integer.parseInt(date.substring(4, 8)));	
				int daysOnLoan = DateTime.diffDays(returnDate, borrowDate);
				items[itemCounter].returnItem(daysOnLoan);
			}
		}catch(IdException | BorrowException | ReturnException e){
			System.out.println(e);
		}
	}
	
	private void saveData()
	{
		System.out.println("Saving Data...");
		PrintWriter data = null;
		try 
		{
			FileOutputStream file = new FileOutputStream("data.txt");
			data = new PrintWriter(file);
			for(int x = 0; x < getAmountOfItems();x++)
			{
				data.println(items[x].toString());
				
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Cannot create file.");
		}
		data.close();
		try 
		{
			FileOutputStream file = new FileOutputStream("data_backup.txt");
			data = new PrintWriter(file);
			for(int x = 0; x < getAmountOfItems();x++)
			{
				data.println(items[x].toString());
				
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Cannot create file.");
		}
		data.close();
		System.out.println("Data Saved");
		System.out.println("Exiting Application");
	}

	private void addItem() throws IdException{
		String inputs[] = new String [7];
		String prompts[] = {"Enter ID:			","Enter Title:			","Enter Genre:			","Enter Description:		", "Movie or Game (M/G)? 		","Enter Game Platforms:		","Enter new release (Y/N):	"};
		boolean movie = false;
		boolean newRelease = false;
		for(int i = 0; i < inputs.length; i++)
		{
			
			System.out.print(prompts[i]);
			inputs[i] = scan.nextLine();
			
			//Return to menu
			if(inputs[i].equals(""))
			{
				System.out.println("Returning to Main Menu");
				start();
			}
			//Deal with input
			switch(i)
			{
				case 0:
					//Receive and Validate ID (Check if id length not equal to 3 and if Id is already in system)
					
					inputs[i] = inputs[i].toUpperCase();
					
					
					if(inputs[i].length() != 3)
					{
						System.out.println("ID "+ inputs[i] + " is Invalid. Please Enter a 3 digit ID.");
						i--;
					}
					
					for(int x = 0; x < getAmountOfItems();x++)
					{
						if(inputs[i].equalsIgnoreCase(items[x].getId()))
						{
							System.out.println("Error -- ID "+ inputs[i] + " is already in the system. Please Enter a unqiue ID");
							x = getAmountOfItems();
							i--;
						}
					}
				break;
				case 4:
					//Receive branching information
					if(inputs[i].equalsIgnoreCase("m"))
					{
						movie = true;
						i++;
					}
					else if(inputs[i].equalsIgnoreCase("g"))
					{
						movie = false;
					}
					else 
					{
						System.out.println("Please Enter Either (M)ovie or (G)ame!");
						i--;
					}
				break;
				case 5:
					// game logic for Platforms
					i = 7;
				break;
				case 6:
					// movie logic for New Release
					if(inputs[i].equalsIgnoreCase("y"))
					{
						newRelease = true;
					}
					else if(inputs[i].equalsIgnoreCase("n"))
					{
						newRelease = false;
					}
					else
					{
						System.out.println("Error: You must enter 'Y' or 'N'");
						i--;
					}
					
				break;
				
				
			}	
		}
		if(movie) 
		{
			items[getAmountOfItems()] = new Movie(inputs[0], inputs[1], inputs[2], inputs[3], newRelease);
			System.out.println("New Movie added successfully for Movie ID: M_"+inputs[0]+".");
		}
		else
		{
			items[getAmountOfItems()] = new Game(inputs[0], inputs[1], inputs[2], inputs[3], inputs[5].split(", "));
			System.out.println("New Game added successfully for Game ID: G_"+inputs[0]+".");
		}
		start();
	}
	
	private void borrowItem() throws BorrowException, IdException
	{
		/*	BEGIN
		 * 		GET INPUT(LOOP)
		 * 		VALIDATE INPUT(BRANCH)
		 * 			VALID
		 * 				LOOP again
		 * 			INVALID	
		 * 				DECREMENT LOOP ask question again
		 * 		
		 * 
		 */
		//TODO use switch statement to control flow better
		String prompts[] = {"Enter Id:			","Enter Member Id:		", "Extend Hire Period (Y/N):	", "Advance Borrow Days:		"};
		String inputs[] = new String [4];
		int itemToBorrow = 0;
		int days = 0;
		for(int i = 0; i < inputs.length;i++)
		{
			System.out.print(prompts[i]);
			inputs[i] = scan.nextLine();
			//Return to menu
			if(inputs[i].equals(""))
			{
				System.out.println("Returning to Main Menu");
				start();
			}

			switch(i)
			{
			case 0:
				//VALIDATE ID
				inputs[i] = inputs[i].toUpperCase();
				//CHECK IF ID Matches Ids in system
				if(matchIds(inputs[i]) == -1)
				{
					i--;
				}
				else
				{
					itemToBorrow = matchIds(inputs[i]);
				}
				break;
			case 1:
				//VALIDATE member id
				if(inputs[i].length() != 3)
				{
					System.out.println("Error enter a 3 digit ID");
					i--;
				}
				else 
				{
					inputs[i] = inputs[i].toUpperCase();
					items[itemToBorrow].borrow(inputs[i]);
					//CHECK IF LOOP SHOULD SKIP NEXT CASE (IF IT IS A MOVIE)
					if(!(items[itemToBorrow] instanceof Game))
					{
						i++;
					}
				}
				break;
			case 2:
				//SET EXTENDED BASED ON 
				if(inputs[i].equalsIgnoreCase("y"))
				{
					((Game) items[itemToBorrow]).setExtended(true);
				}
				else if(inputs[i].equalsIgnoreCase("n"))
				{
					((Game) items[itemToBorrow]).setExtended(false);
				}
				//ASK if wants to set extended again!
				else
				{
					System.out.println("Error -- Please use Y/N");
					i--;
				}
				break;
			case 3:
				//ADVANCE BORROW CASE
				days = Integer.parseInt(inputs[i]);
				items[itemToBorrow].borrowInDays(days);	
				break;
			}
			
		}
		//WHEN LOOP ENDS RETURN TO MENU AND PRINT INFORMATION BACK TO USER.
		System.out.println(items[itemToBorrow].getBorrowStatement(days));
		start();
	}

	private void returnItem() throws ReturnException
	{
		/*	BEGIN
		 * 		VALIDATE ID
		 * 		RETURN ITEM
		 * 	END
		 */
		String[] prompts = {"Enter Id: 			", "Enter Number of days on loan:	"};
		String[] inputs = new String[2];
		int itemToReturn = 0;
		for(int i = 0; i< inputs.length;i++)
		{
			System.out.print(prompts[i]);
			inputs[i] = scan.nextLine();
			
			//Return to menu
			if(inputs[i].equals(""))
			{
				System.out.println("Returning to Main Menu");
				start();
			}
			//Logic for inputs
			switch(i)
			{
			case 0:
				//validate ID
				if(matchIds(inputs[i]) == -1)
				{
					i--;
				}
				else
				{
					itemToReturn = matchIds(inputs[i]);
				}
				break;
			case 1:
				//Return Item
				try {
					double price = items[itemToReturn].returnItem(Integer.parseInt(inputs[i]));
					System.out.println("The total fee payable is $"+ price);
				}
				catch(NumberFormatException e){
					System.out.println("Error -- Please Enter a Number");
					i--;
				}
				break;
			}
		}
		//Return to menu
		start();
	}
	
	private int matchIds(String id)
	{
		//THIS METHOD COMPARES ALL IDS WHEN RETURNING OR BORROWING AND RETURNS MATCHES IN THE ARRAY (-1 means no match found)
		for(int x = 0; x < getAmountOfItems();x++)
		{
			if(items[x].getId().equalsIgnoreCase(id))
			{
				return x;
			}
		}
		System.out.println("Error - The item with id number: " + id +", not found");
		return -1;
	}
	
	private void viewItemList()
	{
		//DISPLAY DETAILS
		//LOOP
		//	CALL DISPLAYDETAILS ON each item of ITEMS
		for(int x = 0;x < getAmountOfItems();x++)
		{
			System.out.println(items[x].getDetails(""));
		}
		start();
	}
	
	private int getAmountOfItems() {
		/* BEGIN 
		 *	LOOP
		 *		IF item is NULL
		 * 			END LOOP RETURN INCREMENT
		 * 		ELSE
		 * 			INCREMENT
		 * 
		 */
		int amountOfItems = 0;
		for(int x = 0;x < items.length; x++)
		{
			if(items[x] != null)
			{
				amountOfItems ++;
			}
		}
		return amountOfItems;
	}
	
	private void seedData() throws BorrowException, ReturnException, IdException
	{
		//SEEDS DATA USING ITEM METHODS
		//CHECK IF LIST ALREADY HAS DATA
		if(getAmountOfItems() > 0)
		{
			System.out.println("Error -- Cannot seed Movie List if List already has data in it");
		}
		//ELSE 
		else {
			items[0] = new Movie("MOS", "Man Of Steel", "Superhero", "Cool Superman movie where cool things happen...", false);
			items[1] = new Movie("WOW", "Wonder Woman", "Superhero", "Cool Superhero movie about Wonder Woman...", false);
			items[1].borrow("RIC");
			items[2] = new Movie("RED", "Red Dog", "Drama", "Cool Dog movie about a dog that is red", false);
			items[2].borrow("JAM");
			items[2].returnItem(5);
			items[3] = new Movie("MAX", "Mad Max", "Action", "Cinematic Masterpiece - Forbes the news dudes", false);
			items[3].borrow("JON");
			items[3].returnItem(10);
			items[4] = new Movie("WIK", "John Wick", "Action", "They call him the bogeyman...", false);
			items[4].borrow("JAM");
			items[4].returnItem(10);
			items[4].borrow("KEV");
			items[5] = new Movie("TRD", "Tomb Raider", "Action", "Lara Croft raids a tomb because yes", true);
			items[6] = new Movie("SHC", "Spiderman Homecoming", "Superhero", "Cool Superhero movie about Spiderman...", true);
			items[6].borrow("POP");
			items[7] = new Movie("PTR", "Peter Rabbit", "Mystery", "A rabbit named peter, Interestingly features Scooby Doo", true);
			items[7].borrow("KOV");
			items[7].returnItem(1);
			items[8] = new Movie("RSW", "Red Sparrow", "Action", "Basically what A Black widow movie would be like", true);
			items[8].borrow("PEN");
			items[8].returnItem(3);
			items[9] = new Movie("BPN", "Black Panther", "Superhero", "Wakanda is a place with pretty outrageous coronation cermonies", true);
			items[9].borrow("DEM");
			items[9].returnItem(3);
			items[9].borrow("ILS");
			items[10] = new Game("ACO", "Assassins Creed Origins", "Action/Adventure", "I hope you dont like sehkmet cause you are going to be killing a ton of lions", "Xbox One, PS4, PC".split(", "));
			items[11] = new Game("TWH", "The Witcher 3: The Wild Hunt", "RPG", "If you aren't sporting wolf gear you arent really Geralt", "Xbox One, PS4".split(", "));
			items[11].borrow("POT");
			items[12] = new Game("BSI", "Bioshock Infinite", "Story", "I can see all the code and whats behind the code", "Xbox 360, PS3, PS4, Xbox One".split(", "));
			items[12].borrow("SPT");
			items[12].returnItem(19);
			items[13] = new Game("DST", "Darksiders 2", "Puzzle/Adventure", "Plz help with the level after you kill the archon super confuzzled", "Xbox 360, PS3".split(", "));
			items[13].borrow("HVN");
			items[13].returnItem(32);
		}
		start();
	}

	private int getSelectedMenu() 
	{
		//MENU LOGIC
		//GET INPUT
		//CONVERT INPUT (for switch statement)
		//TRY AGAIN WHEN INCORRECT INPUT
		//
		char input = scan.nextLine().charAt(0);
		input = Character.toLowerCase(input);
		if(input == 'a')
		{
			return 0;
		}
		else if(input == 'b')
		{
			return 1;
		}
		else if(input == 'c')
		{
			return 2;
		}
		else if(input == 'd')
		{
			return 3;
		}
		else if(input == 'e')
		{
			return 4;
		}
		else if(input == 'x')
		{
			return 5;
		}
		else {
			System.out.println("Please Enter A Menu Option");
			return getSelectedMenu();
		}
		
	}
	
	private void ShowMenuOptions()
	{
		//MENU
		System.out.println(""
				+ "*** Movie Master System Menu ***\n"
				+ "Add Item:			A\n"
				+ "Borrow Item: 			B\n"
				+ "Return Item:			C\n"
				+ "Display Details: 		D\n"
				+ "Seed Data:			E\n"
				+ "Exit Program:			X");
	}
}
