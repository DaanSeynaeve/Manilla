package gui;

import java.util.ArrayList;

import player.InformationHandle;
import player.Player;
import core.Card;
import core.Logger;
import core.Suit;

public class GraphicalPlayer extends Player {
	
	/**
	 * between 0 and 100
	 */
	private static final double SPEED = 50;

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
		uic.updateHand(getHand().getAsList());
		Suit trump = uic.fetchChosenTrump();
		uic.updateTrump(trump);
		Logger.log(":- GUI: Chose Trump");
		return trump;
	}

	@Override
	public boolean knocks(Suit trump) {
		uic.updateTrump(trump);
		return false; //TODO
	}

	@Override
	public Card chooseCard(InformationHandle info) {
		uic.updateHand(getHand().getAsList());
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

	@Override
	public void notify(InformationHandle info) {
		uic.updateTrump(info.inspectTrump());
		uic.updateHand(getHand().getAsList());
		uic.updateField(info.inspectField(),info.getTurn());
		pauze();
	}
	
	private void pauze() {
		try {
			Thread.sleep((int) Math.floor((1-(SPEED/100))*1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String identify() {
		return "GUI player";
	}

	@Override
	public void notifyOfTrick(Card[] trick) {
		uic.updateField(new ArrayList<Card>(), 0);
		uic.updateTrick(trick);
		pauze();
	}

	@Override
	public void notifyOfRoundScore(int ally, int enemy, int allyTotal,int enemyTotal) {
		uic.informOfRoundScore(ally, enemy, allyTotal, enemyTotal);
	}

	@Override
	public void notifyOfNewRound() {
		uic.updateHand(getHand().getAsList());
		pauze();
	}

}
