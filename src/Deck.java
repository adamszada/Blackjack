import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {
	public static final int ACE_VALUE = 11;
	public static final int BLACKJACK_VALUE = 21;
	public static final int START_VALUE = 0;
	public static final int KING_VALUE = 10;
	private List<Card> cards;

	public Deck(){
		this.cards = new ArrayList<Card>();
	}

	public List<Card> getCards() {
		return cards;
	}

	public Card getCard(int index){
		return cards.get(index);
	}

	public void removeCard(int index){
		cards.remove(index);
	}

	public void clearDeck(){
		this.cards.clear();
	}

	public int getSize(){
		return cards.size();
	}
	public void createFullDeck(){
		for(Suit cardSuit: Suit.values()){
			for(Rank cardValue: Rank.values()){
				this.cards.add(new Card(cardSuit,cardValue));
			}
		}
	}

	public void shuffle(){
		List<Card> tmpDeck = new ArrayList<Card>();
		Random random = new Random();
		int CardIndex = START_VALUE;
		int originalSize = this.cards.size();
		for(int i=0;i<originalSize;i++){
			CardIndex = random.nextInt((this.cards.size()-1)+1);
			tmpDeck.add(this.cards.get(CardIndex));
			this.cards.remove(CardIndex);
		}
		this.cards = tmpDeck;
	}

	public String toString() {
		String cardListOutput = "";
		int i = START_VALUE;
		for(Card tmpCard: this.cards){
			cardListOutput += "\n" + i + " " + tmpCard.toString();
			i++;
		}
		return cardListOutput;
	}

	public String toStringMultiplayer(){
		String cardListOutput = "";
		int i = START_VALUE;
		for(Card tmpCard: this.cards){
			cardListOutput += " " + tmpCard.toString();
			i++;
		}
		return cardListOutput;
	}

	public void draw(Deck origin){
		this.cards.add(origin.getCard(START_VALUE));
		origin.removeCard(START_VALUE);
	}

	public int getCardsValue(){
		int value = START_VALUE;
		int aces = START_VALUE;
		for(Card Card: this.cards){
			value += Card.getValue();
			if (Card.getValue() == ACE_VALUE){
				aces++;
			}
		}
		if (value > BLACKJACK_VALUE && aces > START_VALUE){
			while(aces > START_VALUE && value > BLACKJACK_VALUE){
				aces--;
				value -= KING_VALUE;
			}
		}
		return value;
	}

	public void moveCardtoDeck(Deck tmp){
		for(int i=0;i<tmp.cards.size();i++)
			this.cards.add(tmp.getCard(i));
	}

}




