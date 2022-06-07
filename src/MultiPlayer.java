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
	private final List<Integer> bets;
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
			tmpValues.add(players.get(i).getPersonalDeck().getCardsValue());
			if(tmpValues.get(i)>BLACKJACK_VALUE)
				tmpValues.set(i,-1);
		}
		return tmpValues.indexOf(Collections.max(tmpValues));
	}

	public void handleBets(){
		for (int i = 0; i < noPlayers; i++){
			try {
				sendMessageLocal(i,usernames.get(i)+' '+OutputHandler.getMessage(OutputHandler.Type.BET)+players.get(i).getPersonalMoney());
				bets.add(i,Integer.parseInt(readers.get(i).readLine()));
			}catch (IOException e){
				System.out.println("Something went wrong!");
				System.exit(2);
			}
		}
	}


	public void handleMove(){
		int tmp;
		for (int i = 0; i < noPlayers; i++){
			try {
				if(players.get(i).getPersonalDeck().getCardsValue() >= BLACKJACK_VALUE)
					break;
				sendMessageLocal(i,OutputHandler.getMessage(OutputHandler.Type.MOVE_CHOICE));
				do {
					tmp = Integer.parseInt(readers.get(i).readLine());
					if(tmp==1){
						drawCard(players.get(i), 1);
						sendMessageLocal(i,OutputHandler.getMessage(OutputHandler.Type.DRAW_CARD) + players.get(i).getPersonalDeck().getCard(players.get(i).getPersonalDeck().getSize()-1) +'#'+OutputHandler.getMessage(OutputHandler.Type.DECK_VALUE) + players.get(i).getPersonalDeck().getCardsValue());
					}
				}while(tmp!=2);
			}catch (IOException e){
				System.out.println("Something went wrong!");
				System.exit(2);
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
			playingDeck.createFullDeck();
			playingDeck.shuffle();
			handleBets();
			for (int i = 0; i < noPlayers; i++)
				drawCard(players.get(i), 2);
			for(int i=0;i<noPlayers;i++){
				for(int j=0;j<noPlayers;j++)
					if(j!=i)
						playersCards+=String.valueOf(players.get(j).getPersonalDeck().getCard(0))+' '+"[Hidden] #";
				sendMessageLocal(i, "Your deck:"+players.get(i).getPersonalDeck().toStringMultiplayer()+" #Valued at: "+players.get(i).getPersonalDeck().getCardsValue()+playersCards);
				playersCards = "#Remainder have:#";
			}
			handleMove();
			tmp = whoWin();
			try{
				for(int i=0;i<noPlayers;i++){
					if(i!=tmp) {
						sendMessageLocal(i,"You lost the round! #Press any key to continue");
						players.get(i).setPersonalMoney(players.get(i).getPersonalMoney()-bets.get(i));
					}
					else {
						sendMessageLocal(i,"You won the round! #Press any key to continue");
						players.get(i).setPersonalMoney(players.get(i).getPersonalMoney() + bets.get(i));
					}
					readers.get(i).readLine();
				}
			}catch (IOException e){
				System.out.println("Something went wrong!");
				System.exit(2);
			}
			playingDeck.clearDeck();
			for(int i=0;i<noPlayers;i++){
				players.get(i).getPersonalDeck().clearDeck();
				bets.set(i,0);
			}
		}
	}
}
