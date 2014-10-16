package core;

/**
 * Represents a card, which has a suit and a symbol.
 * 
 * @author Daan Seynaeve
 * @version 0.1
 */
public final class Card implements Comparable<Card> {
	
	/**
	 * Default constructor
	 * @param	suit
	 * @param	symbol
	 */
	public Card (Suit suit, Symbol symbol) {
		this.symbol = symbol;
		this.suit = suit;
	}

	/**********************************************************************
	 * SYMBOL
	 **********************************************************************/

	private final Symbol symbol;

	/**
	 * Inspect the symbol of the card.
	 */
	public Symbol getSymbol() {
		return symbol;
	}
	
	/**********************************************************************
	 * SUIT
	 **********************************************************************/

	private final Suit suit;
	
	/**
	 * Inspect the suit of the card.
	 */
	public Suit getSuit() {
		return suit;
	}
	
	/**********************************************************************
	 * MISC & COMPARISON
	 **********************************************************************/
	
	/**
	 * Returns the points value of the card, which is the value of the symbol.
	 * @return	getSymbol.getValue();
	 */
	public int getValue() {
		return getSymbol().getValue();
	}
	
	/**
	 * Compares to another card. If the calling Card is 'stronger' than the given card,
	 * the result is a positive integer, otherwise negative. If they're equally 'strong',
	 * 0 is returned. The card strength corresponds to the strength of the symbol.
	 * @param other	The other card.
	 */
	public int compareTo(Card other) {
		if ( other == null ) {
			return 1;
		} else {
			return getSymbol().getStrength() - other.getSymbol().getStrength();
		}
	}
	
	/**
	 * Checks whether this card is stronger than the given card.
	 * @param	other
	 * 			The other card.
	 * @return	true if this card is stronger.
	 */
	public boolean isStrongerThan(Card other) {
		return this.getSymbol().isStrongerThan(other.getSymbol());
	}

	/**
	 * Checks whether this card has the same suit and symbol as the given card.
	 * @param	other
	 * 			The other card.
	 * @return	True if Suit and Symbol are equal
	 * 			| (getSymbol() == other.getSymbol()) && (getSuit() == other.getSuit())
	 */
	public boolean equals(Card other) {
		return (getSymbol() == other.getSymbol()) && (getSuit() == other.getSuit());
	}
	
	public String toString() {
		return symbol.toString() + " OF " + suit.toString() + "S";
	}
}
