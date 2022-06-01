public class Person {
	private final Deck personalDeck;
	private double personalMoney;
	private int won;
	private int loses;
	private int tied;

	Person(){
		this.personalDeck = new Deck();
		this.personalMoney = 100;
		this.won = 0;
		this.loses = 0;
		this.tied = 0;
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

	public void clearDeck(){
		this.personalDeck.clearDeck();
	}

	public void addWon(){
		this.won+=1;
	}
	public void addLoses(){
		this.loses+=1;
	}
	public void addTied(){
		this.tied+=1;
	}

	public int getWon() {
		return won;
	}

	public int getLoses() {
		return loses;
	}

	public int getTied() {
		return tied;
	}
}
