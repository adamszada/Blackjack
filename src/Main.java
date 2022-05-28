import java.util.Scanner;

public class Main {
	public static void main(String[] args) {

		Deck playingDeck = new Deck();

		playingDeck.createFullDeck();

		System.out.println("PLAYING DECK IN ORDER");
		System.out.println(playingDeck);
		System.out.println("\nRANDOM PLAYING DECK");

		playingDeck.shuffle();

		System.out.println(playingDeck);


		Person player = new Person();
		Person dealer = new Person();

		Scanner sc = new Scanner(System.in);
		while(player.getPersonalMoney()>0){
			System.out.println("U've "+player.getPersonalMoney()+" how much would u like to bet?");
			double bet = sc.nextDouble();
			player.getPersonalDeck().draw(playingDeck);
			player.getPersonalDeck().draw(playingDeck);
			dealer.getPersonalDeck().draw(playingDeck);
			dealer.getPersonalDeck().draw(playingDeck);

			while (true){
				System.out.print("Ur hand: ");
				System.out.println(player.getPersonalDeck());
				System.out.println("Ur deck is valued at: "+player.getPersonalDeck().getCardsValue());
				System.out.println("Press (1) to hit or (2) to stand");

				int choose = sc.nextInt();
				switch(choose){
					case 1:
						player.getPersonalDeck().draw(playingDeck);
						System.out.println(player.getPersonalDeck().getCard(player.getPersonalDeck().getSize()-1));

						if(player.getPersonalDeck().getCardsValue()>21) {
							System.out.print("U suck! "+player.getPersonalDeck().getCardsValue());
							player.setPersonalMoney(player.getPersonalMoney()-bet);
							System.exit(0);
						}
						break;
				}
			}
		}
	}
}
