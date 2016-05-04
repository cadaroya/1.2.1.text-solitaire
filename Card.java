package mp1.daroya;
import java.util.*;

/**
 * Blueprint for card objects used by class Solitaire
 *
 * @author  Carlos Adrian Daroya
 * @version 1.0
 * @since   2015-03-27
 */

public class Card extends Object
{
	private Suit suit;
	private int rank;
	private boolean faceUp; 
	public int source; 

	/**
	 * Instantiates card objects given a Suit and Rank
	 *
	 * @param suit corresponds to the suit of the card
	 * @param rank corresponds to the rank of the card
 	 */

	public Card(Suit suit, int rank)
	{
		this.suit = suit;
		this.rank = rank;
	}

	/**
	 * Returns the suit of the card
	 *
	 * @return the suit of the card
 	 */

	public Suit getSuit()
	{
		return this.suit;
	}

	/**
	 * Returns the rank of the card
	 *
	 * @return the rank of the card
 	 */

	public int getRank()
	{
		return this.rank;
	}

	/**
	 * Returns the face of the card
	 *
	 * @return the face of the card
 	 */

	public boolean getFace()
	{
		return faceUp;
	}

	/**
	 * Changes the faceUp attribute of the card depending on the parameter
	 *
	 * @param up corresponds to the face of the card
 	 */

	public void setFaceUp(boolean up)
	{
			this.faceUp = up;
	}

	/**
	 * Modifies how a card object is displayed 
	 *
	 * @return the string containing card data
 	 */

	public String toString()
	{
		String str = "";
		str = str + rank + " of " + suit;

		if(this.faceUp == false)
			return "Unknown Card";
		else
			return str;
	}

}
