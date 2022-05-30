import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Press (1) to run Single Player");
		int choose = sc.nextInt();
		if(choose==1){
			new SinglePlayer();
		}

	}
}
