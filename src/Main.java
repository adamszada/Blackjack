import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static boolean isCorrectChoice(int choice){
		return choice == 1 || choice == 2;
	}


	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Press (1) to run Single Player\nPress (2) to play with friends");
		int choice = 0;
		try{
			choice = Integer.parseInt(sc.nextLine());
			if(!isCorrectChoice(choice))
				throw new WrongChoiceException("");
		}catch (NumberFormatException | WrongChoiceException e ) {
			System.out.println("Incorrect Input Data");
			main(null);
		}
		String[] tmp = new String[1];
		switch (choice) {
			case 1 -> new SinglePlayer();
			case 2 -> {
				do{
					System.out.println("Enter the amount of players (max 4):");
					while (!sc.hasNextInt()) {
						System.out.println("That's not a proper number!\nTry again");
						sc.next();
					}
					tmp[0] = String.valueOf(sc.nextInt());
				}while (Integer.parseInt(tmp[0])<=0 || Integer.parseInt(tmp[0])>4);
				Server.main(tmp);
			}
		}
	}
}
