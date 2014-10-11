package core;

import AI.ExampleIntelligence;
import AI.ArtificialPlayer;
import exception.InvalidCardException;
import player.Team;

/**
 * Main class for the application. The main method of the
 * match class will setup the players, teams, intelligences, ...
 * and initiate rounds.
 * @author Daan
 */
public class Match {

	public static void main(String[] args) throws InvalidCardException {
		
		ArtificialPlayer t1p1 = new ArtificialPlayer("Freddy", new ExampleIntelligence());
		ArtificialPlayer t1p2 = new ArtificialPlayer("Patrick", new ExampleIntelligence());
		ArtificialPlayer t2p1 = new ArtificialPlayer("Bertha", new ExampleIntelligence());
		ArtificialPlayer t2p2 = new ArtificialPlayer("Joeri", new ExampleIntelligence());
		
		System.out.println("========== PLAYERS ==========");
		System.out.println(t1p1 + " : " + t1p1.getIntelligence().identify());
		System.out.println(t1p2 + " : " + t1p2.getIntelligence().identify());
		System.out.println(t2p1 + " : " + t2p1.getIntelligence().identify());
		System.out.println(t2p2 + " : " + t2p2.getIntelligence().identify());
		
		Team t1 = new Team(t1p1,t1p2);
		Team t2 = new Team(t2p1,t2p2);
		
		System.out.println("========== TEAMS ==========");
		System.out.println("Team 1: " + t1p1 + ", " + t1p2);
		System.out.println("Team 2: " + t2p1 + ", " + t2p2);
		
		Round r1 = new Round(t1,t2,t1p1);
		
		// score calculation
		r1.run();
		if ( t1.getPoolScore() > 30 ) {
			t1.increaseTotalScore((t1.getPoolScore() - 30)*r1.getMultiplier());
		} else {
			t2.increaseTotalScore((t2.getPoolScore() - 30)*r1.getMultiplier());
		}
		t1.getPool().reset();
		t2.getPool().reset();
		
		System.out.println("========== SCORES ==========");
		System.out.println("Team 1: " + t1.getTotalScore());
		System.out.println("Team 2: " + t2.getTotalScore());
	}

}