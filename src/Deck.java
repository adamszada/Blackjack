import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {
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

	public String toStringMultiplayer(){
		String cardListOutput = "";
		int i = 0;
		for(Card tmpCard: this.cards){
			cardListOutput += " " + tmpCard.toString();
			i++;
		}
		return cardListOutput;
	}

	public void addCard(Card addend){
		this.cards.add(addend);
	}
	public void draw(Deck origin){
		this.cards.add(origin.getCard(0));
		origin.removeCard(0);
	}

	public int getCardsValue(){
		int value = 0;
		int aces = 0;
		for(Card Card: this.cards){
			value += Card.getValue();
			if (Card.getValue() == 11){
				aces++;
			}
		}
		if (value > 21 && aces > 0){
			while(aces > 0 && value > 21){
				aces--;
				value -= 10;
			}
		}
		return value;
	}

	public void moveCardtoDeck(Deck tmp){
		for(int i=0;i<tmp.cards.size();i++)
			this.cards.add(tmp.getCard(i));
	}


	public void clearDeck(){
		this.cards.clear();
	}
}




