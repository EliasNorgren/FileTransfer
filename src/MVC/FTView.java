package MVC;

import javax.swing.*;
import java.awt.event.ActionListener;

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

    }

    public int getReceivePortNumber(){
        return Integer.parseInt(receivePort.getText());
    }

    public void addListenButtonListener(ActionListener ae){
        this.listenButton.addActionListener(ae);
    }

    public void setListenButtonEnabled(boolean enabled){
        listenButton.setEnabled(enabled);
    }
}
