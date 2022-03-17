package server;

public class messageOBJ {
    String message;
    long time;
    clientThread senderClient;
    messageOBJ(String message, long time, clientThread senderClient){
        this.message = message;
        this.time = time;
        this.senderClient = senderClient;
    }

    public String getMessage(){
        return message;
    }

    public long getTime() {
        return time;
    }

    public clientThread getSenderClient() {
        return senderClient;
    }

    public String getFormattedMessge(){
        String output;
        if (senderClient.getAlias() != null){
            output = senderClient.getAlias() + " : " + this.message;
        }else{
            output = "Client \"" + this.senderClient.socket.getLocalAddress().toString() + "\" : " + this.message;
        }
        return output;
    }
}
