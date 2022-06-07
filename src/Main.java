import java.io.IOException;

public class Main implements OutputHandler, InputHandler {

	public static void main(String[] args) throws IOException {
		System.out.println(OutputHandler.getMessage(Type.MODE_CHOICE));
		int choice = InputHandler.getChoice(1,2);
		String[] tmp = new String[1];
		switch (choice) {
			case 1 -> new SinglePlayer();
			case 2 -> {
				System.out.println(OutputHandler.getMessage(Type.NO_PLAYERS));
				tmp[0] = String.valueOf(InputHandler.getChoice(1,8));
				Server.main(tmp);
			}
		}
	}
}
