package client;

import globals.constants;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class connectionThread extends java.lang.Thread {
    ArrayList<connectionThread> messageThreads;
    Socket clientSocket;
    DataOutputStream toServer;
    BufferedReader fromServer;
    boolean keepListening = true;
    guiClient guiClient;
    connectionThread(ArrayList<connectionThread> messageThreads, guiClient guiClient) throws IOException {
        this.guiClient = guiClient;
        this.messageThreads = messageThreads;
        //open new socket.
        printToGuiAndConsole("Connecting to server...");
        clientSocket = new Socket("localhost", constants.portNr);
        printToGuiAndConsole("Connection successful");
        toServer = new DataOutputStream(clientSocket.getOutputStream());
        fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    @Override
    public void run() {
        while(keepListening){
            try{
                String responseFromServer = fromServer.readLine();
                if(responseFromServer == null){
                    printToGuiAndConsole("The connection is dead");
                    closeConnection();
                }else{
                    printToGuiAndConsole(responseFromServer);
                }
            }catch (IOException e){
                if (keepListening){
                    e.printStackTrace();
                    printToGuiAndConsole("There was an error reading message from the server");
                }
                closeConnection();
            }
        }
    }
    public void writeToServer(String message){
        System.out.println("Sending : \"" + message + "\" to the server");
        PrintWriter writer = new PrintWriter(toServer, true);
        writer.println(message); // send string to server
    }
    private void printToGuiAndConsole(String input){
        guiClient.addNewLineChattArea(input);
        System.out.println(input);
    }

    public void closeConnection(){
        keepListening = false;
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.messageThreads.remove(this);
    }
}
