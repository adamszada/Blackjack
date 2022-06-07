import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiPlayer implements Runnable {

	public static final int BLACKJACK_VALUE = 21;
	private final List<Socket> sockets;
	private final List<BufferedReader> readers;
	private final List<BufferedWriter> writers;
	private final List<String> usernames;
	private final List<Person> players;
	private final List<Double> bets;
	private String playersCards;
	private final int noPlayers;
	private final Deck playingDeck;


	public MultiPlayer(ArrayList<Socket> sockets, int noPlayers){
		playingDeck = new Deck();
		this.sockets = sockets;
		this.noPlayers = noPlayers;
		this.readers = new ArrayList<>();
		this.writers = new ArrayList<>();
		this.usernames = new ArrayList<>();
		this.players = new ArrayList<>();
		this.bets = new ArrayList<>();
		this.playersCards = "#Remainder have:#";
		try {
			for (int i = 0; i < noPlayers; i++) {
				this.readers.add(new BufferedReader(new InputStreamReader(sockets.get(i).getInputStream())));
				this.writers.add(new BufferedWriter(new OutputStreamWriter(sockets.get(i).getOutputStream())));
				this.players.add(new Person());
			}
		}catch (IOException e){
			System.out.println("Something went wrong!");
			System.exit(1);
		}
	}

	public int whoWin(){
		ArrayList<Integer> tmpValues = new ArrayList<>();
		for(int i=0;i<noPlayers;i++){
			if(sockets.get(i).isConnected()) {
				tmpValues.add(players.get(i).getPersonalDeck().getCardsValue());
				if (tmpValues.get(i) > BLACKJACK_VALUE)
					tmpValues.set(i, -1);
			}
		}
		return tmpValues.indexOf(Collections.max(tmpValues));
	}

	public boolean validateBet(String input,double max){
		if(input.matches("[0-9]+"))
			return Double.parseDouble(input) <= max && Double.parseDouble(input)>0;
		return false;
	}

	public void handleBets(){
		String tmp;
		for (int i = 0; i < noPlayers; i++){
			if(sockets.get(i).isConnected()) {
				try {
					do {
						sendMessageLocal(i, usernames.get(i) + ' ' + OutputHandler.getMessage(OutputHandler.Type.BET) + players.get(i).getPersonalMoney());
						tmp = readers.get(i).readLine();
					} while (!validateBet(tmp, players.get(i).getPersonalMoney()));
					bets.add(i, Double.parseDouble(tmp));
				} catch (IOException e) {
					System.out.println("Something went wrong!");
					System.exit(2);
				}
			}
		}
	}

	public boolean validateMove(String input){
		if(input.matches("[1-2]+"))
			return Integer.parseInt(input) <= 2 && Integer.parseInt(input)>=1;
		return false;
	}

	public void handleMove(){
		int tmp;
		String tmpInput;
		for (int i = 0; i < noPlayers; i++){
			if(sockets.get(i).isConnected()) {
				try {
					if (players.get(i).getPersonalDeck().getCardsValue() >= BLACKJACK_VALUE)
						break;
					sendMessageLocal(i, OutputHandler.getMessage(OutputHandler.Type.MOVE_CHOICE));
					do {
						do {
							tmpInput = readers.get(i).readLine();
						} while (!validateMove(tmpInput));
						tmp = Integer.parseInt(tmpInput);
						if (tmp == 1) {
							drawCard(players.get(i), 1);
							sendMessageLocal(i, OutputHandler.getMessage(OutputHandler.Type.DRAW_CARD) + players.get(i).getPersonalDeck().getCard(players.get(i).getPersonalDeck().getSize() - 1) + '#' + OutputHandler.getMessage(OutputHandler.Type.DECK_VALUE) + players.get(i).getPersonalDeck().getCardsValue());
						}
					} while (tmp != 2);
				} catch (IOException e) {
					System.out.println("Something went wrong!");
					System.exit(2);
				}
			}
		}
	}


	public void sendMessageLocal(int id, String message){
		try {
			writers.get(id).write(usernames.get(id)+' '+message);
			writers.get(id).newLine();
			writers.get(id).flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void drawCard(Person Person, int amount){
		for(int i=0;i<amount;i++)
			Person.getPersonalDeck().draw(playingDeck);
	}


	@Override
	public void run() {
		int tmp;
		for(int i=0;i<noPlayers;i++){
			if(sockets.get(i).isConnected()){
				try {
					writers.get(i).write(String.valueOf(i));
					writers.get(i).newLine();
					writers.get(i).flush();
					usernames.add(readers.get(i).readLine());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		while(true) {
			for(int i=0;i<noPlayers;i++){
				if(players.get(i).getPersonalMoney()==0){
					for (int j=0;j<noPlayers;j++)
						sendMessageLocal(j,"End of the game!");
					System.exit(1);
				}
			}
			playingDeck.createFullDeck();
			playingDeck.shuffle();
			handleBets();
			for (int i = 0; i < noPlayers; i++) {
				if(sockets.get(i).isConnected())
					drawCard(players.get(i), 2);
			}
			for(int i=0;i<noPlayers;i++){
				if(sockets.get(i).isConnected()) {
					for (int j = 0; j < noPlayers; j++)
						if (j != i)
							playersCards += String.valueOf(players.get(j).getPersonalDeck().getCard(0)) + ' ' + "[Hidden] #";
					sendMessageLocal(i, "Your deck:" + players.get(i).getPersonalDeck().toStringMultiplayer() + " #Valued at: " + players.get(i).getPersonalDeck().getCardsValue() + playersCards);
					playersCards = "#Remainder have:#";
				}
			}
			handleMove();
			tmp = whoWin();
			try{
				for (int i = 0; i < noPlayers; i++){
					if(sockets.get(i).isConnected()) {
						if (i != tmp) {
							sendMessageLocal(i, "You lost the round! #Press any key to continue");
							players.get(i).setPersonalMoney(players.get(i).getPersonalMoney() - bets.get(i));
						} else {
							sendMessageLocal(i, "You won the round! #Press any key to continue");
							players.get(i).setPersonalMoney(players.get(i).getPersonalMoney() + bets.get(i));
						}
						readers.get(i).readLine();
					}
				}
			}catch (IOException e){
				System.out.println("Something went wrong!");
				System.exit(2);
			}
			playingDeck.clearDeck();
			for (int i = 0; i < noPlayers; i++){
				if(sockets.get(i).isConnected()) {
					players.get(i).getPersonalDeck().clearDeck();
					bets.set(i, 0.0);
				}
			}
		}
	}
}
