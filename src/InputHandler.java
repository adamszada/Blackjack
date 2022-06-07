import java.util.Scanner;

public interface InputHandler {
	Scanner sc = new Scanner(System.in);

	static double getBet(double limit){
		double bet;
		do{
			while (!sc.hasNextDouble()) {
				System.out.println("That's not a number!\nTry again");
				sc.next();
			}
			bet = sc.nextDouble();
		}while (bet<=0 || bet>limit);
		return bet;
	}

	static int getChoice(int min, int max){
		int choice;
		do{
			while (!sc.hasNextInt()) {
				System.out.println("That's not a number!\nTry again");
				sc.next();
			}
			choice = sc.nextInt();
		}while(choice>max || min<0);
		return choice;
	}
}
