package flaynoxapi.message;

public class MessageExeption extends Exception {

	private static final long serialVersionUID = 7847505235701542474L;

	private String message;
	
	public MessageExeption(String message) {

		this.message = message;
		
	}

	@Override
	public String getMessage() {

		return message;

	}

}
