package test;

import org.junit.Test;

import player.Player;
import AI.ArtificialPlayer;
import AI.ExampleIntelligence;
import AI.Intelligence;
import AI.ward.Giskard;
import AI.daan.Project2501;
import core.Logger;
import core.Match;
public class AIVersusTest {

	private static final int RUNS = 10000;
	private static final Class<? extends Intelligence> teamA = Giskard.class;
	private static final Class<? extends Intelligence> teamB = Project2501.class;
	
	@Test
	public void test() throws Exception {
		Logger.setPrinting(false);
		Logger.setRemember(false);
		int teamAPoints = 0;
		int teamBPoints = 0;
		int teamAGames = 0;
		int teamBGames = 0;
		Player tAp1 = new ArtificialPlayer("tAp1",teamA.newInstance());
		Player tAp2 = new ArtificialPlayer("tAp2",teamA.newInstance());
		Player tBp1 = new ArtificialPlayer("tBp1",teamB.newInstance());
		Player tBp2 = new ArtificialPlayer("tBp2",teamB.newInstance());
		String teamAName = tAp1.identify();
		String teamBName = tBp1.identify();
		for ( int i = 0 ; i < RUNS ; i++ ) {
			if(i % (RUNS / 10) == 0) {
				System.out.println("Now at i = " + i);
			}
			Match m = new Match(tAp1,tAp2,tBp1,tBp2);
			m.run();
			
			teamAPoints += m.getScoreOfTeam1();
			teamAGames += (m.getScoreOfTeam1() > 0) ? 1 : 0;
			teamBPoints += m.getScoreOfTeam2();
			teamBGames += (m.getScoreOfTeam2() > 0) ? 1 : 0;
		}
		for ( int i = 0 ; i < RUNS ; i++ ) {
			if(i % (RUNS / 10) == 0) {
				System.out.println("Now at i = " + i);
			}
			Match m = new Match(tBp1,tBp2,tAp1,tAp2);
			m.run();
			
			teamAPoints += m.getScoreOfTeam2();
			teamAGames += (m.getScoreOfTeam2() > 0) ? 1 : 0;
			teamBPoints += m.getScoreOfTeam1();
			teamBGames += (m.getScoreOfTeam1() > 0) ? 1 : 0;
		}
		System.out.println("********** POINTS ***********");
		System.out.println(teamAName + ": " + teamAPoints);
		System.out.println(teamBName + ": " + teamBPoints);
		int pointsRatio = (int) (((double) teamAPoints / ((double) teamBPoints + teamAPoints))*100);
		System.out.println("Ratio (" + teamAName + "/Total): " + pointsRatio + "%");
		System.out.println("********** GAMES ***********");
		int gA = (int) Math.round((((double) teamAGames) / (RUNS*2)*100));
		int gB = (int) Math.round((((double) teamBGames) / (RUNS*2)*100));
		System.out.println(teamAName + ": " + gA + "%");
		System.out.println(teamBName + ": " + gB + "%");
		System.out.println("Draw: " + (100 - gA - gB) + "%");
	}

}
