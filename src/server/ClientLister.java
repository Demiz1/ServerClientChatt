package server;

import java.util.ArrayList;

public class ClientLister extends java.lang.Thread {
    ArrayList<clientThread> clientList;
    int sleepTime = 7;
    volatile boolean looper = true;
    guiServer guiServer;
    ClientLister(ArrayList<clientThread> clientList, guiServer guiServer){
        this.clientList = clientList;
        this.guiServer = guiServer;
    }
    @Override
    public void run() {
        while(looper) {
            try {
                java.lang.Thread.sleep(sleepTime * 1000);
            } catch (InterruptedException e){
                System.out.println("Aborted sleep");
            }
            if(looper){
                System.out.println("FROM CENTILITER : there are " + clientList.size() + " clients connected");
                this.guiServer.changeLable("There are " + clientList.size() + " clients connected");
            }
        }
    }

    public void stopLoop(){
        looper = false;
        this.interrupt();
        System.out.println("listener is stopped");
    }
}
