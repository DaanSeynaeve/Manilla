package core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Hand implements java.lang.Iterable<Card> {
	
	public Hand() {
		cards = new ArrayList<Card>();
	}
	
	/************************
	 * CARD LIST
	 ************************/
	
	ArrayList<Card> cards;
	
	@Override
	public Iterator<Card> iterator() {
		return cards.iterator();
	}
	
	/************************
	 * MODIFYING THE HAND
	 ************************/
	
	/**
	 * Add a card to the hand
	 * @param card
	 */
	public void addCard(Card card) {
		cards.add(card);
	}
	
	/**
	 * Removes a card from the hand
	 * @param card
	 */
	public void removeCard(Card card) {
		cards.remove(card);
	}
	
	/*************************
	 * OTHER
	 *************************/
	
	public boolean contains(Card card) {
		return cards.contains(card);
	}
	
	public boolean contains(Suit suit) {
		for ( Card card : cards ) {
			if (card.getSuit().equals(suit)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the strongest card of the given suit.
	 * If there are no cards of the given suit, null is returned.
	 * @param suit
	 * @return a Card, or null
	 */
	public Card getStrongestOfSuit(Suit suit) {
		List<Card> suitCards = getCardsOfSuit(suit);
		if ( !suitCards.isEmpty() ) {
			Card strongest = suitCards.get(0);
			for ( Card card : suitCards ) {
				if ( card.isStrongerThan(strongest) ) {
					strongest = card;
				}
			}
			return strongest;
		}
		return null;
	}
	
	private List<Card> getCardsOfSuit(Suit suit) {
		List<Card> suitCards = new ArrayList<>();
		for ( Card card : cards ) {
			if ( card.getSuit().equals(suit) ) {
				suitCards.add(card);
			}
		}
		return suitCards;
	}

}
