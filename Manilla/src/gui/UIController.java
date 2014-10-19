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

	public abstract void updateHand(List<Card> hand);

	public abstract void updateField(List<Card> field, int turn);

	public abstract void updateTrump(Suit trump);

	public abstract Object setupCardChoice();

	public abstract Card fetchChosenCard();

	public abstract void informOfInvalidCardChoice();

	/**********************************************************************
	 * ROUND SCORE DIALOG
	 **********************************************************************/

	public abstract void informOfRoundScore(int ally, int enemy, int allyTotal, int enemyTotal);

	/**********************************************************************
	 * NAMES & STRINGS
	 **********************************************************************/

	public abstract void setNames(String playerName, String other1, String other2, String other3);

	public abstract void setFrameTitle(String title);

	public abstract Suit fetchChosenTrump();

}