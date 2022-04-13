package com.example.tiktaktoeclient;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class HelloApplication extends Application {
    Button pos1 = new Button("1");
    Button pos2 = new Button("2");
    Button pos3 = new Button("3");
    Button pos4 = new Button("4");
    Button pos5 = new Button("5");
    Button pos6 = new Button("6");
    Button pos7 = new Button("7");
    Button pos8 = new Button("8");
    Button pos9 = new Button("9");

    Label WinLabel = new Label();
    PrintWriter out = null;
    BufferedReader in = null;
    Label player = new Label();
    Label message = new Label();
    String line = "";
    Stage stage;
    Scene winscene = new Scene(WinLabel,500,500);
    Timer timer = new Timer();
    volatile String[] currentboard = new String[9];
    private String turnplayer="X";
    private String currentplayer = "X";
    public void init() throws IOException {
        Socket socket = new Socket("localhost", 1234);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        currentplayer = in.readLine();
        currentboard[0] = "_";
        currentboard[1] = "_";
        currentboard[2] = "_";
        currentboard[3] = "_";
        currentboard[4] = "_";
        currentboard[5] = "_";
        currentboard[6] = "_";
        currentboard[7] = "_";
        currentboard[8] = "_";
    }

    @Override
    public void start(Stage thisstage) throws IOException {
        //init();
        this.stage = thisstage;
        player.setText("You are player: "+currentplayer);
        GridPane gamegrid = new GridPane();
        pos1.setMinSize(50,50);
        pos2.setMinSize(50,50);
        pos3.setMinSize(50,50);
        pos4.setMinSize(50,50);
        pos5.setMinSize(50,50);
        pos6.setMinSize(50,50);
        pos7.setMinSize(50,50);
        pos8.setMinSize(50,50);
        pos9.setMinSize(50,50);

        gamegrid.add(pos1,0,0);
        gamegrid.add(pos2,1,0);
        gamegrid.add(pos3,2,0);
        gamegrid.add(pos4,0,1);
        gamegrid.add(pos5,1,1);
        gamegrid.add(pos6,2,1);
        gamegrid.add(pos7,0,2);
        gamegrid.add(pos8,1,2);
        gamegrid.add(pos9,2,2);
        GridPane clientgroup = new GridPane();
        clientgroup.add(player,0,0);
        clientgroup.add(gamegrid,0,1);
        clientgroup.add(message,0,2);
        Scene scene = new Scene(clientgroup,500,500);
        stage.setTitle("ClientView!");
        stage.setScene(scene);
        stage.show();
        //create client thread:
        pos1.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {try {turn("0");} catch (IOException e) {e.printStackTrace();}}});
        pos2.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {try {turn("1");} catch (IOException e) {e.printStackTrace();}}});
        pos3.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {try {turn("2");} catch (IOException e) {e.printStackTrace();}}});
        pos4.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {try {turn("3");} catch (IOException e) {e.printStackTrace();}}});
        pos5.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {try {turn("4");} catch (IOException e) {e.printStackTrace();}}});
        pos6.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {try {turn("5");} catch (IOException e) {e.printStackTrace();}}});
        pos7.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {try {turn("6");} catch (IOException e) {e.printStackTrace();}}});
        pos8.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {try {turn("7");} catch (IOException e) {e.printStackTrace();}}});
        pos9.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent actionEvent) {try {turn("8");} catch (IOException e) {e.printStackTrace();}}});


        //TIMER STUFF:
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //what you want to do
                Platform.runLater(()->{
                    out.println("A"+" "+"11");
                    try {line = in.readLine();} catch (IOException e) {e.printStackTrace();}
                    currentboard=line.split(" ");
                    //System.out.println(Arrays.toString(currentboard));
                    refreshboard();
                    checkwin();
                    out.println("B"+" "+"435");
                    try {turnplayer = in.readLine();} catch (IOException e) {e.printStackTrace();}
//                    System.out.println(currentplayer + "  :  "+turnplayer);
                });
            }
        }, 0, 500);//wait 0 ms before doing the action and do it evry 1000ms (1second)
    }
    public void turn(String pos) throws IOException {//REuseable turn method
        if (!Objects.equals(currentboard[Integer.parseInt(pos)], "_")){
            message.setText("Spot is Taken");
            refreshboard();
        }
        else if (!Objects.equals(currentplayer, turnplayer)){
            message.setText("Try again later");
            System.out.println("NOT TURN");
        }
        else {
            out.println(currentplayer + " " + pos);
            out.flush();
            try {line = in.readLine();}
            catch (IOException e) {e.printStackTrace();}
            currentboard = line.split(" ");
            refreshboard();
            message.setText("");
        }
        checkwin();
    }
    public void refreshboard(){
        pos1.setText(currentboard[0]);
        pos2.setText(currentboard[1]);
        pos3.setText(currentboard[2]);
        pos4.setText(currentboard[3]);
        pos5.setText(currentboard[4]);
        pos6.setText(currentboard[5]);
        pos7.setText(currentboard[6]);
        pos8.setText(currentboard[7]);
        pos9.setText(currentboard[8]);
    }
    public void clearboard(){for (int i=0;i<9;i++){currentboard[i]="_";}}
    public void checkwin(){
        if (Objects.equals(currentboard[0], "X") && Objects.equals(currentboard[1], "X") && Objects.equals(currentboard[2], "X") || Objects.equals(currentboard[3], "X") && Objects.equals(currentboard[4], "X") && Objects.equals(currentboard[5], "X") || Objects.equals(currentboard[6], "X") && Objects.equals(currentboard[7], "X") && Objects.equals(currentboard[8], "X") ||//Horizontal check
                Objects.equals(currentboard[0], "X") && Objects.equals(currentboard[3], "X") && Objects.equals(currentboard[6], "X") || Objects.equals(currentboard[1], "X") && Objects.equals(currentboard[4], "X") && Objects.equals(currentboard[7], "X") || Objects.equals(currentboard[2], "X") && Objects.equals(currentboard[5], "X") && Objects.equals(currentboard[8], "X") ||//Vertical Check
                Objects.equals(currentboard[0], "X") && Objects.equals(currentboard[4], "X") && Objects.equals(currentboard[8], "X") || Objects.equals(currentboard[2], "X") && Objects.equals(currentboard[4], "X") && Objects.equals(currentboard[6], "X")){//Diagonal check
            WinLabel.setText("Player X is victorious");
            stage.setScene(winscene);
            clearboard();
        }
        else if (Objects.equals(currentboard[0], "O") && Objects.equals(currentboard[1], "O") && Objects.equals(currentboard[2], "O") || Objects.equals(currentboard[3], "O") && Objects.equals(currentboard[4], "O") && Objects.equals(currentboard[5], "O") || Objects.equals(currentboard[6], "O") && Objects.equals(currentboard[7], "O") && Objects.equals(currentboard[8], "O") ||//Horizontal check
                Objects.equals(currentboard[0], "O") && Objects.equals(currentboard[3], "O") && Objects.equals(currentboard[6], "O") || Objects.equals(currentboard[1], "O") && Objects.equals(currentboard[4], "O") && Objects.equals(currentboard[7], "O") || Objects.equals(currentboard[2], "O") && Objects.equals(currentboard[5], "O") && Objects.equals(currentboard[8], "O") ||//Vertical Check
                Objects.equals(currentboard[0], "O") && Objects.equals(currentboard[4], "O") && Objects.equals(currentboard[8], "O") || Objects.equals(currentboard[2], "O") && Objects.equals(currentboard[4], "O") && Objects.equals(currentboard[6], "O")){//Diagonal check
            clearboard();
            WinLabel.setText("Player O is victorious");
            stage.setScene(winscene);
        }
        //System.out.println(Arrays.toString(currentboard));
    }
//    public boolean checkturn() throws IOException {
//        boolean Xturn = true;
//        out.println("B"+" "+"0");
//        try{line = in.readLine();}
//        catch(IOException e){e.printStackTrace();}
//        System.out.println(line);
//        if (line == currentplayer){
//            Xturn = true;
//        }
//        else{
//            Xturn = false;
//        }
//        return Xturn;
//    }
    // CLIENT CLASS:
    public static void main(String[] args) {launch();}
}