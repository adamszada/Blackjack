public class Card {
	private final Suit suit;
	private final Rank rank;

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
		return rank.getRankValue();
	}

	public String toString(){
		return ("["+ getRank() +" of "+ getSuit() + "] ("+this.getValue()+")");
	}

}

