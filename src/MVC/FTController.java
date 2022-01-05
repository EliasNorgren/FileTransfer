package MVC;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FTController {

    private FTView view;
    private FTModel model;

    public FTController(FTModel ftModel, FTView ftView) {
        this.model = ftModel;
        this.view = ftView;

        view.addListenButtonListener(new ListenListener());


    }
    private static class ListenListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
}
