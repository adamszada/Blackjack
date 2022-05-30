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
		while(player.getPersonalMoney()>0){
			System.out.println("U've "+player.getPersonalMoney()+"PLN how much would u like to bet?");
			double bet = sc.nextDouble();
			player.getPersonalDeck().draw(playingDeck);
			player.getPersonalDeck().draw(playingDeck);
			dealer.getPersonalDeck().draw(playingDeck);
			dealer.getPersonalDeck().draw(playingDeck);
			if(dealer.getPersonalDeck().getCardsValue()==21 || player.getPersonalDeck().getCardsValue()==21){
				System.out.print("Ur hand: ");
				System.out.println(player.getPersonalDeck());
				System.out.println("Dealer's deck: "+dealer.getPersonalDeck());
				System.out.println("BLACKJACK!");
				if(player.getPersonalDeck().getCardsValue()==21){
					System.out.println("Player wins!");
					player.setPersonalMoney(player.getPersonalMoney()+bet*2);
				}
				else if(dealer.getPersonalDeck().getCardsValue()==21){
					System.out.println("Dealer wins!");
					player.setPersonalMoney(player.getPersonalMoney()-bet*2);
				}
				else
					System.out.println("Push!");
				prepareRound();
				endFlag = true;
			}
			else
				endFlag = false;
			while (!endFlag){
				System.out.print("Ur hand: ");
				System.out.println(player.getPersonalDeck());
				System.out.println("Dealer's deck: "+dealer.getPersonalDeck());
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
							endFlag = true;
						}
						break;
					case 2:
						break;
				}
				while(dealer.getPersonalDeck().getCardsValue()<=16 && !endFlag){
					dealer.getPersonalDeck().draw(playingDeck);
					System.out.println("Dealer draws: " +dealer.getPersonalDeck().getCard(dealer.getPersonalDeck().getSize()-1));
					if(dealer.getPersonalDeck().getCardsValue()>21){
						System.out.println("Dealer busts!");
						player.setPersonalMoney(player.getPersonalMoney()+bet);
						endFlag = true;
					}
				}
				if(!endFlag){
					switch (whoWin()){
						case -1:
							System.out.println("Dealer wins!");
							player.setPersonalMoney(player.getPersonalMoney()-bet);
							break;
						case 1:
							System.out.println("Player wins!");
							player.setPersonalMoney(player.getPersonalMoney()+bet);
							break;
						default:
							System.out.println("Push!");
							break;
					}
					endFlag = true;
				}
				prepareRound();
				System.out.println("END OF ROUND!");
			}
		}
	}
}
