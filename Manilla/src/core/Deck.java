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
	
	/**
	 * Constructs a new deck with all 32 Manille cards
	 */
	public Deck() {
		cards = new ArrayList<Card>();
		generateCards();
	}
	
	private void generateCards() {
		for (Suit suit : Suit.values()) {
			for (Symbol symbol : Symbol.values()) {
				cards.add(new Card(suit,symbol));
			}
		}
	}
	
	/****************************
	 * CARDS
	 ****************************/

	List<Card> cards;
	
	public List<Card> getCards() {
		return cards;
	}
	
	private void setCards(ArrayList<Card> top) {
		this.cards = top;
	}
	
	public int getSize() {
		return cards.size();
	}
	
	public boolean isEmpty() {
		return cards.isEmpty();
	}

	public Card draw() {
		return cards.remove(0);
	}
	
	/**********************************************************************
	 * SHUFFLE
	 **********************************************************************/
	
	/**
	 * Shuffles the cards in the deck using randomization
	 * Algorithm: Knuth / Fisher-Yates
	 */
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
	
	/**
	 * Shuffles the cards <b>n</b> times using riffle shuffle
	 */
	public void multiRiffleShuffle(int n) {
		for ( int i = 0 ; i < n ; i++ ) {
			riffleShuffle();
		}
	}
	
	/**
	 * Shuffles the cards in the deck using riffle shuffle
	 * Algorithm: Own interpretation
	 */
	public void riffleShuffle() { 
		ArrayList<Card> upper = getCards((getSize()/2)-1, getSize());
		ArrayList<Card> lower = getCards(0, (getSize()/2)-1);
		ArrayList<Card> deck = new ArrayList<Card>();
		Random random = new Random();
		for (int i=0 ; i < getSize() ; i++) {
			boolean choice = random.nextBoolean();
			if ( ( choice && !upper.isEmpty() ) || lower.isEmpty() ) {
				deck.add(upper.get(0));
				upper.remove(0);
			} else {
				deck.add(lower.get(0));
				lower.remove(0);
			}
		}
		this.cards = deck;
	}
	
	private ArrayList<Card> getCards(int from, int to) {
		ArrayList<Card> part = new ArrayList<Card>();
		for ( int i = from ; i < to ; i++ ) {
			part.add(cards.get(i));
		}
		return part;
	}
	
	/**********************************************************************
	 * CUTS
	 **********************************************************************/
	
	
	/**
	 * Cuts the deck by taking of a given amount of cards from the top of the deck,
	 * and placing them below the remaining cards.
	 * 
	 * @param	amount
	 * 			The amount of cards to taken from the top
	 */
	public void cut(int amount) {
		ArrayList<Card> top	= getCards(getSize()-amount, getSize());
		ArrayList<Card> bottom = getCards(0,getSize()-amount);
		top.addAll(bottom);
		setCards(top);
	}

	/**
	 * Cut the deck using a scarne cut
	 * 
	 * @param 	topAmount
	 * 			The amount of cards to be cut off from the top to the middle portion.
	 * @param 	middleAmount
	 * 			The amount of cards in the middle portion.
	 */
	public void scarneCut(int topAmount, int middleAmount) {
		ArrayList<Card> top = getCards(getSize() - topAmount, getSize());
		ArrayList<Card> middle = getCards(getSize() - topAmount - middleAmount, getSize() - topAmount);
		top.addAll(middle);
		top.addAll(getCards(0, getSize() - topAmount - middleAmount));
		setCards(top);
	}

}
