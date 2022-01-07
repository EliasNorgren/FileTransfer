package MVC;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FTController {

    private final FTView view;
    private boolean workerFailed = false;

    public FTController(FTView ftView) {
        this.view = ftView;
        view.addListenButtonListener(new ListenListener());
        view.addSendListener(new SendListener());
    }
    private class ListenListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                view.setListenButtonEnabled(false);
                ListenWorker worker = new ListenWorker(view.getReceivePortNumber());
                worker.execute();
            } catch (NumberFormatException ex) {
                view.printToListen("ERROR ");
                view.printToListen(ex.toString());
            }
        }
    }

    private class ListenWorker extends SwingWorker<String, String>{

        private final int recPortNumber;

        public ListenWorker(int port){
            this.recPortNumber = port;
        }

        @Override
        protected String doInBackground() {
            try {
                ReceiverModel model = new ReceiverModel();
                publish("Receiver listening on " + recPortNumber);
                model.listen(recPortNumber);
                while(model.hasNext()){
                    publish("File received: " + model.read());
                }
                model.close();
            } catch (IOException e) {
                return e.toString();
            }
            return "Done!";
        }

        @Override
        protected void process(List<String> chunks) {
            for(String s : chunks){
                view.printToListen(s);
            }
        }

        @Override
        protected void done() {
            try {
                view.printToListen(get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            view.setListenButtonEnabled(true);
        }
    }


    private class SendListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.setSendButtonEnabled(false);
            SendWorker worker = new SendWorker(view.getSendHostName(), view.getSendPortNumber(), view.getSendDir());
            worker.execute();
        }
    }

    private class SendWorker extends SwingWorker<String, String>{

        private final String hostName;
        private final int port;
        private final File dir;

        public SendWorker(String hostName, int portNumber, File dir){
            this.hostName = hostName;
            this.port = portNumber;
            this.dir = dir;
        }

        @Override
        protected String doInBackground() {
            SenderModel sender = new SenderModel();
            sender.readFiles(dir);
            try{
                sender.connectToReceiver(hostName, port);
                while(sender.hasNext()){
                    publish(sender.sendNext());
                }
                sender.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Done!";
        }

        @Override
        protected void process(List<String> chunks) {
            for(String s : chunks){
                view.printToSender(s);
            }
        }

        @Override
        protected void done() {
            try {
                view.printToSender(get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            view.setSendButtonEnabled(true);
        }
    }
}
