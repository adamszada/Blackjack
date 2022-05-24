import org.junit.Assert;
import org.junit.Test;

class DeckTest {

	@org.junit.jupiter.api.Test
	void createFullDeck() {
		Deck testDeck = new Deck();
		testDeck.createFullDeck();
		Assert.assertTrue(testDeck.getCards().size()==52);
	}

	@org.junit.jupiter.api.Test
	void shuffle() {
		Deck testDeck = new Deck();
		testDeck.createFullDeck();
		Deck randomDeck = new Deck();
		randomDeck.createFullDeck();
		randomDeck.shuffle();
		Assert.assertFalse(testDeck.getCards().equals(randomDeck.getCards()));
	}
}