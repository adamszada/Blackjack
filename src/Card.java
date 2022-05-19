public class Card {
	private Suit suit;
	private Rank rank;

	public Card(Suit suit, Rank rank){
		this.suit = suit;
		this.rank = rank;
	}

	public Suit getSuit() {
		return suit;
	}

	public Rank getRank() {
		return rank;
	}


	public int getValue(){
		return getRank().rankValue;
	}



	public String toString(){
		return ("["+ getRank() +" of "+ getSuit() + "] ("+this.getValue()+")");
	}

}

