package MVC;

import javax.swing.*;
import java.awt.*;

public class FTView extends JFrame {
    private JFrame frame;
    private JTabbedPane jTabbedPane;
    private JPanel sendPanel;
    private JPanel receivePanel;

    public FTView(){
        frame = new JFrame("RadioInfo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buildPanels();

        receivePanel.add(new JButton("asdasd"));

        frame.pack();
        frame.setVisible(true);
    }

    private void buildPanels() {
        jTabbedPane = new JTabbedPane();

        sendPanel = new JPanel();
        sendPanel.setLayout(new GridLayout());
        jTabbedPane.add("Send", sendPanel);

        receivePanel = new JPanel();
        receivePanel.setLayout(new GridLayout());
        jTabbedPane.add("Receive", receivePanel);

        frame.add(jTabbedPane);
    }
}
