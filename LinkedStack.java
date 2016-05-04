package mp1.daroya;
import java.util.*;

/**
 * Stack implementation used by the class Solitaire
 *
 * @author  Carlos Adrian Daroya
 * @version 1.0
 * @since   2015-03-27
 */

public class LinkedStack
{
	private Node top = null;
	public int source;

	/**
	 * Adds a new node to the top of the stack
	 *
	 * @param x the object containing data for new node
 	 */

	public void push(Object x)
	{
		
		Node temp = new Node();
		temp.data = x;
		
		if(top != null)
		{
			temp.link = top;
			top = temp;
		}

		else
		{
			top = temp;
		}
	}

	/**
	 * Deletes the top of stack and r
	 *
	 * @return the object data of the deleted top
 	 */

	public Object pop()
	{
		Node temp = top;

		if(top != null)
		{
			top = top.link;
			Object x = temp.data;

			return x;
		}

		else
		{
			return null;
		}

	}

	/**
	 * Returns the object data from the top of the stack
	 *
	 * @return the object data from the top
 	 */

	public Object peek()
	{
		if(top.data == null)
			return null;

		return top.data;

	}
	/**
	 * Checks if the stack is Empty
	 *
	 * @return the boolean true if the top is null
 	 */

	public boolean isEmpty()
	{
		if(top == null)
		{
			return true;
		}
			return false;
		
	}

	/**
	 * Sets all stack attributes to null
 	 */

	public void clear()
	{
		if(top == null)
			return;

		top.link = null;
		top = null;
	}
	/**
	 * Reverses the order of nodes in the stack
	 *
	 * @param source corresponds to the original location of cards
	 * @return the stack that is reversed
 	 */

	public LinkedStack tempReverse(int source)
	{
		LinkedStack reverse = new LinkedStack();
		if(isEmpty() == true)
			return reverse;

		for(Node n = top; n != null; n = n.link)
		{
			Card card = (Card)n.data;
			card.source = source;
			reverse.push(card);
		}

		Card lol = (Card)reverse.peek();
		return reverse;
	}	

	/**
	 * Modifies how a stack object is displayed 
	 *
	 * @return a string containing all data from the stack
 	 */


	public String toString()
	{
		String str = "";
		LinkedStack reverse = new LinkedStack();

		for(Node n = top; n != null; n = n.link)
		{
			reverse.push((Card)n.data);
		}

		for(Node n = reverse.top; n != null; n = n.link)
		{
			Card card = (Card)n.data;


			str = str + card + "\n";
		}

		return str;
	}

}