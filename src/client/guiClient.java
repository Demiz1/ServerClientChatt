package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class guiClient extends JFrame {
    private JPanel panel;
    private JTextArea chattArea;
    private JScrollPane chattAreaScroll;

    private JTextField textFieldChattInput;
    private JButton jButtonSendMessage;
    private JPanel inputPanel;
    private connectionThread connectionThread;

    public void addConnectionThread(connectionThread connection){
        this.connectionThread = connection;
    }

    guiClient(){
        

        this.chattArea = new JTextArea();
        this.chattArea.setAutoscrolls(true);
        this.chattAreaScroll = new JScrollPane(this.chattArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.chattAreaScroll.setPreferredSize(new Dimension(400,200));
        this.jButtonSendMessage = new JButton("Send message");
        this.jButtonSendMessage.setPreferredSize(new Dimension(200,40));
        this.textFieldChattInput = new JTextField();
        this.textFieldChattInput.setPreferredSize(new Dimension(200,40));
        this.panel = new JPanel();
        this.panel.setLayout(new GridBagLayout());

        GridBagConstraints chattAreaConstraints = new GridBagConstraints();
        chattAreaConstraints.gridx = 0;
        chattAreaConstraints.gridy = 0;
        chattAreaConstraints.gridwidth = 3;
        chattAreaConstraints.gridheight = 10;
        chattAreaConstraints.weighty = 0.5;
        chattAreaConstraints.fill = GridBagConstraints.BOTH;
        this.panel.add(this.chattAreaScroll, chattAreaConstraints);
        GridBagConstraints textFieldConstraints = new GridBagConstraints();
        textFieldConstraints.gridx = 0;
        textFieldConstraints.gridy = 11;
        textFieldConstraints.weightx = 0;
        textFieldConstraints.gridwidth = 2;
        textFieldConstraints.weightx = 0.5;
        textFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        this.panel.add(this.textFieldChattInput, textFieldConstraints);

        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 2;
        buttonConstraints.gridy = 11;
        buttonConstraints.weightx = 0;
        buttonConstraints.gridwidth = 1;
        buttonConstraints.fill = GridBagConstraints.HORIZONTAL;
        this.panel.add(this.jButtonSendMessage, buttonConstraints);

        this.jButtonSendMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //see if there is any input in the text area
                String InputText = textFieldChattInput.getText();
                if(InputText.length()>0){
                    //there is text to send!
                    connectionThread.writeToServer(InputText);
                    textFieldChattInput.setText("");
                }
            }
        });

        this.add(this.panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("shutting down");
                System.exit(0);
            }
        });
        this.setVisible(true);
    }

    private void sendMessage(String sendString){

    }

    public void addNewLineChattArea(String newLine){
        this.chattArea.append("\n" + newLine);
    }
}
