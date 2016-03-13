package AI.daan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import player.InformationHandle;
import AI.Intelligence;
import AI.daan.Deducer.Possibility;
import core.Card;
import core.ShuffleCommand;
import core.Suit;

public class MonteCarloAI implements Intelligence {
	
	private Random rand;
	private Deducer deducer;
	
	private static int N = 100; // number of simulations per card
	private static int M = 10; // number of steps for each simulation
	
	private int trickNumber; // temp var to maintain current trick number, functionality should be present in the handle instead
	
	public MonteCarloAI() {
		this.rand = new Random();
	}

	/**
	 * Will run a number of simulations for each valid card and select
	 * the one with the best average results.
	 * Each simulation is based on a (pseudo-)random starting state
	 * which satisfies all constraints that have been deduced so far.
	 */
	@Override
	public Card chooseCard(List<Card> hand, InformationHandle info) {
		List<Card> valid = new ArrayList<Card>();
		for ( Card card : hand ) {
			if ( info.isValidCard(card) ) {
				valid.add(card);
			}
		}
		if ( valid.isEmpty() ) {
			throw new RuntimeException("unexpected");
		}
		
		List<Possibility> ps = new ArrayList<Possibility>();
		for ( int j = 0 ; j < N ; j++ ) {
			ps.add(deducer.generatePossibility());
		}
		
		TreeMap<Integer,Card> scores = new TreeMap<Integer,Card>();
		for ( Card option : valid ) {
			List<Card> optHand = new ArrayList<Card>(hand);
			optHand.remove(option);
			List<Card> optField = new ArrayList<Card>(info.inspectField());
			optField.add(option);
			
			int sum = 0;
			for ( Possibility p : ps ) {
				Simulation s = new Simulation(info.inspectTrump(),optField,
						optHand,p.enemy1,p.ally,p.enemy2);
				
				/*System.out.println("----");
				for (Card c : s.getMe()) {
					System.out.println(c);
				}
				System.out.println("----");
				for (Card c : s.getEnemy1()) {
					System.out.println(c);
				}
				System.out.println("----");
				for (Card c : s.getAlly()) {
					System.out.println(c);
				}
				System.out.println("----");
				for (Card c : s.getEnemy2()) {
					System.out.println(c);
				}
				System.out.println("----");*/
				
				sum += s.run(Math.min(M, 9-trickNumber));
				
			}
			scores.put(sum/N,option);
			// throw new RuntimeException("intended stop");
		}
		
		return scores.lastEntry().getValue();	
	}

	@Override
	public void notify(List<Card> hand, InformationHandle info) {		
		
		List<Card> field = info.inspectField();
		List<Card> reducedField = field.subList(0, field.size()-1);
		
		int turn = reducedField.size();
		Card p = field.get(turn);
		
		int myTurn = info.getTurn();
		
		if (turn != myTurn) {
			deducer.feedAction((turn-myTurn+3) % 4, p, reducedField, info.inspectTrump());
		}
		
		//System.out.println(deducer.getLog());
		
		// update of the trick number
		if ( field.size() == 4) {
			trickNumber++;
			//System.out.println("Trick is now: " + trickNumber);
		}
		
	}
	
	
	/**
	 * Project 2501 trump algo
	 */
	private final int CORRECTION = 3;
	@Override
	public Suit chooseTrump(List<Card> hand) {
		Map<Suit,Double> amounts = new HashMap<Suit,Double>();
		for ( Suit s : Suit.values()) {
			amounts.put(s, (double) 0);
		}
		for ( Card c : hand ) {
			double x = amounts.get(c.getSuit()) + c.getSymbol().getStrength() + CORRECTION;
			amounts.put(c.getSuit(), x);
		}
		Suit best = Suit.HEART;
		for ( Suit s : amounts.keySet() ) {
			if ( amounts.get(s) > amounts.get(best)) { best = s; }
		}
		return best;
	}

	/**
	 * Returns the name of the AI.
	 */
	@Override
	public String identify() {
		return "[Monte Carlo]";
	}
	
	private final int KNOCK_TRESHOLD = 10;

	@Override
	public boolean chooseToKnock(List<Card> hand, Suit trump) {
		int power = 0;
		for ( Card card : hand ) {
			if ( card.getSuit().equals(trump) ) {
				power += card.getSymbol().getStrength();
			}
		}
		return (power > KNOCK_TRESHOLD);
	}
	
	@Override
	public List<ShuffleCommand> chooseShuffleCommands() {
		return Arrays.asList(ShuffleCommand.createRandomShuffleCommand());
	}

	@Override
	public void notifyOfHand(List<Card> hand) {
		this.trickNumber = 1;
		deducer = new Deducer();
		deducer.feedHand(hand);
	}
	
}
