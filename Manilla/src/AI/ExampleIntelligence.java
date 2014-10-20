package AI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import player.InformationHandle;
import core.Card;
import core.Suit;

public class ExampleIntelligence implements Intelligence {
	
	private Random rand;
	
	public ExampleIntelligence() {
		this.rand = new Random();
	}

	/**
	 * Will choose a random card in the hand.
	 */
	@Override
	public Card chooseCard(List<Card> hand, InformationHandle info) {
		List<Card> valid = new ArrayList<Card>();
		for ( Card card : hand ) {
			if ( info.isValidCard(card) ) {
				valid.add(card);
			}
		}
		if ( !valid.isEmpty() ) {
			return valid.get(rand.nextInt(valid.size()));
		} else {
			throw new RuntimeException("unexpected");
		}
		
	}

	@Override
	public void notify(List<Card> hand, InformationHandle info) {
		// do nothing
	}

	/**
	 * Choose a random trump
	 */
	@Override
	public Suit chooseTrump(List<Card> hand) {
		return Suit.values()[rand.nextInt(4)];
	}

	/**
	 * Returns the name of the AI.
	 */
	@Override
	public String identify() {
		return "[ex. Manille-AI v1.0]";
	}

	/**
	 * Never knocks.
	 */
	@Override
	public boolean chooseToKnock(List<Card> hand, Suit trump) {
		return false;
	}
	
}
