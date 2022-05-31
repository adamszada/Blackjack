import java.util.Scanner;

public class Main {

	public static boolean isCorrectChoice(int choice){
		return choice == 1 || choice == 2;
	}


	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Press (1) to run Single Player");
		int choice = 0;
		try{
			choice = Integer.parseInt(sc.nextLine());
			if(!isCorrectChoice(choice))
				throw new WrongChoiceException("");
		}catch (NumberFormatException | WrongChoiceException e ) {
			System.out.println("Incorrect Input Data");
			main(null);
		}
		switch (choice) {
			case 1 -> new SinglePlayer();
			case 2 -> System.out.println("...");
		}
	}
}
