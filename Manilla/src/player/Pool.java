package player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import core.Card;

/**
 * Represents a pool of cards. These are all the cards a team has won
 * during a round.
 * 
 * @author Daan Seynaeve
 */
public class Pool implements Iterable<Card> {
	
	/**
	 * Initializes a new pool
	 */
	public Pool() {
		reset();
	}
	
	/**********************************************************************
	 * CARDS
	 **********************************************************************/
	
	private List<Card> cardList;
	
	public int getSize() {
		return cardList.size();
	}
	
	public void push(Card card) {
		cardList.add(card);
	}
	
	public void pushAll(List<Card> cards) {
		for ( Card card : cards ) {
			push(card);
		}
	}
	
	public void reset() {
		cardList = new ArrayList<Card>();
	}
	
	public boolean isEmpty() {
		return cardList.isEmpty();
	}
	
	/**********************************************************************
	 * SCORE
	 **********************************************************************/
	
	public int getScore() {
		int score = 0;
		for ( Card card : cardList ) {
			score += card.getValue();
		}
		return score;
	}
	
	/**********************************************************************
	 * ITERATE
	 **********************************************************************/

	@Override
	public Iterator<Card> iterator() {
		return cardList.iterator();
	}

}
