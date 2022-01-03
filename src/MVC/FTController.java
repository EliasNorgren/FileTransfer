package MVC;

public class FTController {

    private FTView view;
    private FTModel model;

    public FTController(FTModel ftModel, FTView ftView) {
        this.model = ftModel;
        this.view = ftView;
    }
}
