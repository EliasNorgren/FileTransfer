package MVC;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;

public class FTView extends JFrame{
    private JPanel mainPanel;

    private JTextField receivePort;
    private JTabbedPane recTab;
    private JTextArea receiveTextArea;
    private JButton listenButton;

    private JTextField sendIPTextField;
    private JTextField sendPortTextField;
    private JTextField sendDirTextField;
    private JButton sendButton;
    private JProgressBar progressBar1;
    private JLabel statusText;
    private JTextArea sendTextArea;

    public FTView(){
        super("File Transfer");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        this.setVisible(true);
        progressBar1.setMinimum(0);
        progressBar1.setMaximum(100);
    }

    public void setProgressBarValue(int n ){
        progressBar1.setValue(n);
    }

    public int getProgressbarValue(){
        return progressBar1.getValue();
    }

    public int getReceivePortNumber() throws NumberFormatException{
        return Integer.parseInt(receivePort.getText());
    }

    public void addListenButtonListener(ActionListener ae){
        this.listenButton.addActionListener(ae);
    }

    public void setListenButtonEnabled(boolean enabled){
        listenButton.setEnabled(enabled);
    }

    public void showDialog(String msg, String title, int messageType) {
        JOptionPane.showMessageDialog(null, msg, title, messageType);
    }

    public void printToListen(String text){
        receiveTextArea.append(text + "\n");
    }

    public void addSendListener(ActionListener ae) {
        this.sendButton.addActionListener(ae);
    }

    public String getSendHostName(){
        return this.sendIPTextField.getText();
    }

    public int getSendPortNumber() {
        return Integer.parseInt(this.sendPortTextField.getText());
    }

    public File getSendDir() {
        return new File(this.sendDirTextField.getText());
    }

    public void setSendButtonEnabled(boolean b) {
        this.sendButton.setEnabled(b);
    }

    public void printToSender(String s) {
        this.sendTextArea.append(s + "\n");
    }
}
