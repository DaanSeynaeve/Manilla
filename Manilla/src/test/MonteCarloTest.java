package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import AI.daan.Deducer;
import AI.daan.Deducer.Possibility;
import AI.daan.Simulation;
import core.Card;
import core.Deck;
import core.ShuffleCommand;
import core.Suit;

public class MonteCarloTest {

	@Test
	public void test() {		
		Suit trump = Suit.HEART;
		ArrayList<ArrayList<Card>> hands = new ArrayList<>();
		
		Deck t = new Deck();
		t.execute(ShuffleCommand.createRandomShuffleCommand());
		for (int h = 0 ; h < 4 ; h++) {
			hands.add(new ArrayList<Card>());
			for (int j = 0 ; j < 8 ; j++) {
				hands.get(h).add(t.draw());
			}
		}
		
		Deducer d = new Deducer();
		d.feedHand(hands.get(3));
		
		double start = System.nanoTime();
		
		List<Possibility> ps = new ArrayList<>();
		int P = 1;
		for ( int x = 0 ; x < P ; x++) {
			ps.add(d.generatePossibility());
		}
		
		int M = 6;
		int best = -100;
		Card bestCard = null;
		for ( Card c : hands.get(0) ) {
			List<Card> optHand = new ArrayList<>(hands.get(0));
			optHand.remove(c);
			List<Card> optField = new ArrayList<Card>();
			optField.add(c);
			int sum = 0;
			for ( int x = 0 ; x < P; x++) {
				Possibility p = ps.get(x);
				Simulation s = new Simulation(trump, optField,
						optHand, p.enemy1, p.ally, p.enemy2);
				sum += s.run(M);
			}
			System.out.println(c + " : " + ((float) sum / (float) P));
			if ( sum > best ) {
				best = sum;
				bestCard = c;
			}
		}
		
		System.out.println("---\nBest: " + bestCard);
		
		double stop = System.nanoTime();
		System.out.println("Time (seconds) : " + ((stop-start) / Math.pow(10, 9)));
		
		/*
		System.out.println(d.getUncertain(1).size());
		for ( Card c : d.getUncertain(0) ) {
			int N = 0;
			for ( Possibility pi : p) {
				if ( pi.enemy1.contains(c) ) {
					N += 1;
				}
			}
			System.out.println(c + " : " + ((float) N / (float)P));
		}*/
		
	}

}
