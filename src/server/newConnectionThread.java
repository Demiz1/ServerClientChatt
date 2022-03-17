package server;

import globals.constants;

import javax.swing.*;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class newConnectionThread extends Thread {
    ServerSocket welcomeSocket;
    ArrayList<clientThread> clients;
    volatile boolean acceptNewConnections = true;
    ArrayList<messageOBJ> messageOBJS;
    ClientLister clientLister;
    guiServer guiServer;

    public newConnectionThread(
            int port, ArrayList<clientThread> clients, ArrayList<messageOBJ> messageOBJS, ClientLister clientLister,guiServer guiServer) throws IOException {
        this.clients = clients;
        this.clientLister = clientLister;
        try {
            welcomeSocket = new ServerSocket(port);
        }catch (BindException e){
            JOptionPane.showMessageDialog(new JFrame(), "Could not open port, exiting");
            System.exit(0);
        }
        this.messageOBJS = messageOBJS;
        this.guiServer = guiServer;
    }
    @Override
    public void run() {
        while(acceptNewConnections){
            try {
                welcomeSocket.setSoTimeout(100);
                Socket connectionSocket = welcomeSocket.accept();
                System.out.println("New connection @" + connectionSocket.getLocalAddress().toString());
                guiServer.addTextServerTextArea("New connection @" + connectionSocket.getLocalAddress().toString());
                clientThread newConnection = new clientThread(connectionSocket, clients, messageOBJS, this.clientLister, guiServer);
                newConnection.start();
                clients.add(newConnection);
                //notify clientListerThread
                System.out.println("Im ready for new connections!");
                guiServer.addTextServerTextArea("Im ready for new connections!");

            } catch (SocketTimeoutException e){
                //no new connections
                continue;
            }
            catch (IOException e) {
                e.printStackTrace();
                System.out.println("something went wrong");
                guiServer.addTextServerTextArea("Something went wrong when accepting new connection");
            }
        }
    }
    public void stopAcceptingNewConnecting(){
        acceptNewConnections = false;
    }
}
