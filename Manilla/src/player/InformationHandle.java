package player;

import java.util.ArrayList;
import java.util.List;

import core.*;

public class InformationHandle {
	
	Round round;
	Player player;
	
	public InformationHandle(Player player, Round round) {
		this.round = round;
		this.player = player;
	}
	
	/**
	 * Returns the previous trick as an array. If the current trick is the first one,
	 * null will be returned.
	 * @return an array of Cards of size 4, or null.
	 */
	public Card[] getPreviousTrick() {
		return round.getPreviousTrick();
	}
	
	/**
	 * Returns a (sorted) list of the cards on the field.
	 * The list will be empty if no cards were played yet.
	 * @return a List of Cards
	 * TODO: check if correct order
	 */
	public List<Card> inspectField() {
		return new ArrayList<Card>(round.getField());
	}
	
	/**
	 * Checks if the given card is valid to play at this time.
	 * @param card
	 * @return true if it is
	 */
	public boolean isValidCard(Card card) {
		return round.isValidCard(card, player);
	}
	
	/**
	 * Inspects the current trump.
	 * @return a Suit
	 */
	public Suit inspectTrump() {
		return round.getTrump();
	}
	
	/**
	 * Returns the multiplier for the current round.
	 * This is the factor by which the score over 30 is multiplied
	 * at the end of the round.
	 */
	public int getMultiplier() {
		return round.getMultiplier();
	}

}
