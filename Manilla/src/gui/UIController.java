package gui;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import core.Card;
import core.Suit;

public interface UIController {

	public abstract void initialize() throws InvocationTargetException, InterruptedException;

	/**********************************************************************
	 * VISUAL UPDATES
	 **********************************************************************/

	public abstract void updateHand(List<Card> hand, List<Card> valid);

	public abstract void updateField(List<Card> field, int turn);

	public abstract void updateTrump(Suit trump);

	public abstract void informOfInvalidCardChoice();
	
	public abstract void updateTrick(Card[] trick);
	
	public abstract void updateMultiplier(int multiplier);
	
	public abstract void informOfRoundScore(int ally, int enemy, int allyTotal, int enemyTotal);
	
	public abstract void updatePoolScores(int ally, int enemy);
	
	public abstract void updateTotalScores(int ally, int enemy);
	
	/**********************************************************************
	 * CHOICES
	 **********************************************************************/
	
	public abstract Object setupCardChoice();

	public abstract Card fetchChosenCard();
	
	public abstract boolean fetchKnockChoice(Suit trump);
	
	public abstract Suit fetchChosenTrump();

	/**********************************************************************
	 * NAMES & STRINGS
	 **********************************************************************/

	public abstract void setNames(String playerName, String other1, String other2, String other3);
	
	public abstract void setDealerName(String dealerName);

	public abstract void setFrameTitle(String title);
	
	/**********************************************************************
	 * PAUZE
	 **********************************************************************/
	
	public void pauze();


}