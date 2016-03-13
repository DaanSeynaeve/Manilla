package AI.daan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import core.Card;
import core.Deck;
import core.Suit;

public class Deducer {
	
	private class Deduction {
		public int id;
		public boolean hasCard;
		public Card card;
		
		public Deduction(int id, boolean hasCard,Card card) {
			this.id = id;
			this.hasCard = hasCard;
			this.card = card;
		}
	}
	
	private Set<Card>[] uncertain;
	private Set<Card>[] impossible;
	private Set<Card>[] certain;
	private int[] cardsLeft = {8,8,8};
	private List<Deduction> deductions;
	
	@SuppressWarnings("unchecked")
	public Deducer() {
		uncertain = (Set<Card>[]) new HashSet[3];
		impossible = (Set<Card>[]) new HashSet[3];
		certain = (Set<Card>[]) new HashSet[3];
		deductions = new ArrayList<Deduction>();
		Deck d = new Deck();
		List<Card> cards = d.getCards();
		for (int i = 0 ; i < 3 ; i++ ) {
			uncertain[i] = new HashSet<Card>(cards);
			certain[i] = new HashSet<Card>();
			impossible[i] = new HashSet<Card>();
		}
		r = new Random();
	}
	
	public void feedHand(List<Card> hand) {
		for (int i = 0 ; i < 3 ; i++ ) {
			for (Card c : hand) {
				propagateImpossible(i, c);
			}
		}
		applyDeductions();
	}
	
	/**
	 * Id = 1: Enemy<br/>
	 * Id = 2: Ally<br/>
	 * Id = 3: Enemy2<br/>
	 */
	public void feedAction(int id, Card c, List<Card> field, Suit trump) {
		if (impossible[id].contains(c)) {
			throw new RuntimeException("Logical inconsistency!");
		}
		
		cardsLeft[id]--;
		// none can have this card
		for (int i = 0 ; i < 3 ; i++) {
			uncertain[i].remove(c);
			certain[i].remove(c);
			impossible[i].add(c);
		}
		
		
		// deduce (im)possibilities resulting from action
		if ( field.size() > 0 ) {
			Suit trickSuit = field.get(0).getSuit();
			Card bestCard = getBestCard(field,trump);
			int bestIndex = field.indexOf(bestCard);
			
			// !(card trick suit) => (no trick suit in hand)
			if ( !field.get(0).getSuit().equals(c.getSuit())) {
				propagateImpossible(id,trickSuit);
			}
			
			// A : (player not winning)
			if ( (field.size() - bestIndex) % 2 != 0) {
				// B : (card not trick suit) & (card not trump)
				if ( !c.getSuit().equals(trickSuit) && !c.getSuit().equals(trump) ) {
					// A & B & (best not trump) => (no trump in hand)
					if ( !bestCard.getSuit().equals(trump) ) {
						propagateImpossible(id,trump);
					} else {
						// B & (best trump) & (enemy bought) => no higher trump
						if ( bestIndex != 0 ) {
							propagateImpossibleHigher(id,trump,bestCard);
						}
					}
				}
				
				// A & (card trick suit) & (best trick suit) & (card lower than best)
				// => no higher cards of trick suit
				if ( c.getSuit().equals(trickSuit) && bestCard.equals(trickSuit)
						&& bestCard.isStrongerThan(c)) {
					propagateImpossibleHigher(id, trickSuit, bestCard);
				}
			}	
		}
		
		// Apply deductions & Arc Consistency
		applyArcConsistency();
	}
	
	private void applyArcConsistency() {
		for ( int i = 0 ; i < 3 ; i++ ) {
			update(i);
		}
		while (!deductions.isEmpty()) {
			applyDeductions();
			for ( int i = 0 ; i < 3 ; i++ ) {
				update(i);
			}
		}
	}
	
	private void applyDeductions() {
		for (Deduction d : deductions) {
			uncertain[d.id].remove(d.card);
			if (d.hasCard) {
				certain[d.id].add(d.card);
			} else {
				impossible[d.id].add(d.card);
			}
		}
		deductions.clear();
	}
	
	private void propagateCertain(int id, Card c) {
		deductions.add(new Deduction(id,true,c));
		propagateImpossible(( id + 1 ) % 3, c);
		propagateImpossible(( id + 2 ) % 3, c);	
	}
	
	private void propagateImpossible(int id, Card c) {
		deductions.add(new Deduction(id,false,c));
	}
	
	private void propagateImpossibleHigher(int id, Suit s, Card h) {
		for ( Card uc : uncertain[id] ) {
			if ( uc.getSuit().equals(s) && uc.isStrongerThan(h) ) {
				propagateImpossible(id,uc);
			}
		}
	}
	
	private void propagateImpossible(int id, Suit s) {
		for ( Card uc : uncertain[id] ) {
			if ( uc.getSuit().equals(s) ) {
				propagateImpossible(id,uc);
			}
		}
	}

	private void update(int id) {
		if ( !uncertain[id].isEmpty()) {
			for (Card uc : uncertain[id]) {
				if (impossible[(id+1)%3].contains(uc) && impossible[(id+2)%3].contains(uc)) {
					propagateCertain(id,uc);
				}
			}
			if (uncertain[id].size() + certain[id].size() == cardsLeft[id]) {
				for ( Card uc : uncertain[id]) {
					propagateCertain(id,uc);
				}
			}
			if (certain[id].size() == cardsLeft[id]) {
				for ( Card uc : uncertain[id]) {
					propagateImpossible(id,uc);
				}
			}
		}
	}
	
	/*******************************************
	 * Query
	 *******************************************/	

	public String getLog() {
		String log = "";
		String[] names = {"Enemy1","Ally","Enemy2"};
		for ( int i = 0 ; i < 3 ; i++) {
			log += "\n" + names[i] + "(" + cardsLeft[i] + ")";
			log += "\n\t- uncertain(" + uncertain[i].size() + "): ";
			for (Card c : uncertain[i]) {
				log += shortString(c) + ",";
			}
			log += "\n\t- certain(" + certain[i].size() + "): ";
			for (Card c : certain[i]) {
				log += shortString(c) + ",";
			}
			log += "\n\t- impossible(" + impossible[i].size() + "): ";
			for (Card c : impossible[i]) {
				log += shortString(c) + ",";
			}
		}
		return log;
	}
	
	public List<Card> getPossibleCards(int id) {
		List<Card> possible = new ArrayList<>();
		possible.addAll(certain[id]);
		possible.addAll(uncertain[id]);
		return possible;
	}
	
	public int getCardsLeft(int id) {
		return cardsLeft[id];
	}
	
	/*******************************************
	 * Generate Possibilities
	 *******************************************/
	
	private Random r;
	
	public class Possibility {
		public List<Card> enemy1;
		public List<Card> ally;
		public List<Card> enemy2;
		
		private Possibility() {
			enemy1 = new ArrayList<>();
			ally = new ArrayList<>();
			enemy2 = new ArrayList<>();
		}
	}
	
	public Possibility generatePossibility() {
		backup();
		
		List<Integer> incompleteIds = new ArrayList<>(Arrays.asList(0,1,2));
		updateIncompleteIds(incompleteIds);
		while (incompleteIds.size() > 0) {
			int id = incompleteIds.get(r.nextInt(incompleteIds.size()));
			Card[] cards = uncertain[id].toArray(new Card[uncertain[id].size()]);
			propagateCertain(id, cards[r.nextInt(cards.length)]);
			applyArcConsistency();
			updateIncompleteIds(incompleteIds);
		}
		
		Possibility p = new Possibility();
		p.enemy1.addAll(certain[0]);
		p.ally.addAll(certain[1]);
		p.enemy2.addAll(certain[2]);
		restore();
		
		if (p.enemy1.size() == cardsLeft[0]
				&& p.ally.size() == cardsLeft[1]
				&& p.enemy2.size() == cardsLeft[2]) {
			return p;
		} else {
			throw new RuntimeException("Alas, our approach hath failed");
		}
		
	}
	
	private void updateIncompleteIds(List<Integer> incompleteIds) {
		for ( int i = 0 ; i < 3 ; i++) {
			if (uncertain[i].isEmpty() && incompleteIds.contains(i)) {
				incompleteIds.remove(incompleteIds.indexOf(i));
			}
		}
	}
	
	/*******************
	 * TEST
	 *******************/
	
	public Set<Card> getUncertain(int id) {
		return uncertain[id];
	}
	
	/*******************************************
	 * Backup
	 *******************************************/
	
	Set<Card>[] backupUncertain;
	@SuppressWarnings("unchecked")
	private void backup() {
		backupUncertain = (Set<Card>[]) new HashSet[3];
		for (int i = 0 ; i < 3 ; i++ ) {
			backupUncertain[i] = new HashSet<Card>(uncertain[i]);
		}
	}
	
	private void restore() {
		uncertain = backupUncertain;
		for ( int i = 0 ; i < 3 ; i++ ) {
			certain[i].removeAll(uncertain[i]);
			impossible[i].removeAll(uncertain[i]);
		}
	}
	
	/*******************************************
	 * Helper methods
	 *******************************************/
	
	private Card getBestCard(List<Card> field, Suit trump) {
		Card best = field.get(0);
		for ( Card card : field ) {
			if ((card.getSuit().equals(trump) && !best.getSuit().equals(trump))
				|| (best.getSuit().equals(card.getSuit()) && card.isStrongerThan(best))) {
				best = card;
			}
		}
		return best;
	}
	
	private String shortString(Card c) {
		String rep = "";
		switch (c.getSymbol()) {
		case ACE: rep += "A";
			break;
		case EIGHT: rep += "8";
			break;
		case JACK: rep += "J";
			break;
		case KING: rep += "K";
			break;
		case MANILLE: rep += "M";
			break;
		case NINE: rep += "9";
			break;
		case QUEEN: rep += "Q";
			break;
		case SEVEN: rep += "7";
			break;
		default:
			break;
		
		}
		return rep + c.getSuit().name().charAt(0);
	}

}
