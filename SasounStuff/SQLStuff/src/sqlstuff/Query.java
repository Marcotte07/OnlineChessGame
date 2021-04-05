package sqlstuff;

import java.lang.IllegalArgumentException;

public class Query {
	private int numAttempts = 0;
	
	public int getNumAttempts() {
		return numAttempts;
	}
	
	public void setNumAttempts(int numAttempts) {
		if (numAttempts < 0)
			throw new IllegalArgumentException("Tried to set numAttempts to " + numAttempts);
		this.numAttempts = numAttempts;
	}
	
	static void sendEmail(String username) {
		// TODO: how the hell do i send an email?
	}
	
	
	public static void main(String args[]) {
		System.out.println("Sasoun is the coolest");
	}
}
