package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a shuffle command. These are created elsewhere by a player using
 * the static factory methods. A command has a number of points, ranging from
 * 1 to 6, which indicates the entropy in the deck it causes.
 * 
 * @author Daan
 */
public abstract class ShuffleCommand {
	
	private ShuffleCommand() {}
	
	protected abstract void shuffle(List<Card> cards);
	
	protected abstract int getPoints();
	
	
	/**
	 * Creates a new shuffle command: random shuffle. <br/>
	 * This command shuffles the cards in the deck using randomization. <br/>
	 * Points: 6 <br/>
	 * Algorithm: Knuth / Fisher-Yates
	 * @return ShuffleCommand
	 */
	public static ShuffleCommand createRandomShuffleCommand() {
		return new ShuffleCommand() {
			
			@Override
			protected void shuffle(List<Card> cards) {
				Random random = new Random();
				for (int i=cards.size()-1 ; i>0 ; i--) {
					swap(cards, i, random.nextInt(i));
				}
			}
			
			private void swap(List<Card> cards, int i, int j) {
				Card c = cards.get(i);
				cards.set(i, cards.get(j));
				cards.set(j, c);
			}

			@Override
			protected int getPoints() {
				return 6;
			}
			
		};
	}
	
	/**
	 * Creates a new shuffle command: riffle shuffle. <br/>
	 * This command shuffles the cards in the deck using a riffle shuffle. <br/>
	 * Points: 3 <br/>
	 * Algorithm: Own interpretation
	 * @return ShuffleCommand
	 */
	public static ShuffleCommand createRiffleShuffleCommand() {
		return new ShuffleCommand() {

			@Override
			protected void shuffle(List<Card> cards) {
				ArrayList<Card> upper = getCards(cards, (cards.size()/2)-1, cards.size());
				ArrayList<Card> lower = getCards(cards, 0, (cards.size()/2)-1);
				ArrayList<Card> deck = new ArrayList<Card>();
				Random random = new Random();
				for (int i=0 ; i < cards.size() ; i++) {
					boolean choice = random.nextBoolean();
					if ( ( choice && !upper.isEmpty() ) || lower.isEmpty() ) {
						deck.add(upper.get(0));
						upper.remove(0);
					} else {
						deck.add(lower.get(0));
						lower.remove(0);
					}
				}
				cards = deck;
			}

			@Override
			protected int getPoints() {
				return 3;
			}
		
		};
	}
	
	/**
	 * Creates a new shuffle command: multi riffle shuffle. <br/>
	 * This command shuffles the cards in the deck using <b>n</b> riffle shuffles. <br/>
	 * Points: 3*n <br/>
	 * @param n Amount of riffle shuffles performed.
	 * @return ShuffleCommand
	 */
	public static ShuffleCommand createMultiRiffleShuffle(final int n) {
		return new ShuffleCommand() {

			@Override
			protected void shuffle(List<Card> cards) {
				for (int i = 0 ; i < n ; i++) {
					ShuffleCommand.createRiffleShuffleCommand().shuffle(cards);
				}
			}

			@Override
			protected int getPoints() {
				return 3*n;
			}
			
		};
	}
	
	/**
	 * Creates a new shuffle command: hindu shuffle. <br/>
	 * This command shuffles the cards in the deck using a hindu shuffle. <br/>
	 * Points: 2 <br/>
	 * Algorithm:<br/>
	 * <ol>
	 * <li> take a portion of the given size (<b>portionSize</b>) form the bottom of the deck </li>
	 * <li> take a small amount of cards (random size between 1 and <b>maximumDrop</b>) from the portion </li>
	 * <li> drop the small portion on top of the remainder of the cards </li>
	 * <li> repeat step 2 and 3 until the portion is gone </li>
	 * </ol>
	 * <a href="https://www.youtube.com/watch?v=0ZXhPWkro9A">Youtube video</a><br/>
	 * @param portionSize integer between 16 and 32
	 * @param maximumDrop integer between 1 and 10
	 * @throws IllegalArgumentException when not respecting above bounds.
	 * @return ShuffleCommand
	 */
	public static ShuffleCommand createHinduShuffle(final int portionSize, final int maximumDrop) {
		return new ShuffleCommand() {

			@Override
			protected void shuffle(List<Card> cards) {
				if ( portionSize < 16 || portionSize > 32 || maximumDrop < 1 || maximumDrop > 10 ) {
					throw new IllegalArgumentException();
				}
				Random r = new Random();
				//1. take a portion of the given size from the bottom of the deck
				ArrayList<Card> portion = getCards(cards, 0, portionSize );
				ArrayList<Card> remainder = getCards(cards, portionSize, cards.size());
				
				//4. repeat the following until the portion is gone
				while (!portion.isEmpty()) {
					//2. take a small amount of cards from the portion
					ArrayList<Card> drop = new ArrayList<Card>();
					int dropSize = r.nextInt(maximumDrop) + 1;
					if ( dropSize > portion.size() ) { dropSize = portion.size(); }
					int iniSize = portion.size();
					for ( int i = iniSize - dropSize  ; i < iniSize ; i++ ) {
						drop.add(portion.get(iniSize - dropSize));
						portion.remove(iniSize - dropSize);
					}
					//3. drop them on top the remainder of the cards
					remainder.addAll(drop);
				}
				cards = remainder;
				
			}

			@Override
			protected int getPoints() {
				return 2;
			}
	
		};
	}
	
	/**
	 * Creates a copy of the sublist(from,to) from cards.
	 * @param cards
	 * @param from (inclusive)
	 * @param to (exclusive)
	 */
	protected static ArrayList<Card> getCards(List<Card> cards, int from, int to) {
		ArrayList<Card> part = new ArrayList<Card>();
		for ( int i = from ; i < to ; i++ ) {
			part.add(cards.get(i));
		}
		return part;
	}



}