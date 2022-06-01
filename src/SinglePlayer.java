import java.util.Scanner;

public class SinglePlayer {
	private final Deck playingDeck;
	private final Person player;
	private final Person dealer;
	private boolean endFlag;


	SinglePlayer(){
		this.playingDeck = new Deck();
		this.playingDeck.createFullDeck();
		this.playingDeck.shuffle();
		this.player = new Person();
		this.dealer = new Person();
		this.endFlag = false;
		runRound();
	}

	public void prepareRound(){
		playingDeck.moveCardtoDeck(player.getPersonalDeck());
		playingDeck.moveCardtoDeck(dealer.getPersonalDeck());
		player.getPersonalDeck().clearDeck();
		dealer.getPersonalDeck().clearDeck();
		playingDeck.shuffle();
	}

	public int whoWin(){
		// 1 - player | 0 - draw | -1 - dealer;

		if(dealer.getPersonalDeck().getCardsValue()>21 || dealer.getPersonalDeck().getCardsValue() < player.getPersonalDeck().getCardsValue())
			return 1;
		else if(dealer.getPersonalDeck().getCardsValue() == player.getPersonalDeck().getCardsValue())
			return 0;
		else
			return -1;
	}

	public void runRound(){
		Scanner sc = new Scanner(System.in);
		int choice;
		double bet;
		while(player.getPersonalMoney()>0){
			do{
				System.out.println("U've "+player.getPersonalMoney()+"PLN how much would u like to bet?");
				while (!sc.hasNextDouble()) {
					System.out.println("That's not a number!\nTry again");
					sc.next();
				}
				bet = sc.nextDouble();
			}while (bet<=0 || bet>player.getPersonalMoney());
			player.getPersonalDeck().draw(playingDeck);
			player.getPersonalDeck().draw(playingDeck);
			dealer.getPersonalDeck().draw(playingDeck);
			dealer.getPersonalDeck().draw(playingDeck);
			if(dealer.getPersonalDeck().getCardsValue()==21 || player.getPersonalDeck().getCardsValue()==21){
				System.out.print("Your deck: ");
				System.out.println(player.getPersonalDeck());
				System.out.println("Dealer's deck: "+dealer.getPersonalDeck());
				System.out.println("BLACKJACK!");
				if(player.getPersonalDeck().getCardsValue()==21){
					System.out.println("Player wins!");
					player.setPersonalMoney(player.getPersonalMoney()+bet*2);
					player.addWon();
				}
				else if(dealer.getPersonalDeck().getCardsValue()==21){
					System.out.println("Dealer wins!");
					player.setPersonalMoney(player.getPersonalMoney()-bet*2);
					player.addLoses();
				}
				else {
					System.out.println("Push!");
					player.addTied();
				}
				prepareRound();
				endFlag = true;
			}
			else
				endFlag = false;
			while (!endFlag){
				System.out.print("Ur hand: ");
				System.out.println(player.getPersonalDeck());
				System.out.println("Dealer's deck: "+dealer.getPersonalDeck().getCard(0)+ " [Hidden]");
				System.out.println("Ur deck is valued at: "+player.getPersonalDeck().getCardsValue());
				do{
					System.out.println("Press (1) to hit or (2) to stand");
					while (!sc.hasNextInt()) {
						System.out.println("That's not a number!\nTry again");
						sc.next();
					}
					choice = sc.nextInt();
				}while(choice!=1 && choice!=2);
				switch(choice){
					case 1:
						do {
							player.getPersonalDeck().draw(playingDeck);
							System.out.println("U draw: " + player.getPersonalDeck().getCard(player.getPersonalDeck().getSize() - 1));
							if (player.getPersonalDeck().getCardsValue() > 21) {
								System.out.println("U bust! ");
								player.setPersonalMoney(player.getPersonalMoney() - bet);
								player.addLoses();
								endFlag = true;
								break;
							}
							System.out.println("Ur deck is valued at: " + player.getPersonalDeck().getCardsValue());
							System.out.println("Press (1) to hit or (2) to stand");
							choice = sc.nextInt();
						}while(choice!=2);
						break;
					case 2:
						break;
				}
				while(dealer.getPersonalDeck().getCardsValue()<=16 && !endFlag){
					dealer.getPersonalDeck().draw(playingDeck);
					System.out.println("Dealer draws: " +dealer.getPersonalDeck().getCard(dealer.getPersonalDeck().getSize()-1));
					if(dealer.getPersonalDeck().getCardsValue()>21){
						System.out.println("Dealer busts!");
						System.out.println("Dealer's deck: "+dealer.getPersonalDeck()+ "\nValue: "+dealer.getPersonalDeck().getCardsValue());
						player.setPersonalMoney(player.getPersonalMoney()+bet);
						player.addWon();
						endFlag = true;
					}
				}
				if(!endFlag){
					switch (whoWin()) {
						case -1 -> {
							System.out.println("Dealer wins!");
							System.out.println("Dealer's deck: "+dealer.getPersonalDeck()+ "\nValue: "+dealer.getPersonalDeck().getCardsValue());
							player.setPersonalMoney(player.getPersonalMoney() - bet);
							player.addLoses();
						}
						case 1 -> {
							System.out.println("Player wins!");
							System.out.println("Dealer's deck: "+dealer.getPersonalDeck()+ "\nValue: "+dealer.getPersonalDeck().getCardsValue());
							player.setPersonalMoney(player.getPersonalMoney() + bet);
							player.addWon();
						}
						default -> {
							System.out.println("Push!");
							player.addTied();

						}
					}
					endFlag = true;
				}
				prepareRound();
				System.out.println("END OF ROUND!");
			}
		}
		System.out.println("Rounds: \nwon: "+player.getWon()+" lost: "+player.getLoses()+" tied: "+player.getTied());
	}
}
