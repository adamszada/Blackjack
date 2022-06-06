public interface OutputHandler {
	enum Type{
		BET,
		DRAW_CARD,
		CHOICE,
		NUMBER_ERROR,
		STATS
	}
	static String getMessage(Type type){
		String output = "";
		switch (type){
			case BET:
				output = "How much would you like to bet? Current balance: PLN ";
				break;
			case CHOICE:
				output = "Press (1) to hit or (2) to stand";
				break;
			case NUMBER_ERROR:
				output = "That's not a number!\nTry again";
				break;
			case DRAW_CARD:
				output = "Taken card: ";
				break;
			default:
				output = "Something went wrong!";
				break;
		}
		return output;
	}
}
