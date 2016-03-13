package test;

import org.junit.Test;

import player.Player;
import player.Team;
import core.FullMatch;
import core.Logger;
import AI.ArtificialPlayer;
import AI.ExampleIntelligence;
import AI.Intelligence;
import AI.daan.Project2501;
import AI.ward.*;

public class AIMatchVersusTest {
	
	private static final int MATCHES = 100000;
	private static final Class<? extends Intelligence> teamA = Project2501.class;
	private static final Class<? extends Intelligence> teamB = Giskard.class;

	@Test
	public void test() throws Exception {
		Logger.setPrinting(false);
		Logger.setRemember(false);
		Player tAp1 = new ArtificialPlayer("tAp1",teamA.newInstance());
		Player tAp2 = new ArtificialPlayer("tAp2",teamA.newInstance());
		Player tBp1 = new ArtificialPlayer("tBp1",teamB.newInstance());
		Player tBp2 = new ArtificialPlayer("tBp2",teamB.newInstance());
		Team tA = new Team(tAp1,tAp2);
		Team tB = new Team(tBp1,tBp2);
		
		for ( int i = 0 ; i < MATCHES ; i++) {
			FullMatch m = new FullMatch(tA,tB);
			m.run();
		}
		
		double ratio = ((double)tA.getMatchPoints()) / ((double)tB.getMatchPoints() + tA.getMatchPoints());
		ratio = roundTo(ratio,3);
		System.out.println(tAp1.identify() + " : " + tA.getMatchPoints());
		System.out.println(tBp1.identify() + " : " + tB.getMatchPoints());
		System.out.println("ratio " + tAp1.identify() + "/total : " + ratio);
	}

	private double roundTo(double num, int i) {
		double f = Math.pow(10, i);
		return Math.round(num*f)/f;
	}

}
