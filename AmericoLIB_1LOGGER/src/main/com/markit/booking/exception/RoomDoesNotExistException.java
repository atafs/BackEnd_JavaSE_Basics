package main.com.markit.booking.exception;

public class RoomDoesNotExistException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public RoomDoesNotExistException(String message) {
		super(message);
	}

}
