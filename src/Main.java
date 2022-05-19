public class Main {
	public static void main(String[] args) {
		Deck playingDeck = new Deck();
		playingDeck.createFullDeck();
		System.out.println("PLAYING DECK IN ORDER");
		System.out.println(playingDeck);
		System.out.println("\nRANDOM PLAYING DECK");
		playingDeck.shuffle();
		System.out.println(playingDeck);
	}
}
