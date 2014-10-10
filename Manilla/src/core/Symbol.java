package core;
/**
 * Possible symbols of a card. The following is associated with each symbol:
 * <br/>- strength: strength relative to other symbols (between 0 and 7)
 * <br/>- value: amount of points the symbol is worth
 * @author Daan Seynaeve
 */
public enum Symbol implements Comparable<Symbol> {
	SEVEN 	(0,0),
	EIGHT 	(1,0),
	NINE  	(2,0),
	JACK	(3,1),
	QUEEN	(4,2),
	KING	(5,3),
	ACE		(6,4),
	MANILLE	(7,5);
	
	/**
	 * The strength of the symbol, sense of a natural order
	 */
	private int strength;
	
	/**
	 * The value of the symbol in points
	 */
	private int value;
	
	/**
	 * Constructor
	 * @param strength
	 * @param value
	 */
	Symbol(int strength, int value) {
		setStrength(strength);
		setValue(value);
	}
	
	/**********************************************************************
	 * VALUE
	 **********************************************************************/
	
	private void setValue(int value) {
		this.value = value;
	}
	
	/**
	 * Inspect the value in points.
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Checks if this symbol has a greater value than the given symbol.
	 * @param symbol
	 * @return true if it has
	 */
	public boolean hasGreaterValueThan(Symbol symbol) {
		return this.getValue() > symbol.getValue();
	}
	
	/**********************************************************************
	 * STRENGTH
	 **********************************************************************/
	private void setStrength(int strength) {
		this.strength = strength;
	}
	
	/**
	 * Inspect the relative strength. (Number between 0 and 7)
	 */
	public int getStrength() {
		return strength;
	}
	
	/**
	 * Checks if this symbol is stronger than the given symbol.
	 * @param symbol
	 * @return true if it is
	 */
	public boolean isStrongerThan(Symbol symbol) {
		return this.getStrength() > symbol.getStrength();
	}
	
}
