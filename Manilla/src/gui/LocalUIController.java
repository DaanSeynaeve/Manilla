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
	public void updateHand(final List<Card> hand, final List<Card> valid) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				frame.updateHand(hand, valid);
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
				JOptionPane.showMessageDialog(frame,
						"Invalid Card! Please choose again.",
						"Invalid Card",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		
	}

	/**********************************************************************
	 * ROUND SCORE DIALOG
	 **********************************************************************/
	
	@Override
	public void informOfRoundScore(final int ally,final int enemy, final int allyTotal, final int enemyTotal) {
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					String message = "The round has ended.\n"
							+ "Scores:\n"
							+ "- your team: " + ally + "\n"
							+ "- enemy team: " + enemy + "\n"
							+ "Total scores:\n"
							+ "- your team: " + allyTotal + "\n"
							+ "- enemy team: " + enemyTotal;
					JOptionPane.showMessageDialog(frame, message,"Round ended",
							JOptionPane.INFORMATION_MESSAGE);
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**********************************************************************
	 * NAMES & STRINGS
	 **********************************************************************/
	
	@Override
	public void setNames(final String playerName, final String other1, 
			final String other2, final String other3) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				frame.updateNames(playerName, other1, other2, other3);
			}
		});
	}
	
	@Override
	public void setDealerName(final String dealerName) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				frame.clearTrump();
				frame.updateDealerName(dealerName);
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
	
	/**********************************************************************
	 * UPDATE TRICK
	 **********************************************************************/

	@Override
	public void updateTrick(final Card[] trick) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				frame.updatePreviousTrick(trick);
			}
		});	
	}
	
	/**********************************************************************
	 * PAUZE
	 **********************************************************************/
	
	private double speed = 50;
	
	@Override
	public void pauze() {
		int milis = (int) Math.floor((1-(speed/100))*2000);
		//System.out.println(milis);
		try {
			Thread.sleep(milis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateMultiplier(final int multiplier) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				frame.updateMultiplier(multiplier);
			}
		});
		
	}

	boolean knock = false;
	
	@Override
	public boolean fetchKnockChoice(final Suit trump) {
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					String situation = "Enemy ";
					if ( trump == null ) {
						situation += "chose to play without trump";
					} else {
						situation += "made the trump " + trump.toString();
					}
					int n = JOptionPane.showConfirmDialog(
						    frame,
						    situation + ".\nWould you like to knock?",
						    "Knocking",
						    JOptionPane.YES_NO_OPTION);
					knock = (n == JOptionPane.YES_OPTION);
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
		return knock;
	}

	@Override
	public void updatePoolScores(final int ally, final int enemy) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				frame.updatePoolScores(ally, enemy);
			}
		});
	}
	
	@Override
	public void updateTotalScores(final int ally, final int enemy) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				frame.updateTotalScores(ally, enemy);
			}
		});
	}

}
