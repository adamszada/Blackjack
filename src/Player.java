import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Player implements Runnable{

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String username;
    private boolean myTurn = false;

    public void sendNotification(){
        Scanner scanner = new Scanner(System.in);
        new Thread(() -> {
            while(socket.isConnected()) {
                if (!myTurn) {
                    String move;
                    move = scanner.nextLine();
                    try {
                        writer.write(move);
                        writer.newLine();
                        writer.flush();
                    } catch (IOException e) {
                        closeEverything(socket, reader, writer);
                    }
                    myTurn = false;
                }
            }
        }).start();
    }

    public void listenNotification(){
        new Thread(() -> {
            String notificationFromServer;
            String[] splited;
            while(socket.isConnected()) {
                try {
                    notificationFromServer = reader.readLine();
                    if(notificationFromServer.split(" ")[0].equals(username)) {
                        splited = notificationFromServer.split("#");
                        for(String s : splited)
                            System.out.println(s);
                        //System.out.println(notificationFromServer);
                        myTurn = true;
                    }
                } catch (IOException e) {
                    closeEverything(socket, reader, writer);
                    System.out.println("Problem with connection to the server");
                    System.exit(0);
                }
            }
        }).start();
    }


    public void closeEverything(Socket socket, BufferedReader reader, BufferedWriter writer) {
        try {
            if(reader != null)
                reader.close();
            if(writer != null)
                writer.close();
            if(socket != null)
                socket.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Joining the game...");
        try {
            socket = new Socket("localhost", 666);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch (IOException e){
            System.out.println("Server didnt respond!");
            closeEverything(socket,reader,writer);
            System.exit(1);
        }
        try {
            reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Enter your username:");
        Scanner sc = new Scanner(System.in);
        username = sc.nextLine();
        try {
            writer.write(username);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendNotification();
        listenNotification();
    }

    public static void main(String[] args) {
        Player Player = new Player();
        Player.run();
    }
}
