package MVC;

import javax.swing.*;
import java.awt.*;

public class FTView extends JFrame {
    private JTextField portTextField;
    private JTextArea statusTextArea;
    private JFrame frame;
    private JTabbedPane jTabbedPane;
    private JPanel sendPanel;
    private JPanel receivePanel;
    private JButton listenButton;

    public FTView(){
        frame = new JFrame("File Transfer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buildPanels();
        buildReceiverTab();

        frame.pack();
        frame.setVisible(true);
    }

    private void buildReceiverTab() {
        JPanel portPanel = new JPanel();
        portPanel.setPreferredSize(new Dimension(100,50));
        portPanel.setLayout(new GridLayout(1,2, 10, 10));
        JLabel portLabel = new JLabel("Listening port");
        portPanel.add(portLabel);
        portTextField = new JTextField("3030");
        portPanel.add(portTextField);
        receivePanel.add(portPanel);

        statusTextArea = new JTextArea();
        statusTextArea.setEditable(false);
//        statusTextArea.setSize(300,3000);
        statusTextArea.setPreferredSize(new Dimension(100, 100));
        receivePanel.add(statusTextArea);

        listenButton = new JButton("Listen");
        receivePanel.add(listenButton);
    }

    private void buildPanels() {
        jTabbedPane = new JTabbedPane();

        sendPanel = new JPanel();
        sendPanel.setLayout(new GridLayout(4,2,10,10));
        jTabbedPane.add("Send", sendPanel);

        receivePanel = new JPanel();
        receivePanel.setLayout(new GridLayout(3,1, 10, 10));
        jTabbedPane.add("Receive", receivePanel);

        frame.add(jTabbedPane);
    }
}
