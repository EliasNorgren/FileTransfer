package MVC;

import javax.swing.*;

public class FTView extends JFrame{
    private JPanel mainPanel;
    private JTextField textField1;
    private JTabbedPane recTab;
    private JTextArea textArea1;
    private JButton listenButton;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton sendButton;
    private JProgressBar progressBar1;
    private JLabel statusText;

    public FTView(){
        super("File Transfer");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        this.setVisible(true);
    }

}
