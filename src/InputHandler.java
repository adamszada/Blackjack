import java.util.Scanner;

public interface InputHandler {
	public Scanner sc = new Scanner(System.in);

	static double getBet(double limit){
		double bet = 0;
		do{
			while (!sc.hasNextDouble()) {
				System.out.println(OutputHandler.getMessage(OutputHandler.Type.BET));
				sc.next();
			}
			bet = sc.nextDouble();
		}while (bet<=0 || bet>limit);
		return bet;
	}

	static int getChoice(int min, int max){
		int choice = 0;
		do{
			while (!sc.hasNextInt()) {
				System.out.println(OutputHandler.Type.NUMBER_ERROR);
				sc.next();
			}
			choice = sc.nextInt();
		}while(choice>max || min<0);
		return choice;
	}
}
