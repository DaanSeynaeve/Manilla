package AI;

import java.util.List;

import player.InformationHandle;
import core.Card;
import core.Suit;

public interface Intelligence {
	
	public Card chooseCard(List<Card> hand, InformationHandle info);
	
	public Suit chooseTrump(List<Card> hand);
	
	public void notify(List<Card> hand, InformationHandle info);
	
	public String identify();

	public boolean chooseToKnock(List<Card> hand, Suit trump);

}