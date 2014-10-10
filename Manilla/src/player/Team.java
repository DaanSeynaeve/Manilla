package player;

import core.Card;

/**
 * Represents a team of players. A team also has a pool of cards
 * containing the cards they have won during a certain period of
 * time. A team also has a total score, and a number of match points.
 * for the entire match.
 * 
 * @author Daan Seynaeve
 *
 */
public class Team {
	
	/**
	 * Constructs a team of 2 players.
	 * @param player1
	 * @param player2
	 */
	public Team(Player player1, Player player2) {
		setPlayer1(player1);
		setPlayer2(player2);
		pool = new Pool();
		matchPoints = 0;
	}

	/**********************************************************************
	 * PLAYERS
	 **********************************************************************/
	
	private Player player1;
	private Player player2;
	
	public Player getPlayer1() {
		return player1;
	}

	private void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	private void setPlayer2(Player player2) {
		this.player2 = player2;
	}
	
	/**
	 * Sets player n to the given player
	 * @param n
	 * @param player
	 */
	public void setPlayer(int n, Player player) {
		if ( n == 1 ) {
			setPlayer1(player);
		} else {
			setPlayer2(player);
		}
	}
	
	/**
	 * Checks if the given player is in this team.
	 * @param player
	 * @return true if he is
	 */
	public boolean hasPlayer(Player player) {
		return (getPlayer1() == player || getPlayer2() == player);
	}
	
// TODO: needed?
//	/**
//	 * Returns player n
//	 * @param n
//	 * @return player
//	 */
//	public Player getPlayer(int n) {
//		if ( n == 1 ) {
//			return getPlayer1();
//		} else {
//			return getPlayer2();
//		}
//	}
	
	
	/**
	 * Returns the other player in the team
	 * @param	player
	 * @return	The other player
	 */
	public Player getTeamPlayerOf(Player player) {
		if ( player == getPlayer1() ) {
			return getPlayer2();
		} else {
			return getPlayer1();
		}
	}
	
	/**********************************************************************
	 * POOL
	 **********************************************************************/
	
	private Pool pool;
	
	public Pool getPool() {
		return pool;
	}
	
	/**
	 * Inspect the pool score for the team
	 * @return getPool().getScore();
	 */
	public int getPoolScore() {
		return pool.getScore();
	}
	
	/**
	 * Inspect the last trick the team took
	 * @return getPool().getCards();
	 */
	public Card[] getLastTrick() {
		return pool.getCards();
	}
	
	/**********************************************************************
	 * TOTAL SCORE
	 **********************************************************************/
	
	private int totalScore;
	
	public int getTotalScore() {
		return totalScore;
	}
	
	/**
	 * Increases the total score, if the amount is negative, nothing will be added
	 * @param amount
	 */
	public void increaseTotalScore(int amount) {
		if ( amount > 0 ) {
			this.totalScore += amount;
		}
	}
	
	private void setTotalScore(int score) {
		this.totalScore = score;
	}
	
	public void resetTotalScore() {
		setTotalScore(0);
	}
	
	/**********************************************************************
	 * MATCHPOINTS
	 **********************************************************************/
	
	private int matchPoints;
	public void addMatchPoint() {
		matchPoints++;
	}
	public int getMatchPoints() {
		return matchPoints;
	}
	
}
