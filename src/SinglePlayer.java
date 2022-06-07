public class SinglePlayer implements OutputHandler, InputHandler {

	public static final int BLACKJACK_VALUE = 21;
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

	public boolean isBlackjack(){
		return dealer.getPersonalDeck().getCardsValue() == BLACKJACK_VALUE || player.getPersonalDeck().getCardsValue() == BLACKJACK_VALUE;
	}


	public void drawCard(Person Person, int amount){
		for(int i=0;i<amount;i++)
			Person.getPersonalDeck().draw(playingDeck);
	}

	public double whoWin(double bet){
		if(player.getPersonalDeck().getCardsValue()<BLACKJACK_VALUE && (dealer.getPersonalDeck().getCardsValue()>BLACKJACK_VALUE || dealer.getPersonalDeck().getCardsValue() < player.getPersonalDeck().getCardsValue()))
			return bet;
		else if(dealer.getPersonalDeck().getCardsValue() == player.getPersonalDeck().getCardsValue())
			return 0;
		else
			return bet*-1;
	}

	public void runRound(){
		int choice;
		double bet;
		while(player.getPersonalMoney()>0){
			System.out.println(OutputHandler.getMessage(Type.BET)+player.getPersonalMoney());
			bet = InputHandler.getBet(player.getPersonalMoney());
			drawCard(player,2);
			drawCard(dealer,2);
			if(isBlackjack()){
				System.out.println("Blackjack");
				bet*=2;
				endFlag = true;
			}
			else
				endFlag = false;
			if(!endFlag){
				System.out.println("Your hand:" +player.getPersonalDeck());
				System.out.println("Dealer's deck: "+dealer.getPersonalDeck().getCard(0)+ " and" + " [Hidden]");
				System.out.println(OutputHandler.getMessage(Type.DECK_VALUE) + player.getPersonalDeck().getCardsValue());
				System.out.println(OutputHandler.getMessage(OutputHandler.Type.MOVE_CHOICE));
				choice = InputHandler.getChoice(1,2);
				switch(choice){
					case 1:
						do {
							drawCard(player,1);
							System.out.println(OutputHandler.getMessage(Type.DRAW_CARD)+ player.getPersonalDeck().getCard(player.getPersonalDeck().getSize() - 1));
							if (player.getPersonalDeck().getCardsValue() > BLACKJACK_VALUE) {
								endFlag = true;
								break;
							}
							System.out.println(OutputHandler.getMessage(Type.DECK_VALUE) + player.getPersonalDeck().getCardsValue());
							System.out.println(OutputHandler.getMessage(Type.MOVE_CHOICE));
							choice = InputHandler.getChoice(1,2);
						}while(choice!=2);
						break;
					case 2:
						break;
				}
				while(dealer.getPersonalDeck().getCardsValue()<=16 && !endFlag){
					drawCard(dealer,1);
					System.out.println(OutputHandler.getMessage(Type.DRAW_CARD) +dealer.getPersonalDeck().getCard(dealer.getPersonalDeck().getSize()-1));
				}
				endFlag = true;
			}
			bet = whoWin(bet);
			if(bet==0){
				System.out.println("Push!");
			} else {
				if(bet>0)
					System.out.println("You win!");
				else
					System.out.println("You lose!");
				player.setPersonalMoney(player.getPersonalMoney()+bet);
			}
			System.out.println("Dealer's deck: "+dealer.getPersonalDeck()+ "\n"+OutputHandler.getMessage(Type.DECK_VALUE)+dealer.getPersonalDeck().getCardsValue());
			prepareRound();
			System.out.println("END OF ROUND!");
		}
	}
}
