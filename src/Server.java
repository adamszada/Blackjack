import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private final ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer(int slots) {
        System.out.println("Initialized server at port " +
                serverSocket.getLocalPort() +
                "waiting for connections...");
        try {
            ArrayList<Socket> tmpSockets = new ArrayList<>();
            while(!serverSocket.isClosed()) {
                for(int i=0;i<slots;i++) {
                    Socket socket = serverSocket.accept();
                    System.out.println(i+" ID");
                    tmpSockets.add(socket);

                }
                MultiPlayer game = new MultiPlayer(tmpSockets,slots);
                Thread gameThread = new Thread(game);
                gameThread.start();
            }
        } catch(IOException e) {
            e.printStackTrace();
            closeServerSocket();
        }
    }

    public void closeServerSocket() {
        try {
            if(serverSocket != null)
                serverSocket.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(666);
        Server server = new Server(serverSocket);
        server.startServer(Integer.parseInt(args[0]));
    }
}
