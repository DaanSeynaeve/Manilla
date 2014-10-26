package exception;

public class IllegalShuffleException extends Exception {

	private static final long serialVersionUID = -5446624723157358288L;
	
	public String toString() {
		return "The issued shuffle commands were unvalid\n" + super.toString();
	}

}
