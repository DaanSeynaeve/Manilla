package AI;

import java.util.List;

import player.InformationHandle;
import core.Card;
import core.Suit;

/**
 * Represents an intelligent actor which is used to make choices 
 * for the corresponding player.
 * <br/><br/>
 * <b>Part of the AI API:</b> custom AI's should implement this interface.
 * @author Daan
 */
public interface Intelligence {
	
	/**
	 * Chooses a card to play from the given list of cards.
	 * <br/><br/>
	 * System usage: If an invalid card is returned, the system will throw an exception
	 * and the program will terminate. You can use the info.isValid(Card)
	 * method to check whether the card is valid.
	 * 
	 * @param hand	The list of cards in the hand of the player.
	 * @param info	An object ask questions to the system.
	 * @return 		the chosen card
	 */
	public Card chooseCard(List<Card> hand, InformationHandle info);
	
	/**
	 * Chooses a suit to be trump.
	 * @param hand	The list of cards in the hand of the player.
	 * @return 		the chosen suit
	 */
	public Suit chooseTrump(List<Card> hand);
	
	/**
	 * A stateful Intelligence updates its state to reflect the changes in the game.
	 * Implementing this is optional.
	 * <br/><br/>
	 * System usage: this method is called every time someone plays a card.
	 * (including the player corresponding to this Intelligence)
	 * @param hand	The list of cards in the hand of the player.
	 * @param info	An object ask questions to the system.
	 */
	public void notify(List<Card> hand, InformationHandle info);
	
	/**
	 * Returns an identifier/name for the Intelligence.
	 * @return
	 */
	public String identify();

	/**
	 * Decides whether or not to knock with the given trump and hand.
	 * @param hand	The list of cards in the hand of the player.
	 * @param trump	The trump suit.
	 * @return		True if the Intelligence decides to knock
	 */
	public boolean chooseToKnock(List<Card> hand, Suit trump);

}