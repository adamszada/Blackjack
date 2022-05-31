import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class MultiPlayer implements Runnable {

	private ArrayList<Socket> sockets;
	private ArrayList<BufferedReader> readers;
	private ArrayList<BufferedWriter> writers;
	private ArrayList<String> usernames;
	private ArrayList<Boolean> isPlaying;
	private ArrayList<Person> players;
	private ArrayList<Integer> bets;
	private String playersCards;
	private int noPlayers;
	private final Deck playingDeck;
	private ArrayList<Boolean> isReady;



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


	@Override
	public void run() {
		int tmp;
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

		}
	}
}
