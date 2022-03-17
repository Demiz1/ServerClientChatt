package server;

import globals.constants;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class serverMain {
    static ArrayList<clientThread> clients = new ArrayList<>();
    static ArrayList<messageOBJ> messageOBJS = new ArrayList<>();
    static ClientLister clientLister;
    static newConnectionThread newConnectionThread;
    static guiServer guiServer;
    public static void main(String[] args) throws IOException {
        //ask about a new port
        int intPortnumber = constants.portNr;
        String portnumber = JOptionPane.showInputDialog("Enter a portnumber, four numbers, like 6666", constants.portNr).trim();
        try{
            intPortnumber = Integer.parseInt(portnumber);
        }catch (NumberFormatException e){
            JOptionPane.showMessageDialog(new JOptionPane(), "Don't fuck with the number formatting, ok?");
            System.exit(0);
        }

        guiServer = new guiServer();
        printToServerTextAreaAndConsole("starting Server");
        Scanner inFromConsole = new Scanner(System.in);

        //starting client lister
        clientLister = new ClientLister(clients, guiServer);
        clientLister.start();

        //starting connection handler
        newConnectionThread = new newConnectionThread(intPortnumber, clients, messageOBJS, clientLister, guiServer);
        newConnectionThread.start();

        printToServerTextAreaAndConsole("all ok, now accepting clients");



        boolean loop = true;
        while (loop){
            if(System.in.available() >0){
                String input = inFromConsole.nextLine();
                if(input.startsWith("exit")){
                    shutDownServer();
                    loop = false;
                }
            }
            if (messageOBJS.size() > 0 && loop){
                printToChattTextAreaAndConsole("new messages, sending \"" + messageOBJS.get(0) + "\" to clients");
                for (int i=0;i<clients.size();i++){
                    printToChattTextAreaAndConsole("Sendign to " + clients.get(i).socket.getLocalAddress().toString());
                    clients.get(i).writeMessageObjToConnection(messageOBJS.get(0));
                }
                messageOBJS.remove(messageOBJS.get(0));
            }
        }
        System.out.println("done!");
    }

    public static void shutDownServer(){
        System.out.println("shutting down the server");
        clientLister.stopLoop();
        newConnectionThread.stopAcceptingNewConnecting();
        for(int i=0;i<clients.size();i++){
            clients.get(i).closeConnection();
        }
        System.exit(0);
    }

    private static void printToChattTextAreaAndConsole(String text){
        System.out.println(text);
        guiServer.addTextServerTextArea(text);
    }

    private static void printToServerTextAreaAndConsole(String text){
        System.out.println(text);
        guiServer.addTextServerTextArea(text);
    }

}
