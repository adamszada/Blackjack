public interface OutputHandler {

	enum Type{
		BET,
		DRAW_CARD,
		DECK_VALUE,
		MOVE_CHOICE,
		MODE_CHOICE,
		NUMBER_ERROR,
		NO_PLAYERS,
		STATS
	}

	static String getMessage(Type type){
		String output = "";
		switch (type){
			case BET:
				output = "How much would you like to bet? Current balance: PLN ";
				break;
			case MOVE_CHOICE:
				output = "Press (1) to hit or (2) to stand";
				break;
			case MODE_CHOICE:
				output = "Press (1) to run Single Player\nPress (2) to play with friends";
				break;
			case NUMBER_ERROR:
				output = "That's not a number!\nTry again";
				break;
			case DRAW_CARD:
				output = "Taken card: ";
				break;
			case DECK_VALUE:
				output = "Deck is valued at: ";
				break;
			case NO_PLAYERS:
				output = "Enter the amount of players:";
				break;
			default:
				output = "Something went wrong!";
				break;
		}
		return output;
	}
}
