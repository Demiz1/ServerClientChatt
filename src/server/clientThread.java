package server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class clientThread extends java.lang.Thread {
    Socket socket;
    ArrayList<clientThread> clients;
    ClientLister clientLister;
    volatile boolean keepConnection = true;
    BufferedReader input;
    DataOutputStream output;
    ArrayList<messageOBJ> messageOBJS;
    String alias;
    guiServer guiServer;

    clientThread(Socket socket, ArrayList<clientThread> clients, ArrayList<messageOBJ> messageOBJS, ClientLister clientLister, guiServer guiServer) throws IOException {
        this.socket = socket;
        this.clients = clients;
        this.guiServer = guiServer;
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new DataOutputStream(socket.getOutputStream());
        this.messageOBJS = messageOBJS;
        this.clientLister = clientLister;
    }

    @Override
    public void run() {
        while(keepConnection){
            try{
                String sentence = input.readLine();
                if(sentence == null){
                    printToServerTextAreaWithOrWithoutAlias("The connection is dead with the client :");
                    closeConnection();
                }else{
                    //this.message.add(sentence);
                    printToChattTextAreaWithOrWithoutAlias("Received :" + sentence);
                    handleMessage(sentence);
                }
            } catch (IOException e) {
                if(keepConnection){
                    e.printStackTrace();
                    printToServerTextAreaWithOrWithoutAlias("There was an error while reading new messages from client ");
                }
                closeConnection();
            }
        }
    }

    public void writeToConnection(String message){
        if (keepConnection){
                PrintWriter writer = new PrintWriter(output, true);
                writer.println(message); // send string to server
        }
    }

    public void writeMessageObjToConnection(messageOBJ messageOBJ){
        if (keepConnection){
            PrintWriter writer = new PrintWriter(output, true);
            writer.println(messageOBJ.getFormattedMessge());
        }
    }

    public void closeConnection(){
        keepConnection = false;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("tried to close connection");
        }
        this.clients.remove(this);
    }

    public String getAlias() {
        return alias;
    }

    private void handleMessage(String message){
        if(message.startsWith("setAlias:")){
            setAlias(message.substring("setAlias:".length()));
        }else{
            newMessage(message);
        }
    }
    private void setAlias(String newAlias){
        this.alias = newAlias;
    }

    private void newMessage(String message){
        messageOBJ messageOBJ = new messageOBJ(message, System.currentTimeMillis(), this);
        messageOBJS.add(messageOBJ);
    }

    private void printToServerTextAreaWithOrWithoutAlias(String text){
        if(alias != null){
            String out = text + this.socket.getLocalAddress().toString() + "with alias \"" + this.alias;
            System.out.println(out);
            guiServer.addTextServerTextArea(out);
        }else{
            String out = text + this.socket.getLocalAddress().toString();
            System.out.println(out);
            guiServer.addTextServerTextArea(out);
        }
    }
    private void printToChattTextAreaWithOrWithoutAlias(String text){
        if(alias != null){
            String out = text + this.socket.getLocalAddress().toString() + "with alias \"" + this.alias;
            System.out.println(out);
            guiServer.addTextChattTextArea(out);
        }else{
            String out = text + this.socket.getLocalAddress().toString();
            System.out.println(out);
            guiServer.addTextChattTextArea(out);
        }
    }
}
