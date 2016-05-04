package mp1.daroya;
import java.util.*;
import java.io.*;
import java.lang.*;


/**
 * Class containing the whole implementation of Solitaire Card
 *
 * @author  Carlos Adrian Daroya
 * @version 1.0
 * @since   2015-03-27
 */

public class Solitaire
{
	private Card[] deck = new Card[52];
	private LinkedStack talon = new LinkedStack();
	private LinkedStack[] foundations = new LinkedStack[4]; 
	private LinkedStack stock = new LinkedStack();
	private LinkedStack[] tableau = new LinkedStack[7];
	private LinkedStack hand = new LinkedStack();
	private LinkedStack selectStack = null;
	private Card selectCard = null;

	/**
	 * Instantiates a Solitaire object, its variables, and creates the deck of cards
 	 */

	public Solitaire()
	{
		System.out.println("Solitaire!");
		int index = -1;
		for(int i = 1; i<=13; i++)
		{
			for(int j = 0; j<4; j++)
			{
				deck[++index] = new Card(Suit.values()[j], i);	
				deck[index].setFaceUp(false);			
			}
		}

		for(int i = 0; i < 7; i++)
			tableau[i] = new LinkedStack();

		for(int i = 0; i < 4; i++)
			foundations[i] = new LinkedStack();

	}

	/**
	 * Checks if the user has something selected
	 *
	 * @return the boolean true if something is selected
 	 */

	public boolean isSelect()
	{
		if(selectStack != null || selectCard != null)
			return true;	

		return false;
	}

	/**
	 * Contains the game's main interface.
	 * It contains the whole solitaire game.
 	 */

	public void gameArea()
	{	
		Scanner scan = new Scanner(System.in);
		int gameChoice = 1;

		while(gameChoice != 6)
		{
			if(isGameOver() == true)
				return;

			clrscr();
			printGameState();
			printGameMenu();

			System.out.println("What do you like to do? ");
			gameChoice = inputValidation(1,6);

			switch(gameChoice)
			{
				case 1: // Re-deal Cards
						clrscr();
						shuffle();
						deal();
						break;
				case 2: // Draw topmost card from 3s
						Card card = draw();

						if(card == null)
						{
							System.out.println("The stock is now empty. Talon cards are now refilled to stock.");
							pauseMessage();
							scan.next();
						}
						break;
				case 3: // Select A Card
						if(isSelect() == true) deselect();
						caseSelect();
						break;

				case 4: // Move Option
						if(isSelect() == false)
						{
							System.out.println("Select A Card First! ");
							pauseMessage();
						}
						else
						caseMove();
						break;
				case 5: // Save Game
						clrscr();
						deselect();		
						System.out.println("<< Save Game >>");
						System.out.print("Input Filename (i.e: carlos.txt): ");
						String string = scan.nextLine();
								
						if(saveGame(string) == false)
						{
							System.out.println("I'm sorry but you just can't. Try again next time.");
							pauseMessage();
						}
						else
						{
							System.out.println("The game was successfully saved!");
							pauseMessage();
						}
						break;

				case 6: // Quit Game
						clrscr();
						break;
			}
		}

	}

	/**
	 * Clears display in the command line.
 	 */

	public static void clrscr()
	{
		try
		{
			System.out.print("\033[H\033[2J");

		}

		catch (Exception e)
		{
			
			for(int i = 0; i < 1000; i++)
				System.out.println();
		}
		
	}

	/**
	 * Checks if the user has already won.
	 *
	 * @return the boolean true if the ranks of all the tops of the foundations are 13
 	 */

	public boolean isGameOver()
	{

		for(int i = 0; i < 4; i++)
		{
			if(foundations[i].isEmpty() == false)
			{
				Card card = (Card)foundations[i].peek();
				if(card.getRank() != 13)
					return false;
			}
			else
			{
				return false;
			}
		}

		clrscr();
		System.out.println("Congratulations!");
		System.out.println("\nYou won! Here's a Heart of Diamonds for u: <3 of <>");
		pauseMessage();

		return true;
	}

	/**
	 * Arranges the cards as in the start of an actual solitaire game.
	 * It sets the tableau and the stock.
 	 */

	public void deal()
	{
		// Making sure the LinkedStacks are cleared first
		stock.clear();
		talon.clear();
		hand.clear();

		for(int i = 0; i < 7; i++)
			tableau[i].clear();

		for(int i = 0; i < 4; i++)
			foundations[i].clear();

		selectStack = null;
		selectCard = null;

		for(int i = 0; i < 52; i++)
			deck[i].setFaceUp(false);

		// Setting the stock

		for(int i = 0; i < 52; i++)
			stock.push(deck[i]);

		// Setting the tableau

		for(int i = 0; i < 7; i++)
		{
			for(int j = 0; j <= i; j++)
			{
				Card card = (Card)stock.pop();

				if(j == i)
					card.setFaceUp(true);
				
				tableau[i].push(card);
			}
		}
	}

	/**
	 * Randomizes cards in the deck.
 	 */

	public void shuffle()
	{

	    int index;
	  	Card temp;

	    Random rdm = new Random();
	    for(int i = deck.length - 1; i > 0; i--)
	    {
	    	index = rdm.nextInt(i + 1);
	    	temp = deck[index];
	    	deck[index] = deck[i];
	    	deck[i] = temp;
	    }

	}

	/**
	 * Allows the user to draw by 3s from the stock.
	 * It returns the topmost from the drawn cards for display.
	 *
	 * @return returns the topmost card from the drawn 3s
 	 */

	public Card draw()
	{
		hand.clear();

		if(stock.isEmpty() == true)
		{
			while(talon.isEmpty() != true)
			{
				Card card = (Card)talon.pop();
				card.setFaceUp(false);
				stock.push(card);
			}
			talon.clear();
			return null;

		}

		for(int i = 0; i < 3; i++)
		{	
			// If stock is not yet empty
			if(stock.isEmpty() == false)
			{	
				Card card = (Card)stock.pop();
				card.setFaceUp(true);
				talon.push(card);
				hand.push(card);
			}
			// Stock empties during the loop
			else
			{
				autoMoveFoundation();
				if(talon.isEmpty() == true)
					return null;
				else
					return (Card)talon.peek();
			}
		}
		
		autoMoveFoundation();
		if(talon.isEmpty() == true)
			return null;
		else
			return (Card)talon.peek();

	}

	/**
	 * Moves cards automatically from drawn 3s into the foundation if valid.
 	 */

	public void autoMoveFoundation()
	{
		if(talon.isEmpty() == true)
			return;

		int x = 0;
		int y = 0;

		while(talon.isEmpty() == false && x == 0)
		{
			if(moveToFoundation((Card)talon.peek()) == true)
			{	
				x = 0;
				y = 1;
				System.out.println((Card)talon.peek() + " became available , so it was automatically moved to the foundations");	
				hand.pop();
				talon.pop();
			}
			else
				x = 1;
		}

		if(y == 1)
			pauseMessage();
	}

	/**
	 * Displays the current state of the game.
	 * It displays the foundations, tableau piles, drawn cards, and the card/s currently selected.
 	 */

	public void printGameState()
	{
		System.out.println("<< FOUNDATIONS >> \n");
		for(int i = 0; i < 4; i++)
		{
			System.out.println("Foundation #" + (i + 1) + ": ");
			if(foundations[i].isEmpty() == true)
				System.out.println("Empty\n");
			else
				System.out.println((Card)foundations[i].peek() + "\n");
		}

		System.out.println("<< TABLEAU >> ");
		for(int i = 0; i < 7; i++)
		{
			System.out.println("Pile #" + (i + 1) + ": ");
			if(tableau[i].isEmpty() == true)
				System.out.println("Empty\n");
			else
				System.out.println(tableau[i]);
		}

		System.out.println("<< DRAWN CARDS >>");// if(hand is not empty, continue playing.) else, start using the talons
		if(talon.isEmpty() == true )
			System.out.println("Empty\n");
		else if(hand.isEmpty() == false)
			System.out.println(hand);
		else
			System.out.println((Card)talon.peek());

		if(!isSelect())
			System.out.println("\nSELECTED CARD/S: None");
		else
		{
			System.out.print("\nSELECTED CARD/S: ");
			if(selectCard != null)
				System.out.println(selectCard);
			else
				System.out.println("\n" + selectStack);
		}

	}

	/**
	 * Displays the Menu for possible moves during the game.
 	 */

	public void printGameMenu()
	{
		System.out.println("\n<< Game Menu >>\n");
		System.out.println("1 - Re-deal cards");
		System.out.println("2 - Draw 3s from deck");
		System.out.println("3 - Select card/s");
		System.out.println("4 - Move card/s");
		System.out.println("5 - Save Game");
		System.out.println("6 - Quit Game");
	}

	/**
	 * Displays the sub-menu for the option 'Select card/s'.
 	 */

	public void caseSelect()
	{
		Scanner scan = new Scanner(System.in);
		System.out.println("\n_______________________________________________________________\nSelect from ...\n");
		System.out.println("1 - Tableau");
		System.out.println("2 - Foundation");
		System.out.println("3 - Drawn 3s");
		System.out.println("4 - Back");

		int gameChoice = 1;
		System.out.println("What do you like to do? ");
		gameChoice = inputValidation(1,4);

		switch(gameChoice)
		{
			case 1: // Select From Tableau
					System.out.println("From which Pile Number do you wish to select from? ");
					int pile = inputValidation(1,7);

					System.out.println("How many cards do you wish to select? ");
					int numCard = inputValidation(-10000, 10000);

					selectStack = getFromTableau(pile, numCard);
					break;

			case 2: // Select From Foundation
					caseSuit();
					break;
			case 3: // Select From Drawn 3s
					selectCard = getFromDrawn();
					break;
			case 4: // Go Back
					break;
		}
	}

	/**
	 * Displays the sub-menu for choosing the suit to select from.
 	 */

	public void caseSuit()
	{
		Scanner scan = new Scanner(System.in);
		System.out.println("From which Suit would you like to Select?");
		for(int i = 0; i < 4; i++)
		{
			System.out.println((i + 1) + "- " + Suit.values()[i]);
		}

		int gameChoice = 1;
		gameChoice = inputValidation(1,4);

		Suit suit = Suit.values()[gameChoice-1];
		selectCard = getFromFoundation(suit);
	}

	/**
	 * Displays the sub-menu for the option 'Move card/s'.
 	 */

	public void caseMove()
	{
		Scanner scan = new Scanner(System.in);
		System.out.println("\n_______________________________________________________________\nMove to ...\n");
		System.out.println("1 - Tableau");
		System.out.println("2 - Foundation");
		System.out.println("3 - Back");

		int flag = 1;
		int gameChoice = 1;
		boolean isValid = true;

		
		System.out.println("Where would you like to move the card? ");
		gameChoice = inputValidation(1,3);

		switch(gameChoice)
		{
			case 1: // Move To Tableau
					System.out.println("From which pile would like to place the card/s? ");
					int pile = inputValidation(1,7);

					if(selectStack != null)
					{
							Card move = (Card)selectStack.peek();
							isValid = moveToTableau(move, pile);
							if(isValid == false)
							{
								System.out.println("Invalid Move!");
								deselect();
								pauseMessage();
							}
							else
							{
								selectStack.pop();
								while(selectStack.isEmpty() != true)
								{
									moveToTableau((Card)selectStack.pop(), pile);
								}

								upRemaining(selectStack.source);
								autoMoveFoundation();
								selectStack = null;
							}
					}

					else
					{
						Card move = selectCard;
						isValid = moveToTableau(move, pile);
						if(isValid == false)
						{
							System.out.println("Invalid Move!");
							deselect();
							selectCard = null;
							pauseMessage();
						}
						else
						{
							moveToTableau(selectCard, pile);
							autoMoveFoundation();
							selectCard = null;
						}				
					}

					break;
			case 2: // Move To Foundation
					if(selectStack != null)
					{
						// Checks selectStack if greater than 1
						Card dummy = (Card)selectStack.pop();
						if(selectStack.isEmpty() == false)
						{
							System.out.println("You can only move one card to a foundation!");
							selectStack.push(dummy);
							deselect();
							pauseMessage();
						}
						else
						{	
							selectStack.push(dummy);
							Card move = (Card)selectStack.peek();
							isValid = moveToFoundation(move);
							if(isValid == false)
							{
								System.out.println("Invalid Move! Card can't be moved to foundation");
								deselect();
								pauseMessage();
							}
							else
							{
								autoMoveFoundation();
								upRemaining(selectStack.source);
								selectStack = null;	
							}
						}
					}

					else
					{
						isValid = moveToFoundation(selectCard);
						if(isValid == false)
						{
							System.out.println("Invalid Move! Card can't be moved to foundation");
							deselect();
							pauseMessage();
						}
						else
						{
							autoMoveFoundation();
							selectCard = null;
						}
					}

					break;
			case 3: // Go Back
					break;
		}

	}

	/**
	 * Asks for a user input to hold the program from clearing the screen.
 	 */
	public void pauseMessage()
	{
		Scanner scan = new Scanner(System.in);

		System.out.print("\nEnter any character to move on: ");
		scan.nextLine();
	}

	/**
	 * Selects an arbitrary number of cards (depends on user) from a pile in the tableau.
	 *
	 * @param pile the integer that corresponds to the pile number trying to be accessed
	 * @param numberOfCards the integer that correspons to the number of cards trying to be selected from the pile
	 * @return the stack of cards to be selected
 	 */

	public LinkedStack getFromTableau(int pile, int numberOfCards)
	{
		pile = pile - 1;
		int ctr = 0;

		if(tableau[pile].isEmpty() == true)
		{
			System.out.println("The pile you're trying to select from is Empty.");
			pauseMessage();
			return null;
		}

		LinkedStack selected = new LinkedStack();
		for(LinkedStack n = tableau[pile]; ctr < numberOfCards; ctr++)
		{
			if(n.isEmpty() == true)
			{
				System.out.println("Invalid Input. The number of cards you inputted is greater than what's available.");

				while(selected.isEmpty() != true)
				{
					tableau[pile].push(selected.pop());
				}

				pauseMessage();
				return null;
			}

			Card card = (Card)n.peek();
			if(card.getFace() == false)
			{
				System.out.println("Invalid Input. The number of cards you inputted is greater than what's available.");

				while(selected.isEmpty() != true)
				{
					tableau[pile].push(selected.pop());
				}
				pauseMessage();
				return null;
			}
			selected.push(n.pop());
			
		}

		selected.source = pile;
		return selected;
	}

	/**
	 * Selects the topmost card from the talon.
	 *
	 * @return the topmost card from the talon if the talon is not empty
 	 */
	public Card getFromDrawn()
	{
		if(talon.isEmpty() == true)
		{
			System.out.println("Invalid move. The talon is Empty.");
			return null;
		}

		Card card = (Card)talon.pop();
		if(hand.isEmpty() == false)
			hand.pop();

		card.source = 4;
		return card;
	}

	/**
	 * Input Validation for integer inputs.
	 *
	 * @param min the minimum integer input that is valid
	 * @param max the maximum integer input that is valid
	 * @return the valid integer input
 	 */

	public int inputValidation(int min, int max)
	{
		Scanner scan = new Scanner(System.in);
		int choice;
		do
		{
			System.out.print("\nPlease choose a valid number: ");
			while(!scan.hasNextInt())
			{
				System.out.print("Invalid Input! ");
				scan.next();
			}
			choice = scan.nextInt();
		}while(choice < min || choice > max);

		return choice;
	}

	/**
	 * Selects a card from the foundation based on the user-chosen suit.
	 *
	 * @param suit the suit of the card to be selected from the foundation
	 * @return the card from the chosen foundation
 	 */

	public Card getFromFoundation(Suit suit)
	{

		for(int i = 0; i < 4; i++)
		{
			if(foundations[i].isEmpty() == true)
				continue;

			Card card = (Card)foundations[i].peek();
			if(card.getSuit() == suit)
			{
				card = (Card)foundations[i].pop();
				card.source = i;

				return card;
			}
		}

		System.out.println("Invalid Input. There is no such suit in the foundations.");
		return null;

	}

	/**
	 * Allows user to move the selected card into the foundations.
	 *
	 * @param card the selected card
	 * @return the boolean true if the move is valid
 	 */	

	public boolean moveToFoundation(Card card)
	{
		for(int i = 0; i < 4; i++)
		{
			if(foundations[i].isEmpty() == true)
			{
				if(card.getRank() == 1)
				{
					foundations[i].push(card);
					return true;
				}
			}
			else
			{
				Card check = (Card)foundations[i].peek();
				if(check.getSuit() == card.getSuit())
				{
					if(check.getRank() + 1 == card.getRank())
					{
						foundations[i].push(card);
						return true;
					}
				}
				
			}
		}

		return false;
	}

	/**
	 * Allows user to move the selected card/s to a specific pile inside the tableau
	 *
	 * @param card the card to be moved to a pile
	 * @param pile corresponds to the pile where the cards will be moved 
	 * @return the boolean true if the move is valid
	 */

	public boolean moveToTableau(Card card, int pile)
	{
		pile = pile - 1;

		if(tableau[pile].isEmpty() == true)
		{
			if(card.getRank() == 13)
			{
				tableau[pile].push(card);
				return true;
			}

			else
				return false;
		}

		Card check = (Card)tableau[pile].peek();

		if(check.getFace() == false)
			return false;

		Suit a = check.getSuit();
		Suit b = card.getSuit();
		int x = a.ordinal();
		int y = b.ordinal();

		if(x < 2)
		{
			if(y < 2)
				return false;
		}

		if(x > 1)
		{
			if(y > 1)
				return false;
		}

		if(check.getRank() - 1 != card.getRank())
			return false;

		tableau[pile].push((Card)card);

		return true;
	}

	/**
	 * Sets to up the face of the topmost remaining card of the pile selected from after being moved.
	 *
	 * @param pile the integer that corresponds to the pile from where cards were taken	 
	 */

	public void upRemaining(int pile)
	{
		if(tableau[pile].isEmpty() == true)
			return;

		Card card = (Card)tableau[pile].pop();
		if(card.getFace() == false)
			card.setFaceUp(true);
		
		tableau[pile].push(card);
	}

	/**
	 * Deselects cards currently selected.
	 * It sends back currently selected cards to their original location.
	 */

	public void deselect()
	{
		if(selectStack == null & selectCard == null)
			return;

		if(selectStack != null)
		{
			int pile = selectStack.source;

			while(selectStack.isEmpty() != true)
			{
				Card c = (Card)selectStack.pop();
				tableau[pile].push(c);
			}

			selectStack = null;
		}

		else
		{
			int i = selectCard.source;
			if(i == 4)
			{
				talon.push(selectCard);
				hand.push(selectCard);
			}
			else
			{

				foundations[i].push(selectCard);
			}
			selectCard = null;
		}

	}

	/**
	 * Saves/Writes current game state to a text file 
	 *
	 * @param filename the string that is file name chosen by the user
	 * @return the boolean true if the process sucessfully executed 
	 */

	public boolean saveGame(String filename)
	{
		Scanner scan = new Scanner(System.in);
		
		boolean bool = false;

		try
		{
			File file = new File(filename);
			bool = file.createNewFile();
			if(bool == false)
			{
				System.out.println("Filename already exists, would you like to overwrite?");
				System.out.println("1 - Yes , 2 - No");

				int choice;
				do
				{
					System.out.print("Choose a valid number: ");
					while(!scan.hasNextInt())
					{
						System.out.print("Invalid Input! ");
						scan.next();
					}
					choice = scan.nextInt();
				}while(choice < 1 || choice > 2);

				if(choice == 2)
					return false;

				System.out.println("Overwriting. . . ");
			}

			FileWriter fw = new FileWriter(file, false);
			BufferedWriter w = new BufferedWriter(fw);
			// pile 0 1 2 3 4 5 6 
			// foundation 7 8 9 10
			// hand 11
			// talon 12
			// stock 13
			// suit 0 1 2 3
			// rank 1 2 3 4 5 6 7 8 9 10 11 12 13
			// face 0 1

			LinkedStack[] tempReverse = new LinkedStack[14];
			for(int i = 0; i < 14; i++)
			{
				tempReverse[i] = new LinkedStack();

				if(i >= 0 && i <= 6)
					tempReverse[i] = tableau[i].tempReverse(i);

				if(i >= 7 && i <= 10)
					tempReverse[i] = foundations[i-7].tempReverse(i);
		
				if(i == 11)
					tempReverse[i] = hand.tempReverse(i);
				if(i == 12)
					tempReverse[i] = talon.tempReverse(i);
				if(i == 13)
					tempReverse[i] = stock.tempReverse(i);
			}
			// source;suit;rank;face;

			for(int i = 0; i < 14; i++)
			{

				if(tempReverse[i].isEmpty() != true)
				{
					while(tempReverse[i].isEmpty() == false)
					{
						Card card = (Card)tempReverse[i].pop();
						w.write(i + ";" + card.getSuit().ordinal() + ";" + card.getRank() + ";" + card.getFace() + ";");
						w.newLine();
					}
				}			
			}

			w.close();
			return true;
		}

		catch (Exception e)
		{
			System.out.println("Error!");
			return false;
		}
	}

	/**
	 * Loads/Reads game state saved in a text file.
	 *
	 * @param filename the string that is file name chosen by the user
	 * @return the boolean true if the process sucessfully executed
	 */

	public boolean loadGame(String filename)
	{
		Scanner scan = new Scanner(System.in);
		File file = new File(filename);
		BufferedReader r = null;

		try
		{
			if(!file.isFile())
				return false;

			r = new BufferedReader(new FileReader(file));
			String text = null;
			// source;suit;rank;face;
			while((text = r.readLine()) != null)
			{
				String[] parsed = text.split(";");
				int source = Integer.parseInt(parsed[0]);
				int suit = Integer.parseInt(parsed[1]);
				int rank = Integer.parseInt(parsed[2]);
				boolean face = Boolean.parseBoolean(parsed[3]);

				Card card = new Card(Suit.values()[suit], rank);
				card.source = source;
				card.setFaceUp(face);
	
				if(source >= 0 && source <=6)
					tableau[source].push(card);

				if(source >= 7 && source <= 10)
					foundations[source-7].push(card);

				if(source == 11)
					hand.push(card);

				if(source == 12)
					talon.push(card);

				if(source == 13)
					stock.push(card);
			}

			r.close();
			return true;

		}
		catch (Exception e)
		{
			System.out.println("Error!");
			return false;
		}
	}
}