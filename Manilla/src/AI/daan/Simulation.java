package AI.daan;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import core.Card;
import core.Hand;
import core.Suit;

public class Simulation {
	
	private List<Card> field;
	private List<List<Card>> hands;
	private int starter;
	private Suit trump;
	private Random r;
	
	public Simulation(Suit trump, List<Card> field,
			List<Card> me,List<Card> enemy1,List<Card> ally,List<Card> enemy2) {
		this.hands = new ArrayList<>();
		this.hands.add(new ArrayList<Card>(me)); // 0
		this.hands.add(new ArrayList<Card>(enemy1)); // 1
		this.hands.add(new ArrayList<Card>(ally)); // 2
		this.hands.add(new ArrayList<Card>(enemy2)); // 3
		this.field = new ArrayList<>(field);
		this.starter = 1;
		this.trump = trump;
		this.r = new Random();
		
		//System.out.println("-- Sim run:");
		//System.out.println("\tSim option: " + field.get(field.size()-1));
	}
	
	/**
	 * Runs a random simulation which spans over minimum(N,remaining) tricks,
	 * where 'remaining' is the maximum number of remaining cards in any player
	 * hand. The difference (positive or negative) between both teams 
	 * in points accumulated during the simulation is returned.
	 * @param N
	 * @return score
	 */
	public int run(int M) { 
		//System.out.println("\tSim length: " + M);
		int difference = 0;
		int m = 0;
		
		while ( m < M && cardsLeft() ) {
			//System.out.println("\tsim step " + m);
			m = m + 1;
			
			// fill field with random cards.
			int player = starter;
			while ( field.size() < 4) {
				List<Card> valid = getValidCards(player);
				//System.out.println("\t#" + valid.size());
				int rand = r.nextInt(valid.size());
				Card choice = valid.get(rand);
				hands.get(player).remove(choice);
				field.add(choice);
				player = (player + 1) % 4;
			}
			
			int winner = getWinner();
			
			difference += ((winner % 2) == 0 ? 1 : -1) * getPoints();
			
			starter = winner;
			field.clear();
		}
		
		//System.out.println("\tSim score: " + difference);
		return difference; // TODO
	}

	private int getPoints() {
		int points = 0;
		for ( Card c : field ) {
			points += c.getValue();
		}
		return points;
	}
	
	private int getWinner() {
		return (starter + field.indexOf(getBestCard())) % 4;
	}

	private boolean cardsLeft() {
		boolean left = false;
		for ( List<Card> hand : hands) {
			left |= !hand.isEmpty();
		}
		return left;
	}
	
	private Card getBestCard() {
		Card best = field.get(0);
		for ( Card card : field ) {
			if ((card.getSuit().equals(trump) && !best.getSuit().equals(trump))
				|| (best.getSuit().equals(card.getSuit()) && card.isStrongerThan(best))) {
				best = card;
			}
		}
		return best;
	}
	
	private List<Card> getValidCards(int player) {
		List<Card> res = new ArrayList<Card>();
		for ( Card card : hands.get(player)) {
			if ( isValidCard(card,player)) {
				res.add(card);
			}
		}
		return res;
	}
	
	// CODE DUPLICATION
	
	/**
	 * Checks if the given card is valid to play at this time
	 * by the given player.
	 * This check can still be used if <i>this.trum == null</i>
	 * @param card
	 * @param player
	 * @return true if it is
	 */
	public boolean isValidCard(Card card, int player) {
		if ( field.isEmpty() ) {
			// CASE 1: the field is empty: any card is allowed
			return true;
		} else {
			// CASE 2: the field is not empty
			Suit trickSuit = getTrickSuit();
			Hand playerHand = new Hand(hands.get(player));
			//System.out.println("::-" + playerHand.getAsList().size());
			
			Card best = getBestCard();
			int bestIndex = field.indexOf(card);
			int playerIndex = field.size();
			
			boolean playerInLeadingTeam = (playerIndex - bestIndex) == 2;
			
			if ( playerInLeadingTeam ) {
				// CASE 2.1: ally is leading
				if ( card.getSuit().equals(trickSuit)) {
					// CASE 2.1.1: follow: OK
					return true;
				} else {
					// CASE 2.1.2: don't follow
					if ( hands.get(player).contains(trickSuit) ) {
						// CASE 2.1.2.1: didn't follow --> should have
						return false;
					} else {
						// CASE 2.1.2.2: couldn't follow: any card is OK
						return true;
					}
				}
			} else {
				// CASE 2.2: enemy is leading
				if ( card.getSuit().equals(trickSuit)) {
					// CASE 2.2.1: follow
					if ( getBestCard().getSuit().equals(trickSuit) ) {
						// CASE 2.2.1.1: none bought yet, must follow
						if ( playerHand.contains(trickSuit) && playerHand.getStrongestOfSuit(trickSuit).isStrongerThan(best)) {
							// CASE 2.2.1.1.1: can overpower
							if ( !card.isStrongerThan(getBestCard())) {
								// CASE 2.2.1.1.1.1: didn't overpower --> should have
								return false;
							} else {
								// CASE 2.2.1.1.1.2: did overpower: OK
								return true;
							}
						} else {
							// CASE 2.2.1.1.2: can't overpower: OK
							return true;
						}
					} else {
						// CASE 2.2.1.2: enemy bought, we have to follow, symbol doesn't matter: OK
						return true;
					}
				} else {
					// CASE 2.2.2: don't follow
					if ( playerHand.contains(trickSuit)) {
						// CASE 2.2.2.1: didn't follow --> should have
						return false;
					} else {
						// CASE 2.2.2.2: couldn't follow
						if ( getBestCard().getSuit().equals(trump) ) {
							// CASE 2.2.2.2.1: enemy bought
							if ( playerHand.contains(trump)) {
								// CASE 2.2.2.2.1.1: player has trump
								if ( playerHand.getStrongestOfSuit(trump).isStrongerThan(getBestCard())) {
									// CASE 2.2.2.2.1.1.1: player has a better trump
									if ( card.getSuit().equals(trump) && card.isStrongerThan(getBestCard())) {
										// CASE 2.2.2.2.1.1.1.1: did overpower: OK
										return true;
									} else {
										// CASE 2.2.2.2.1.1.1.2: didn't overpower --> should have
										return false;
									}
								} else {
									// CASE 2.2.2.2.1.1.2: player has worse trump
									if ( card.getSuit().equals(trump)) {
										// CASE 2.2.2.2.1.1.2.1: player underbought
										if ( playerHand.containsSuitOtherThan(trump)) {
											// CASE: couldn't do anything else
											return false;
										} else {
											// CASE: could do something else --> should have done that
											return true;
										}
										
									} else {
										// CASE 2.2.2.2.1.1.2.2: player didn't play trump: OK
										return true;
									}
								}
							} else {
								// CASE 2.2.2.2.1.2: player doesn't have trump: ok
								return true;
							}
							
						} else {
							// CASE 2.2.2.2.2: enemy did not buy, but owns highest card
							if ( playerHand.contains(trump) && !card.getSuit().equals(trump)) {
								// CASE 2.2.2.2.2.1: player can buy, buy didn't --> should have
								return false;
							} else {
								// CASE 2.2.2.2.2.2: player couldn't buy or bought when he could: OK
								return true;
							}
						}
					}
				}				
			}
		}
	}

	private Suit getTrickSuit() {
		return field.get(0).getSuit();
	}
}
