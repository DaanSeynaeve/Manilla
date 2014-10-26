package player;

import java.util.ArrayList;
import java.util.List;

import core.*;

/**
 * This class allows intelligent actors to ask questions to the system.
 * <br/><br/>
 * <b>Part of the AI API:</b> custom AI's can use an object of this class (when provided)
 * to inspect certain aspects of the system.
 * @author Daan
 */
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
		return round.getPreviousTrickClone();
	}
	
	/**
	 * Returns a (sorted) list of the cards on the field.
	 * The list will be empty if no cards were played yet.
	 * @return a List of Cards
	 */
	public List<Card> inspectField() {
		return new ArrayList<Card>(round.getField());
	}
	
	/**
	 * Checks if the given card is valid to play at this time.
	 * Returns false if the card is null.
	 * @param card
	 * @return true if it is
	 */
	public boolean isValidCard(Card card) {
		if ( card != null ) {
			return round.isValidCard(card, player);
		} else {
			return false;
		}
	}
	
	/**
	 * Inspects the current trump.
	 * When the round is no-trump, null is returned.
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
	
	/**
	 * Returns the players position in the current trick's order of play.
	 * This is a number ranging from 0 (first player to play a card) to 3 (last person to play a card).
	 * @return position (int)
	 */
	public int getTurn() {
		return round.getPositionInSequence(player);
	}
	
	/**
	 * Returns the dealers position in the current trick's order of play.
	 * This is a number ranging from 0 (dealer was first to play a card) to 3 (dealer is last to play a card)
	 * @return position (int)
	 */
	public int getDealerTurn() {
		return round.getPositionInSequence(round.getDealer());
	}
	
	/**
	 * Returns the current pool score for the allied team. This is the
	 * sum of the values of the cards in the tricks the allied team has won.
	 * @return integer
	 */
	public int getCurrentAllyPoolScore() {
		return round.getTeam(player).getPoolScore();
	}
	
	/**
	 * Return the current pool score for the enemy team. This is the
	 * sum of the values of the cards in the tricks the enemy team has won.
	 * @return integer
	 */
	public int getCurrentEnemyPoolScore() {
		return round.getOpposingTeam(player).getPoolScore();
	}

}
