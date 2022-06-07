public class Person {
	public static final int START_MONEY = 100;
	private final Deck personalDeck;
	private double personalMoney;

	Person(){
		this.personalDeck = new Deck();
		this.personalMoney = START_MONEY;
	}

	public double getPersonalMoney() {
		return personalMoney;
	}

	public Deck getPersonalDeck() {
		return personalDeck;
	}

	public void setPersonalMoney(double money){
		this.personalMoney = money;
	}

}
