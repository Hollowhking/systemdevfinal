import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;

public class Server {
    public static String currentplayer = "X";
    public static String[] board = new String[9];
    public static String orderplayer = "X";
    static class Servers extends Thread{
        ServerSocket server = null;
        public void run(){
            try {
                clearboard();
                // server is listening on port 1234
                server = new ServerSocket(1234);
                server.setReuseAddress(true);

                // running infinite loop for getting
                // client request
                while (true) {
                    // socket object to receive incoming client
                    // requests
                    Socket client = server.accept();

                    // Displaying that new client is connected
                    // to server
                    System.out.println("New client connected" + client.getInetAddress().getHostAddress());

                    // create a new thread object
                    ClientHandler clientSock = new ClientHandler(client);
                    if (currentplayer == "X"){currentplayer = "O";}
                    else {currentplayer = "X";}
                    // This thread will handle the client
                    // separately
                    new Thread(clientSock).start();

                }
            }
            catch (IOException e) {e.printStackTrace();}
            finally {
                if (server != null) {
                    try {server.close();}
                    catch (IOException e) {e.printStackTrace();}
                }
            }
        }
        private class ClientHandler implements Runnable {
            private final Socket clientSocket;
            boolean newtext = true;
            // Constructor
            public ClientHandler(Socket socket) {this.clientSocket = socket;}
            public void run() {
                PrintWriter out = null;
                BufferedReader in = null;
                try {
                    // get the outputstream of client
                    out = new PrintWriter(clientSocket.getOutputStream(), true);

                    // get the inputstream of client
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                    String line[]=new String[2];
                    out.println(currentplayer);
                    //out.println(sendboard());
                    while (newtext) {//interupt all input from player whose turn it is not

                        line = in.readLine().split(" ");
                        // writing the received message from
                        if (line != null) {
                            if (Objects.equals(line[0], "A")){
                                out.println(sendboard());
                            }
                            else if (Objects.equals(line[0],"B")){
                                out.println(orderplayer);
                            }
                            else {
                                System.out.printf(" Sent from the client: %s\n", line[1]);
                                board[Integer.parseInt(line[1])] = line[0];
                                currentplayer = line[0];
                                printboard();
                                out.println(sendboard());
                                if (Objects.equals(line[0], "X")){orderplayer = "O";}
                                else if (Objects.equals(line[0], "O")){orderplayer = "X";}
                                System.out.println("Current Turn: "+orderplayer);
                            }
                        }
                    }
                }catch (IOException e) {e.printStackTrace();}
                finally {
                    try {
                        if (out != null) {
                            System.out.println("Ending Client");
                            out.close();
                        }
                        if (in != null) {
                            in.close();
                            clientSocket.close();
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            public void stop(){
                //System.out.println("Stopping");
                newtext = false;
            }
        }
        public void printboard(){
            System.out.println(board[0]+" "+board[1]+" "+board[2]);
            System.out.println(board[3]+" "+board[4]+" "+board[5]);
            System.out.println(board[6]+" "+board[7]+" "+board[8]);
        }
        public void clearboard(){
            for (int i=0;i<9;i++){board[i]="_";}
        }
        public String sendboard(){
            String boardstring = board[0]+" "+board[1]+" "+board[2]+" "+board[3]+" "+board[4]+" "+board[5]+" "+board[6]+" "+board[7]+" "+board[8];
            return boardstring;
        }
    }

    public static void main(String[] args){
        Servers serverthread = new Servers();
        serverthread.start();
    }
}
