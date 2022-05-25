import java.util.ArrayList;
import java.util.Random;

public class Deck {
	private ArrayList<Card> cards;

	public Deck(){
		this.cards = new ArrayList<Card>();
	}

	public ArrayList<Card> getCards() {
		return cards;
	}

	public Card getCard(int index){
		return cards.get(index);
	}

	public void removeCard(int index){
		cards.remove(index);
	}

	public void createFullDeck(){
		for(Suit cardSuit: Suit.values()){
			for(Rank cardValue: Rank.values()){
				this.cards.add(new Card(cardSuit,cardValue));
			}
		}
	}

	public void shuffle(){
		ArrayList<Card> tmpDeck = new ArrayList<Card>();
		Random random = new Random();
		int CardIndex = 0;
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
		int i = 0;
		for(Card tmpCard: this.cards){
			cardListOutput += "\n" + i + " " + tmpCard.toString();
			i++;
		}
		return cardListOutput;
	}
}




