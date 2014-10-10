package AI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import player.InformationHandle;
import player.Intelligence;
import core.Card;
import core.Suit;

public class ExampleIntelligence implements Intelligence {

	/**
	 * Will choose the first valid card in hand.
	 * Close enough.
	 */
	@Override
	public Card chooseCard(List<Card> hand, InformationHandle info) {
		for ( Card card : hand ) {
			if ( info.isValidCard(card) ) {
				return card;
			}
		}
		throw new RuntimeException("unexpected");
	}

	@Override
	public void notify(List<Card> hand, InformationHandle info) {
		// do nothing
	}

	/**
	 * Will choose the suit associated with the cards with the highest combined
	 * symbol strength.
	 */
	@Override
	public Suit chooseTrump(List<Card> hand) {
		Map<Suit,Double> amounts = new HashMap<Suit,Double>();
		for ( Suit s : Suit.values()) {
			amounts.put(s, (double) 0);
		}
		for ( Card c : hand ) {
			double x = amounts.get(c.getSuit()) + c.getSymbol().getStrength();
			amounts.put(c.getSuit(), x);
		}
		Suit best = Suit.HEART;
		for ( Suit s : amounts.keySet() ) {
			if ( amounts.get(s) > amounts.get(best)) { best = s; }
		}
		return best;
	}

	/**
	 * IDENTIFY YOURSELF
	 */
	@Override
	public String identify() {
		return "[example manille-AI v1.0: Dalek]";
	}

	/**
	 * I am not the one who knocks
	 */
	@Override
	public boolean chooseToKnock(List<Card> hand, Suit trump) {
		return false;
	}
	
}
