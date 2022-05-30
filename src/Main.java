import java.util.Scanner;

public class Main {
	public static void main(String[] args) {

		Deck playingDeck = new Deck();

		playingDeck.createFullDeck();

		//System.out.println("PLAYING DECK IN ORDER");
		//System.out.println(playingDeck);
		//System.out.println("\nRANDOM PLAYING DECK");

		playingDeck.shuffle();

		//System.out.println(playingDeck);


		Person player = new Person();
		Person dealer = new Person();
		boolean endRound = false;
		Scanner sc = new Scanner(System.in);
		while(player.getPersonalMoney()>0){
			System.out.println("U've "+player.getPersonalMoney()+"PLN how much would u like to bet?");
			double bet = sc.nextDouble();
			player.getPersonalDeck().draw(playingDeck);
			player.getPersonalDeck().draw(playingDeck);
			dealer.getPersonalDeck().draw(playingDeck);
			dealer.getPersonalDeck().draw(playingDeck);
			endRound = false;
			while (!endRound){
				System.out.print("Ur hand: ");
				System.out.println(player.getPersonalDeck());
				System.out.println("Dealer's deck: "+dealer.getPersonalDeck().getCard(0)+" [Hidden]");
				System.out.println("Ur deck is valued at: "+player.getPersonalDeck().getCardsValue());
				System.out.println("Press (1) to hit or (2) to stand");
				int choose = sc.nextInt();
				switch(choose){
					case 1:
						player.getPersonalDeck().draw(playingDeck);
						System.out.println("U draw: "+player.getPersonalDeck().getCard(player.getPersonalDeck().getSize()-1));
						if(player.getPersonalDeck().getCardsValue()>21){
							System.out.println("U suck! "+player.getPersonalDeck().getCardsValue());
							player.setPersonalMoney(player.getPersonalMoney()-bet);
							endRound = true;
						}
						break;
					case 2:
						break;
				}
				if(dealer.getPersonalDeck().getCardsValue() > player.getPersonalDeck().getCardsValue() && !endRound){
					System.out.println("Dealer wins! "+player.getPersonalDeck().getCardsValue()+' '+dealer.getPersonalDeck().getCardsValue());
					player.setPersonalMoney(player.getPersonalMoney()-bet);
					endRound = true;
				}
				while(dealer.getPersonalDeck().getCardsValue()<=16 && !endRound){
					dealer.getPersonalDeck().draw(playingDeck);
					System.out.println("Dealer draws: " +dealer.getPersonalDeck().getCard(dealer.getPersonalDeck().getSize()-1));

				}
				if((dealer.getPersonalDeck().getCardsValue()>21 || dealer.getPersonalDeck().getCardsValue() < player.getPersonalDeck().getCardsValue()) && !endRound){
					System.out.println("U win! "+player.getPersonalDeck().getCardsValue()+' '+dealer.getPersonalDeck().getCardsValue());
					player.setPersonalMoney(player.getPersonalMoney()+bet);
					endRound = true;
				}
				if(dealer.getPersonalDeck().getCardsValue() == player.getPersonalDeck().getCardsValue() && !endRound){
					System.out.println("Push "+player.getPersonalDeck().getCardsValue()+' '+dealer.getPersonalDeck().getCardsValue());
					endRound = true;
				}
				player.clearDeck();
				dealer.clearDeck();
				System.out.println("END OF ROUND!");
			}
		}


	}
}
