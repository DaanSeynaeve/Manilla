package player;

import java.util.List;

import core.Card;

/**
 * Represents a pool of cards. Specific elements can't be removed:
 * there is the possibility to add a card or remove all cards. The cards
 * are stored in no particular order. The pool can be used to count the points
 * of the cards in it. The pool only remembers a specified
 * amount of cards (4)
 * 
 * @author Daan Seynaeve
 */
public class Pool {
	
	/**
	 * Initializes a new pool
	 */
	public Pool() {
		reset();
	}

	/**********************************************************************
	 * SCORE
	 **********************************************************************/
	
	private int score;
	
	public int getScore() {
		return score;
	}

	/**********************************************************************
	 * BUFFER
	 **********************************************************************/
	
	private static final int BUFFER_SIZE = 4;
	private Card[] cards = new Card[4];
	
	public Card[] getCards() {
		return this.cards;
	}
	
	/**********************************************************************
	 * PUSH & RESET
	 **********************************************************************/

	private int next = 0;
	
	public void push(Card card) {
		score +=  card.getValue();
		cards[next] = card;
		next = ( next + 1 ) % BUFFER_SIZE;
	}
	
	public void pushAll(List<Card> cards) {
		for ( Card card : cards ) {
			push(card);
		}
	}
	
	public void reset() {
		for ( int i = 0 ; i < BUFFER_SIZE ; i++ ) { cards[i] = null; }
		score = 0;
	}
	
	public boolean isEmpty() {
		for ( Card card : cards ) {
			if (card != null) {
				return false;
			}
		}
		return true;
	}

}
