import MVC.FTController;
import MVC.FTView;

import javax.swing.*;

public class MVCMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FTController(new FTView());
        });
    }
}
