package test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import core.Card;
import core.Deck;

public class DeckTest {

	@Test
	public void testShuffle() {
		double sd_acc_average = 0;
		double sd_acc_variance = 0;
		double ssg_acc_average = 0;
		double ssg_acc_variance = 0;
		final int runs = 10000;
		double[] sd = new double[runs];
		double[] ssg = new double[runs];
		
		for ( int x = 0 ; x < runs ; x++) {
			Deck d = new Deck();
			// according to literature 7 riffle shuffles is enough to produce a random result
			// d.multiRiffleShuffle(7);
			// d.shuffle();
			sd[x] = getSubsequentSymbolDistance(d) / 31;
			ssg[x] = getSuitSubGroups(d);
			sd_acc_average += sd[x];
			ssg_acc_average += ssg[x];
		}
		for ( int x = 0 ; x < runs ; x++) {
			sd_acc_variance += (sd[x] - sd_acc_average / runs)*(sd[x] - sd_acc_average / runs);
			ssg_acc_variance += (ssg[x] - ssg_acc_average / runs)*(ssg[x] - ssg_acc_average / runs);
		}
		System.out.println("runs: " + runs);
		System.out.println("# Average symbol distance between subsequent cards:");
		System.out.println("average: " + sd_acc_average / runs);
		System.out.println("sqrt(variance): " + Math.sqrt(sd_acc_variance / runs));
		
		System.out.println("\n# Suit subgroups:");
		System.out.println("average: " + ssg_acc_average / runs);
		System.out.println("sqrt(variance): " + Math.sqrt(ssg_acc_variance / runs));
		
	}
	
	private double getSubsequentSymbolDistance(Deck deck) {
		List<Card> cards = deck.getCards();
		int total = 0;
		int i = 0;
		for (int j = 1 ; j < cards.size() ; j++) {
			Card a = cards.get(i);
			Card b = cards.get(j);
			total += Math.abs(a.compareTo(b));
			i = j;
		}
		return total;
	}
	
	private int getSuitSubGroups(Deck deck) {
		List<Card> cards = deck.getCards();
		int i = 0;
		int total = 1;
		for ( int j = 1 ; j < cards.size() ; j++) {
			if ( !cards.get(j).getSuit().equals(cards.get(i).getSuit())) {
				total++;
			}
			i = j;
		}
		return total;
	}

}
