package server;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

//class for gui
public class guiServer extends JFrame {
    private JTextArea chatTextArea;
    private JScrollPane textAreaScroll;

    private JTextArea serverTextStatusArea;
    private JScrollPane serverTextStatusAreaScroll;

    private JLabel textLable;
    private JPanel listPanel;
    guiServer(){
        this.setName("Chat server");

        //client messages
        this.chatTextArea = new JTextArea( 10, 1);
        this.chatTextArea.setAutoscrolls(true);
        this.textAreaScroll = new JScrollPane(
                this.chatTextArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        //server status area
        this.serverTextStatusArea = new JTextArea(1, 1);
        this.serverTextStatusArea.setAutoscrolls(true);
        this.serverTextStatusAreaScroll = new JScrollPane(
                this.serverTextStatusArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        //client count
        this.textLable = new JLabel();
        this.textLable.setHorizontalAlignment(JLabel.LEFT);

        //panel
        this.listPanel = new JPanel();
        this.listPanel.setLayout(new BoxLayout(this.listPanel, BoxLayout.PAGE_AXIS));

        //add all to panel
        this.listPanel.add(this.textLable);
        this.listPanel.add(this.serverTextStatusAreaScroll);
        this.listPanel.add(this.textAreaScroll);

        this.add(this.listPanel);
        this.setSize(700,600);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //server is about to shut down.
                serverMain.shutDownServer();
            }
        });
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void changeLable(String newText){
        this.textLable.setText(newText);
    }
    public void addTextServerTextArea(String appendRow){
        this.serverTextStatusArea.append(appendRow + "\n");
    }

    public void addTextChattTextArea(String appendRow){
        this.chatTextArea.append("\n" + appendRow);
    }
}
