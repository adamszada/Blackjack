public class Person {
	private final Deck personalDeck;
	private double personalMoney;

	Person(){
		this.personalDeck = new Deck();
		this.personalMoney = 100;
	}

	public double getPersonalMoney() {
		return personalMoney;
	}

	public Deck getPersonalDeck() {
		return personalDeck;
	}

	public void setPersonalMoney(double money) {
		this.personalMoney = money;
	}
}
