package core;

import java.util.ArrayList;
import java.util.List;

import player.Pool;

/**
 * This class represents a deck, from which cards can be drawn.
 * A deck can be shuffled with a ShuffleCommand.
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
	
	/**
	 * Reconstructs a deck from the pools of teams.
	 */
	public Deck(Pool pool1, Pool pool2) {
		cards = new ArrayList<Card>();
		for ( Card card : pool1 ) {
			cards.add(card);
		}
		for ( Card card : pool2 ) {
			cards.add(card);
		}
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
	
	private void setCards(List<Card> cards) {
		this.cards = cards;
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
	
	public void execute(ShuffleCommand command) {
		command.shuffle(cards);
	}
	
	public void execute(List<ShuffleCommand> commands ) {
		for ( ShuffleCommand command : commands) {
			execute(command);
		}
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
		List<Card> top	= getCards().subList(getSize()-amount, getSize());
		List<Card> bottom = getCards().subList(0,getSize()-amount);
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
		List<Card> top = getCards().subList(getSize() - topAmount, getSize());
		List<Card> middle = getCards().subList(getSize() - topAmount - middleAmount, getSize() - topAmount);
		top.addAll(middle);
		top.addAll(getCards().subList(0, getSize() - topAmount - middleAmount));
		setCards(top);
	}

}
