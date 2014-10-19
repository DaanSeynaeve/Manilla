package player;

import core.Card;
import core.Hand;
import core.Suit;

public abstract class Player {
	
	public Player(String name) {
		this.hand = new Hand();
		this.name = name;
	}

	/**********************************************************************
	 * NAME
	 **********************************************************************/
	
	private String name;
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return getName();
	}
	
	/**********************************************************************
	 * HAND
	 **********************************************************************/
	
	Hand hand;
	
	public Hand getHand() {
		return hand;
	}
	
	/**********************************************************************
	 * ACTIONS
	 **********************************************************************/
	
	public abstract Suit chooseTrump();

	public abstract boolean knocks(Suit trump);

	public abstract Card chooseCard(InformationHandle info);
	
	public abstract void notify(InformationHandle info);
	
	public abstract void notifyOfTrick(Card[] trick);
	
	/**
	 * Notifies the player of the scores for this round (above 30)
	 * and the current total score for both teams.
	 * @param ally
	 * @param enemy
	 * @param allyTotal
	 * @param enemyTotal
	 */
	public abstract void notifyOfRoundScore(int ally, int enemy, int allyTotal, int enemyTotal);

	public abstract String identify();

	public abstract void notifyOfNewRound();
	
}
