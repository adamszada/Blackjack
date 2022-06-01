import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MultiPlayer implements Runnable {

	private final ArrayList<Socket> sockets;
	private final ArrayList<BufferedReader> readers;
	private final ArrayList<BufferedWriter> writers;
	private final ArrayList<String> usernames;
	private ArrayList<Boolean> isPlaying;
	private final ArrayList<Person> players;
	private ArrayList<Integer> bets;
	private String playersCards;
	private int noPlayers;
	private final Deck playingDeck;
	private final ArrayList<Boolean> isReady;



	public MultiPlayer(ArrayList<Socket> sockets, int noPlayers){
		playingDeck = new Deck();
		this.sockets = sockets;
		this.noPlayers = noPlayers;
		this.readers = new ArrayList<BufferedReader>();
		this.writers = new ArrayList<BufferedWriter>();
		this.usernames = new ArrayList<String>();
		this.isPlaying = new ArrayList<Boolean>();
		this.players = new ArrayList<Person>();
		this.bets = new ArrayList<Integer>();
		this.playersCards = "#Remainder have:#";
		this.isReady = new ArrayList<Boolean>();
		try {
			for (int i = 0; i < noPlayers; i++) {
				this.readers.add(new BufferedReader(new InputStreamReader(sockets.get(i).getInputStream())));
				this.writers.add(new BufferedWriter(new OutputStreamWriter(sockets.get(i).getOutputStream())));
				this.isPlaying.add(true);
				this.players.add(new Person());
				this.isReady.add(false);
			}
		}catch (IOException e){
			System.out.println("Something went wrong!");

			System.exit(1);
		}
	}


	public int whoWin(){
		ArrayList<Integer> tmpValues = new ArrayList<Integer>();
		for(int i=0;i<noPlayers;i++){
			tmpValues.add(players.get(i).getPersonalDeck().getCardsValue());
			if(tmpValues.get(i)>21)
				tmpValues.set(i,-1);
		}
		return  tmpValues.indexOf(Collections.max(tmpValues));
	}

	@Override
	public void run() {
		for(int i=0;i<noPlayers;i++){
			if(sockets.get(i).isConnected()){
				try {
					writers.get(i).write(String.valueOf(i));
					writers.get(i).newLine();
					writers.get(i).flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		for (int i=0;i<noPlayers;i++){
			if(sockets.get(i).isConnected()){
				try {
					usernames.add(readers.get(i).readLine());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		int turn = 0;
		int tmp = 0;
		while(true){
			playingDeck.createFullDeck();
			playingDeck.shuffle();
			if(turn==0){
				for (int i = 0; i < noPlayers; i++) {
					try {
						writers.get(i).write(usernames.get(i)+' '+"How much would u like to bet? #"+"U have: "+players.get(i).getPersonalMoney()+"PLN");
						writers.get(i).newLine();
						writers.get(i).flush();
						bets.add(i,Integer.parseInt(readers.get(i).readLine()));
					}catch (IOException e){
						System.out.println("Something went wrong!");
						System.exit(2);
					}
				}
				turn+=1;
			}
			if(turn==1){
				for (int i = 0; i < noPlayers; i++) {
					players.get(i).getPersonalDeck().draw(playingDeck);
					playersCards+=players.get(i).getPersonalDeck().toStringMultiplayer()+' '+"[Hidden] #";
					players.get(i).getPersonalDeck().draw(playingDeck);
					//System.out.println(players.get(i).getPersonalDeck());
				}
				turn+=1;
			}
			if(turn==2){
				for (int i = 0; i < noPlayers; i++) {
					try {
						writers.get(i).write(usernames.get(i)+' '+"Your deck:"+players.get(i).getPersonalDeck().toStringMultiplayer()+" #Valued at: "+players.get(i).getPersonalDeck().getCardsValue()+playersCards+" #Press (1) to hit or (2) to stand");
						writers.get(i).newLine();
						writers.get(i).flush();
					}catch (IOException e){
						System.out.println("Something went wrong!");
						System.exit(2);
					}
				}
				turn+=1;
			}
			if(turn==3){
				for (int i = 0; i < noPlayers; i++) {
					try {
						tmp = Integer.parseInt(readers.get(i).readLine());
						if(tmp==2)
							isReady.set(i,true);
					}catch (IOException e){
						System.out.println("Something went wrong!");
						System.exit(2);
					}
				}
				turn+=1;
			}
			if(turn==4){
				for (int i = 0; i < noPlayers; i++) {
					try {
						if(!isReady.get(i)) {
							do {
								players.get(i).getPersonalDeck().draw(playingDeck);
								writers.get(i).write(usernames.get(i) + ' ' + "Your deck:" + players.get(i).getPersonalDeck().toStringMultiplayer() + " #Valued at: " + players.get(i).getPersonalDeck().getCardsValue() + " #Press (1) to hit or (2) to stand");
								writers.get(i).newLine();
								writers.get(i).flush();
								tmp = Integer.parseInt(readers.get(i).readLine());
							}while(tmp!=2);
							isReady.set(i,true);
						}

					}catch (IOException e){
						System.out.println("Something went wrong!");
						System.exit(2);
					}
				}
				turn+=1;
			}
			if(turn==5) {
				tmp = whoWin();
				try{
					for(int i=0;i<noPlayers;i++){
						if(i!=tmp) {
							writers.get(i).write(usernames.get(i) + ' ' + "You lost the round! #Press any key to continue");
							players.get(i).setPersonalMoney(players.get(i).getPersonalMoney()-bets.get(i));
						}
						else {
							writers.get(i).write(usernames.get(i) + ' ' + "You won the round! #Press any key to continue");
							players.get(i).setPersonalMoney(players.get(i).getPersonalMoney() + bets.get(i));
						}
						writers.get(i).newLine();
						writers.get(i).flush();
						readers.get(i).readLine();
					}
				}catch (IOException e){
					System.out.println("Something went wrong!");
					System.exit(2);
				}
			}
			turn = 0;
			playingDeck.clearDeck();
			for(int i=0;i<noPlayers;i++){
				players.get(i).getPersonalDeck().clearDeck();
				bets.set(i,0);
				isReady.set(i,false);
			}
			playersCards = "#Remainder have:#";
		}
	}
}
