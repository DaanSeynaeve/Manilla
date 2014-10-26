package gui;

import java.util.ArrayList;
import java.util.List;

import player.InformationHandle;
import player.Player;
import core.Card;
import core.Logger;
import core.ShuffleCommand;
import core.Suit;

public class GraphicalPlayer extends Player {

	public GraphicalPlayer(String name, UIController uic) {
		super(name);
		this.uic = uic;
	}
	
	/**********************************************************************
	 * LOCK
	 **********************************************************************/
	
	private UIController uic;
	
	public UIController getUIController() {
		return this.uic;
	}
	
	/**********************************************************************
	 * LOCK
	 **********************************************************************/
	
	private void waitForResponse(final Object lock) {
		synchronized(lock) {
			try {
				lock.wait();
			} catch (InterruptedException e) {
				Logger.log("UI Lock communication problem");
				e.printStackTrace();
			}
		}
	}
	
	/**********************************************************************
	 * PLAYER
	 **********************************************************************/

	@Override
	public Suit chooseTrump() {
		uic.updateHand(getHand().getAsList(), null);
		Suit trump = uic.fetchChosenTrump();
		uic.updateTrump(trump);
		Logger.log(":- GUI: Chose Trump");
		return trump;
	}

	@Override
	public boolean knocks(Suit trump) {
		if ( trump == null ) {
			uic.updateMultiplier(2);
		}
		uic.updateTrump(trump);
		return uic.fetchKnockChoice(trump);
	}

	@Override
	public Card chooseCard(InformationHandle info) {
		uic.updateHand(getHand().getAsList(), getValidCards(info));
		uic.updateField(info.inspectField(),info.getTurn());
		
		Card chosen;
		
		for(;;) {
			Object lock = uic.setupCardChoice();
			
			Logger.log(":- GUI: Waiting for Card choice");
			waitForResponse(lock);
			Logger.log(":- GUI: Chose Card");
			
			chosen = uic.fetchChosenCard();
			if ( info.isValidCard(chosen)) {
				return chosen;
			} else {
				Logger.log(":- GUI: Invalid!");
				uic.informOfInvalidCardChoice();
			}
		}
	}

	private List<Card> getValidCards(InformationHandle info) {
		List<Card> valid = new ArrayList<Card>();
		for ( Card card : getHand() ) {
			if (info.isValidCard(card)) {
				valid.add(card);
			}
		}
		return valid;
	}

	@Override
	public void notify(InformationHandle info) {
		uic.updateTrump(info.inspectTrump());
		uic.updateHand(getHand().getAsList(), null);
		uic.updateField(info.inspectField(),info.getTurn());
		uic.pauze();
	}
	


	@Override
	public String identify() {
		return "GUI player";
	}

	@Override
	public void notifyOfTrick(Card[] trick, InformationHandle info) {
		uic.updateField(new ArrayList<Card>(), 0);
		uic.updateTrick(trick);
		uic.updatePoolScores(info.getCurrentAllyPoolScore(),info.getCurrentEnemyPoolScore());
		uic.pauze();
	}

	@Override
	public void notifyOfRoundScore(int ally, int enemy, int allyTotal,int enemyTotal) {
		uic.updateTotalScores(allyTotal, enemyTotal);
		uic.informOfRoundScore(ally, enemy, allyTotal, enemyTotal);
	}

	@Override
	public void notifyOfNewRound(String dealerName) {
		uic.updateHand(getHand().getAsList(), null);
		uic.setDealerName(dealerName);
		uic.updatePoolScores(0, 0);
		uic.pauze();
	}

	@Override
	public void notifyOfMultiplier(Suit trump, int multiplier) {
		uic.updateTrump(trump);
		uic.updateMultiplier(multiplier);
	}

	@Override
	public List<ShuffleCommand> chooseShuffleCommands() {
		// TODO
		List<ShuffleCommand> commands = new ArrayList<>();
		commands.add(ShuffleCommand.createRandomShuffleCommand());
		return commands;
	}

}
