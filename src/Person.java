public class Person {
	public static final int START_MONEY = 100;
	public static final int START_VALUE = 0;
	private final Deck personalDeck;
	private double personalMoney;
	private int won;
	private int loses;
	private int tied;

	Person(){
		this.personalDeck = new Deck();
		this.personalMoney = START_MONEY;
		this.won = START_VALUE;
		this.loses = START_VALUE;
		this.tied = START_VALUE;
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

	public void addWon(){
		this.won+=1;
	}

	public void addLoses(){
		this.loses+=1;
	}

	public void addTied(){
		this.tied+=1;
	}

	public int getWon() { return won;}

	public int getLoses() { return loses;}

	public int getTied() { return tied;}

}
