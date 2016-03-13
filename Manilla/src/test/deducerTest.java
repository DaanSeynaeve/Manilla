package test;

import org.junit.Test;

import player.Player;
import AI.ArtificialPlayer;
import AI.ExampleIntelligence;
import AI.daan.Deducer;
import AI.daan.MonteCarloAI;
import core.Logger;
import core.Match;
import exception.IllegalShuffleException;
import exception.InvalidCardException;

public class deducerTest {

	@Test
	public void test() throws InvalidCardException, IllegalShuffleException {		
		Logger.setPrinting(true);
		Logger.setRemember(false);
		Player tAp1 = new ArtificialPlayer("Dimitri",new MonteCarloAI());
		Player tAp2 = new ArtificialPlayer("Boris",new ExampleIntelligence());
		Player tBp1 = new ArtificialPlayer("Samson",new ExampleIntelligence());
		Player tBp2 = new ArtificialPlayer("Gert",new ExampleIntelligence());
		
		Match m = new Match(tAp1,tAp2,tBp1,tBp2);
		m.run();
	}

}
