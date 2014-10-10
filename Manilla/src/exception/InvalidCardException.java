package exception;

import core.Card;

public class InvalidCardException extends Exception {
	
	private static final long serialVersionUID = -3872951161842873464L;
	private Card card;

	public InvalidCardException(Card card) {
		this.card = card;
	}

	public String toString() {
		return "Invalid card at this time: " + card.toString() + "\n" + super.toString();
	}
}
