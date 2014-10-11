package AI;

import java.util.ArrayList;
import java.util.List;

import player.InformationHandle;
import player.Player;
import core.Card;
import core.Suit;

public class ArtificialPlayer extends Player {
	
	public ArtificialPlayer(String name, Intelligence intelligence) {
		super(name);
		setIntelligence(intelligence);
	}
	
	/**********************************************************************
	 * INTELLIGENCE
	 **********************************************************************/
	
	private Intelligence intelligence;
	
	private void setIntelligence(Intelligence intelligence) {
		this.intelligence = intelligence;
	}
	
	public Intelligence getIntelligence() {
		return intelligence;
	}
	
	/**********************************************************************
	 * PLAYER
	 **********************************************************************/
	
	@Override
	public Suit chooseTrump() {
		return intelligence.chooseTrump(getHandAsList());
	}

	@Override
	public boolean knocks(Suit trump) {
		return intelligence.chooseToKnock(getHandAsList(),trump);
	}

	@Override
	public Card chooseCard(InformationHandle info) {
		Card card = intelligence.chooseCard(getHandAsList(), info);
		if ( !getHand().contains(card) ) {
			throw new RuntimeException("Fatal error: AI invented a card... scumbag\nCannot recover");
		}
		return card;
	}

	@Override
	public void notify(InformationHandle info) {
		intelligence.notify(getHandAsList(), info);
	}
	
	private List<Card> getHandAsList() {
		List<Card> copy = new ArrayList<Card>();
		for ( Card card : getHand() ) {
			copy.add(card);
		}
		return copy;
	}
	
}
