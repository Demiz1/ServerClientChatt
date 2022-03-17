package client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientMain {
    static guiClient guiClient;
    public static void main(String[] arg) throws IOException {
        guiClient = new guiClient();
        System.out.println("starting Client");
        Scanner inFromconsole = new Scanner(System.in);
        boolean loop = true;
        ArrayList<connectionThread> messages = new ArrayList<>();
        connectionThread connection = new connectionThread(messages, guiClient);
        guiClient.addConnectionThread(connection);
        connection.start();
        while (loop) {
            if (inFromconsole.hasNext()) {
                //new message is ready
                String input = inFromconsole.nextLine();

                if (input.compareTo("exit") == 0) {
                    loop = false;
                    connection.closeConnection();
                }else{
                    //create new thread and pass the string...
                    if(input.length()>0){
                        connection.writeToServer(input);
                    }
                }
            }
        }
    }
    public static void printToGuiAndConsole(String input){
        System.out.println(input);
        guiClient.addNewLineChattArea(input);
    }
}
