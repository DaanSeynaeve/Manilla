package player;

import java.util.ArrayList;
import java.util.List;

import core.Card;
import core.Hand;
import core.Suit;
import exception.InvalidCardException;


public class Player {
	
	public Player(String name, Intelligence intelligence) {
		this.hand = new Hand();
		this.name = name;
		this.intelligence = intelligence;
	}
	
	/*****************
	 * HAND
	 *****************/
	
	private String name;
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return getName();
	}

	/*****************
	 * HAND
	 *****************/
	
	Hand hand;
	
	public Hand getHand() {
		return hand;
	}
	
	/*****************
	 * INTELLIGENCE
	 *****************/
	
	private Intelligence intelligence;
	
	public void setIntelligence(Intelligence intelligence) {
		this.intelligence = intelligence;
	}
	
	public Suit chooseTrump() {
		return intelligence.chooseTrump(getHandAsList());
	}
	
	private List<Card> getHandAsList() {
		List<Card> copy = new ArrayList<Card>();
		for ( Card card : hand ) {
			copy.add(card);
		}
		return copy;
	}

	public Card chooseCard(InformationHandle info) throws InvalidCardException {
		Card card = intelligence.chooseCard(getHandAsList(), info);
		if ( !hand.contains(card) ) {
			throw new InvalidCardException(card);
		}
		return card;
	}

	public Intelligence getIntelligence() {
		return intelligence;
	}

	public boolean knocks(Suit trump) {
		return intelligence.chooseToKnock(getHandAsList(),trump);
	}
	
	

}
