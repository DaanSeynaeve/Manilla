package core;

import java.util.Random;

import exception.IllegalShuffleException;
import exception.InvalidCardException;
import player.Player;
import player.Team;

/**
 * Main class of the system.
 * As for now, the run() method simulates a single round.
 * @author Daan
 */
public class FullMatch {
	
	private Team t1;
	private Team t2;
	private Player[] players;
	
	/**
	 * Creates a new match between the given teams.
	 */
	public FullMatch(Team t1, Team t2) {
		this.t1 = t1;
		this.t2 = t2;
		Player[] players = {t1.getPlayer1(),t1.getPlayer2(),t2.getPlayer1(),t2.getPlayer2()};
		this.players = players;
	}

	public void run() throws InvalidCardException, IllegalShuffleException {
		
		Logger.log("========== PLAYERS ==========");
		Logger.log(players[0] + " : " + players[0].identify());
		Logger.log(players[1] + " : " + players[1].identify());
		Logger.log(players[2] + " : " + players[2].identify());
		Logger.log(players[3] + " : " + players[3].identify());
		
		Logger.log("========== TEAMS ==========");
		Logger.log("Team 1: " + t1.getPlayer1() + ", " + t1.getPlayer2());
		Logger.log("Team 2: " + t2.getPlayer1() + ", " + t2.getPlayer2());
		
		Player dealer = players[new Random().nextInt(4)];
		Deck deck = new Deck();
		deck.execute(ShuffleCommand.createRandomShuffleCommand());
		int baseMultiplier = 1;
		
		while ( Math.max(t1.getTotalScore(), t2.getTotalScore()) < 101 ) {
			Round r = new Round(t1,t2,dealer,deck,baseMultiplier);
			r.run();
		
			// score calculation
			int roundScoreTeam1 = 0;
			int roundScoreTeam2 = 0;
			if ( t1.getPoolScore() > 30 ) {
				roundScoreTeam1  = (t1.getPoolScore() - 30)*r.getMultiplier();
			} else {
				roundScoreTeam2 = (t2.getPoolScore() - 30)*r.getMultiplier();
			}
			t1.increaseTotalScore(roundScoreTeam1);
			t2.increaseTotalScore(roundScoreTeam2);
			// multiplier
			if ( roundScoreTeam1 == 0 && roundScoreTeam2 == 0 ) {
				baseMultiplier = 2;
			} else {
				baseMultiplier = 1;
			}
			
			// notify players  & log scores
			t1.getPlayer1().notifyOfRoundScore(roundScoreTeam1, roundScoreTeam2, t1.getTotalScore(), t2.getTotalScore() );
			t1.getPlayer2().notifyOfRoundScore(roundScoreTeam1, roundScoreTeam2, t1.getTotalScore(), t2.getTotalScore() );
			t2.getPlayer1().notifyOfRoundScore(roundScoreTeam2, roundScoreTeam1, t2.getTotalScore(), t1.getTotalScore() );
			t2.getPlayer2().notifyOfRoundScore(roundScoreTeam2, roundScoreTeam1, t2.getTotalScore(), t1.getTotalScore() );
			Logger.log("========== SCORES ==========");
			Logger.log("Team 1: " + t1.getTotalScore());
			Logger.log("Team 2: " + t2.getTotalScore());
			
			// recalculate the deck
			deck = new Deck(t1.getPool(),t2.getPool());
			t1.getPool().reset();
			t2.getPool().reset();
			
			// new dealer
			dealer = getSuccessor(dealer,t1,t2);
			
			// shuffle
			deck.execute(ShuffleCommand.createMultiRiffleShuffle(3));
		}
		(t1.getTotalScore() >= 101 ? t1 : t2).addMatchPoint();
		t1.resetTotalScore();
		t2.resetTotalScore();
		Logger.log("========== MATCH ENDED ==========");
		Logger.log("Team 1: " + t1.getTotalScore());
		Logger.log("Team 2: " + t2.getTotalScore());
	}
	
	private Player getSuccessor(Player player, Team team1, Team team2) {
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

}