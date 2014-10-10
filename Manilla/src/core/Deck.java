package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class represents a deck, from which cards can be drawn.
 * A deck can be shuffled.
 * @author Daan
 */
public class Deck {
	
	public Deck() {
		cards = new ArrayList<Card>();
		generateCards();
	}
	
	/****************************
	 * CARDS
	 ****************************/

	List<Card> cards;
	
	private List<Card> getCards() {
		return cards;
	}
	
	public int getSize() {
		return cards.size();
	}
	
	private void generateCards() {
		for (Suit suit : Suit.values()) {
			for (Symbol symbol : Symbol.values()) {
				cards.add(new Card(suit,symbol));
			}
		}
	}
	
	public boolean isEmpty() {
		return cards.isEmpty();
	}

	public Card draw() {
		return cards.remove(0);
	}
	
	/****************************
	 * SHUFFLE
	 ****************************/
	
	public void shuffle() {
		Random random = new Random();
		for (int i=getSize()-1 ; i>0 ; i--) {
			swap(i, random.nextInt(i));
		}
	}
	
	private void swap(int i, int j) {
		Card c = getCards().get(i);
		getCards().set(i, getCards().get(j));
		getCards().set(j, c);
	}

}
