package mp1.daroya;
import java.util.*;

/**
 * Uses the class Solitaire to implement the game of Solitaire.
 *
 * @author  Carlos Adrian Daroya
 * @version 1.0
 * @since   2015-03-27
 */

public class Main
{
	/**
	 * The main method that uses Solitaire.java to execute a game of Solitaire
	 *
	 * @param args not used
 	 */

	public static void main(String args[])
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

		Scanner scan = new Scanner(System.in);
		boolean programRuntime = true;

		do
		{
			int startChoice = 0;
			System.out.println("Welcome to Solitaire! ");
			System.out.println("1 - New Game");
			System.out.println("2 - Load Saved Game");
			System.out.println("3 - Exit");

			System.out.println("\nWhat would you like to do? ");
			do
			{
				System.out.print("Choose a valid number: ");
				while(!scan.hasNextInt())
				{
					System.out.print("Invalid Input! ");
					scan.next();
				}

				startChoice = scan.nextInt();
			}while(startChoice < 1 || startChoice > 3);


			switch(startChoice)
			{

				case 1: // Starts a New Game
						{
						Solitaire solitaire = new Solitaire();
						solitaire.shuffle();
						solitaire.deal();
						solitaire.gameArea();
						break;
						}

				case 2: // Load Saved Game
						{
						Solitaire solitaire = new Solitaire();
						scan.nextLine();
						solitaire.clrscr();
						System.out.println("<< Load Saved Game >> ");
						System.out.print("Input a Filename (i.e: carlos.txt): ");
						String string = scan.nextLine();

						if(solitaire.loadGame(string) == false)
						{
							System.out.println("Error! The File Doesn't Exist.");
							solitaire.pauseMessage();
						}
						else
						{
							System.out.println("Loading successful!");
							solitaire.pauseMessage();
							solitaire.gameArea();
						}
						solitaire.clrscr();
						break;
						}

				case 3:	programRuntime = false;
						break;
			}


		}while(programRuntime != false);

		System.out.println("Bye Bye!");

	}

}

