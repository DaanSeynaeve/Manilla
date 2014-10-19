package gui;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.swing.JOptionPane;

import core.Card;
import core.Suit;

public class LocalUIController implements UIController {
	
	private GameFrame frame;
	
	public LocalUIController() {
		this.chosenCard = null;
	}
	
	@Override
	public void initialize() throws InvocationTargetException, InterruptedException {
		System.out.println("UIC INITIALIZING...");
		EventQueue.invokeAndWait(new Runnable() {
			public void run() {
				frame = new GameFrame();
				frame.setVisible(true);
			}
		});
	}
	
	/**********************************************************************
	 * VISUAL UPDATES
	 **********************************************************************/
	
	@Override
	public void updateHand(final List<Card> hand) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				frame.updateHand(hand);
			}
		});
	}
	
	@Override
	public void updateField(final List<Card> field, final int turn) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				frame.updateField(field, turn);
			}
		});		
	}
	
	@Override
	public void updateTrump(final Suit trump) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				frame.updateTrump(trump);
			}
		});
	}
	
	/**********************************************************************
	 * CARD CHOICE
	 **********************************************************************/
	
	private Card chosenCard;
	
	@Override
	public Object setupCardChoice() {
		final Object lock = new Object();
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					frame.attach(lock);
					frame.activateHand();
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
		return lock;
	}

	@Override
	public Card fetchChosenCard() {
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					chosenCard = frame.getSelectedCard();
					frame.deactivateHand();
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
		return chosenCard;
	}

	@Override
	public void informOfInvalidCardChoice() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				JOptionPane.showMessageDialog(frame, "Invalid Card! Please choose again.","Invalid Card",JOptionPane.ERROR_MESSAGE);
			}
		});
		
	}

	/**********************************************************************
	 * ROUND SCORE DIALOG
	 **********************************************************************/
	
	@Override
	public void informOfRoundScore(final int ally,final int enemy, final int allyTotal, final int enemyTotal) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				String message = "The round has ended.\n"
						+ "Scores:\n"
						+ "- your team: " + ally + "\n"
						+ "- enemy team: " + enemy + "\n"
						+ "Total scores:\n"
						+ "- your team: " + allyTotal + "\n"
						+ "- enemy team: " + enemyTotal;
				JOptionPane.showMessageDialog(frame, message,"Round ended",JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}
	
	/**********************************************************************
	 * NAMES & STRINGS
	 **********************************************************************/
	
	@Override
	public void setNames(final String playerName, final String other1, final String other2, final String other3) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				frame.updateNames(playerName, other1, other2, other3);
			}
		});
	}
	
	@Override
	public void setFrameTitle(final String title) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				frame.setTitle(title);
			}
		});
	}
	
	/**********************************************************************
	 * TRUMP DIALOG
	 **********************************************************************/
	
	private Suit trump;

	@Override
	public Suit fetchChosenTrump() {
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					TrumpDialog trumpDialog = new TrumpDialog(frame,true);
					trump = trumpDialog.getTrump();
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
		return trump;
	}

}
