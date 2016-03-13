package core;

import java.util.ArrayList;
import java.util.List;

import player.InformationHandle;
import player.Player;
import player.Team;
import exception.IllegalShuffleException;
import exception.InvalidCardException;


/**
 * Sequence of 8 tricks. This class is responsible for
 * all game-controlling logic that occurs within
 * a single game.
 * @author Daan
 */
public class Round {
	
	Team team1;
	Team team2;
	Player dealer;
	Suit trump;
	List<Card> field;
	Player[] sequence;
	Card[] previousTrick;
	int multiplier;
	Deck deck;
	private static final int MIN_COMMAND_POINTS = 6;
	
	public Round(Team team1, Team team2, Player dealer, Deck deck, int multiplier) {
		this.team1 = team1;
		this.team2 = team2;
		this.dealer = dealer;
		this.field = new ArrayList<Card>();
		this.sequence = new Player[4];
		this.multiplier = multiplier;
		this.previousTrick = null;
		this.deck = deck;
	}
	
	/**********************************************************************
	 * MULTIPLIER
	 **********************************************************************/
	
	public int getMultiplier() {
		return multiplier;
	}
	
	/**********************************************************************
	 * TRUMP
	 **********************************************************************/
	
	/**
	 * Returns the trump suit when one was declared.
	 * Returns null if:<br/>
	 * 1) no trump suit was declared yet.
	 * 2) the round is no-trump
	 * @return a Suit, or null
	 */
	public Suit getTrump() {
		return trump;
	}
	
	
	/**********************************************************************
	 * RUN
	 **********************************************************************/
	
	/**
	 * Executes a sequence of 8 tricks and returns the winning team
	 * @throws IllegalShuffleException 
	 * @throws InvalidCardException 
	 */
	public void run() throws IllegalShuffleException, InvalidCardException {
		// STEP 1: BUILD PLAYER SEQUENCE
		Player starter = getSuccessor(dealer);
		buildSequence(starter);
		
		// STEP 1.2: EXAMINE SEQUENCE
		Logger.log("------- PLAYER ORDER -------");
		for ( Player player : sequence ) {
			Logger.log(player);
		}
		
		// STEP 2: SHUFFLE & DEAL CARDS
		List<ShuffleCommand> commands = dealer.chooseShuffleCommands();
		if ( legalShuffleCommands(commands)) {
			deck.execute(commands);
		} else {
			throw new IllegalShuffleException();
		}
		deal323(deck);
		for ( Player player : sequence ) {
			player.notifyOfNewRound(dealer.getName());
		}
		
		// STEP 2.2: EXAMINE HANDS
		Logger.log("====== " + dealer + " deals: ======");
		for ( Player player : sequence) {
			Logger.log(player + " got the following: ");
			for ( Card c : player.getHand() ) {
				Logger.log(c);
			}
			Logger.log("---------------------------- ");
		}
		
		// STEP 3: DEFINE TRUMP & KNOCKING
		trump = dealer.chooseTrump();
		if (trump == null) {
			multiplier *= 2;
		}
		if (starter.knocks(trump)) {
			Logger.log(starter.getName() + " knocked");
			multiplier *= 2;
		} else if ( getTeamPlayer(starter).knocks(trump) ) {
			Logger.log(getTeam(starter) + " knocked.");
			multiplier *= 2;
		}
		for ( Player player : sequence ) {
			player.notifyOfMultiplier(trump, multiplier);
		}
		
		// STEP 3.2: EXAMINE THE TRUMP
		Logger.log(dealer + " chose the trump: " + trump);
		Logger.log("---------------------------- ");
		
		// STEP 4: TRICKS
		for (int trick = 1 ; trick <= 8 ; trick++) {
			// STEP 4.1: EACH PLAYER PUTS A CARD 
			for ( Player player : sequence ) {
				InformationHandle info = new InformationHandle(player, this);
				Card card = player.chooseCard(info);
				if (isValidCard(card, player)) {
					field.add(card);
					player.getHand().removeCard(card);
					Logger.log(player + " plays " + card);
					for ( Player peer : sequence ) {
						peer.notify(new InformationHandle(peer,this));
					}
				} else {
					throw new InvalidCardException(card);
				}
			}
			// STEP 4.2: DETERMINE WINNER
			Player winner = getOwner(getBestCard());
			Logger.log("-- Trick (" + trick + ") winner is " + winner + " --");
			Team winningTeam = getTeam(winner);
			
			// STEP 4.3: CARDS ARE AWARDED TO WINNING TEAMS POOL
			winningTeam.getPool().pushAll(field);
			
			// STEP 4.4: STORE THE TRICK AS PREVIOUS TRICK, NOTIFY PLAYERS & CLEAR FIELD
			storeTrick();
			
			// STEP 4.5: REBUILD PLAYER SEQUENCE
			buildSequence(winner);
		}
	}
	
	private boolean legalShuffleCommands(List<ShuffleCommand> commands) {
		int points = 0;
		for ( ShuffleCommand command : commands ) {
			points += command.getPoints();
		}
		return (points >= MIN_COMMAND_POINTS);
	}

	private void storeTrick() {
		previousTrick = new Card[4];
		for ( int i = 0 ; i < 4 ; i++ ) {
			previousTrick[i] = field.get(i);
		}
		field.clear();
		for ( Player p : sequence ) {
			p.notifyOfTrick(previousTrick, new InformationHandle(p,this));
		}
	}

	/**********************************************************************
	 * CARD PLAYING LOGIC
	 **********************************************************************/
	
	/**
	 * Checks if the given card is valid to play at this time
	 * by the given player.
	 * This check can still be used if <i>this.trum == null</i>
	 * @param card
	 * @param player
	 * @return true if it is
	 */
	public boolean isValidCard(Card card, Player player) {
		if ( field.isEmpty() ) {
			// CASE 1: the field is empty: any card is allowed
			return true;
		} else {
			// CASE 2: the field is not empty
			Suit trickSuit = getTrickSuit();
			Team leadingTeam = getTeam(getOwner(getBestCard()));
			
			if ( leadingTeam.hasPlayer(player)) {
				// CASE 2.1: ally is leading
				if ( card.getSuit().equals(trickSuit)) {
					// CASE 2.1.1: follow: OK
					return true;
				} else {
					// CASE 2.1.2: don't follow
					if ( player.getHand().contains(trickSuit) ) {
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
						if ( player.getHand().contains(trickSuit) && player.getHand().getStrongestOfSuit(trickSuit).isStrongerThan(getBestCard())) {
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
					if ( player.getHand().contains(trickSuit)) {
						// CASE 2.2.2.1: didn't follow --> should have
						return false;
					} else {
						// CASE 2.2.2.2: couldn't follow
						if ( getBestCard().getSuit().equals(trump) ) {
							// CASE 2.2.2.2.1: enemy bought
							if ( player.getHand().contains(trump)) {
								// CASE 2.2.2.2.1.1: player has trump
								if ( player.getHand().getStrongestOfSuit(trump).isStrongerThan(getBestCard())) {
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
										if ( player.getHand().containsSuitOtherThan(trump)) {
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
							if ( player.getHand().contains(trump) && !card.getSuit().equals(trump)) {
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

	/**********************************************************************
	 * SEQUENCE
	 **********************************************************************/
	
	private void buildSequence(Player starter) {
		sequence[0] = starter;
		for (int i = 1 ; i < 4 ; i++) {
			sequence[i] = getSuccessor(sequence[i-1]);
		}
	}

	private Player getSuccessor(Player player) {
		if ( player == team1.getPlayer1() ) {
			return team2.getPlayer1();
		} else if ( player == team1.getPlayer2() ) {
			return team2.getPlayer2();
		} else if ( player == team2.getPlayer1() ) {
			return team1.getPlayer2();
		} else {
			return team1.getPlayer1();
		}
	}
	
	/**
	 * Returns the index of the given player in the sequence
	 * @param player
	 * @return index
	 */
	public int getPositionInSequence(Player player) {
		for ( int i = 0 ; i < 4 ; i++ ) {
			if ( sequence[i] == player ) {
				return i;
			}
		}
		throw new RuntimeException("unexpected");
	}
	
	/**********************************************************************
	 * MISC
	 **********************************************************************/
	
	/**
	 * Returns the team of the given player (the ally team)
	 */
	public Team getTeam(Player player) {
		if ( team1.hasPlayer(player)) {
			return team1;
		} else {
			return team2;
		}
	}
	
	/**
	 * Returns the team opposing the given player (the enemy team)
	 */
	public Team getOpposingTeam(Player player) {
		if ( team1.hasPlayer(player)) {
			return team2;
		} else {
			return team1;
		}
	}
	
	/**
	 * Returns the team player of the given player
	 */
	private Player getTeamPlayer(Player player) {
		return getTeam(player).getTeamPlayerOf(player);
	}
	
	/**
	 * Returns the owner of the given card.
	 * (or null when the card isn't on the field)
	 */
	private Player getOwner(Card card) {
		return sequence[field.indexOf(card)];
	}
	
	/**
	 * Returns the best card on the field
	 */
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

	private void deal323(Deck deck) {
		for ( Player player : sequence ) {
			player.getHand().addCard(deck.draw());
			player.getHand().addCard(deck.draw());
			player.getHand().addCard(deck.draw());
		}
		for ( Player player : sequence ) {
			player.getHand().addCard(deck.draw());
			player.getHand().addCard(deck.draw());
		}
		for ( Player player : sequence ) {
			player.getHand().addCard(deck.draw());
			player.getHand().addCard(deck.draw());
			player.getHand().addCard(deck.draw());
		}
	}
	
	/**
	 * Return the suit of the first card in this trick.
	 * Returns null when the field is empty.
	 * @return a Suit, or null.
	 */
	public Suit getTrickSuit() {
		if ( !field.isEmpty() ) {
			return field.get(0).getSuit();
		} else {
			return null;
		}
	}

	public List<Card> getField() {
		return field;
	}
	
	/**
	 * Returns a clone of the previous trick if there is one,
	 * else returns null.
	 */
	public Card[] getPreviousTrickClone() {
		return previousTrick.clone();
	}
	

	public Player getDealer() {
		return this.dealer;
	}

	
}
