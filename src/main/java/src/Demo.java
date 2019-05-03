package src;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Arrays;


public class Demo extends Application {
    private Locator locatorPanel = new Locator(this::scDisableHook);
    private Controller controllerPanel = new Controller((Double angle) -> locatorPanel.setAngle(angle));
    private Button kpBtn = new Button();
    private Label kpLabel = new Label("     КП");
    private Button autoBtn = new Button();
    private Label autoLabel = new Label("АВТОНОМНО");
    private Button scBtn = new Button("СЦ");
    private Button ruBtn = new Button();
    private Label ruLable = new Label("      РУ");


    private BottomTable bottomTable = new BottomTable(locatorPanel);

    public Demo() throws Exception {
    }

    @Override
    public void init() {
        kpBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> { locatorPanel.onKpClicked(); });
//        autoBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> { locatorPanel.onAutoClicked(); });
//        scBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> { locatorPanel.onScClicked(); });
//        ruBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> { locatorPanel.onRuClicked(); });
        locatorPanel.setPrefSize(400, 400);
        controllerPanel.setPrefSize(150, 150);
        bottomTable.setPrefSize(500, 150);

        BorderPane.setMargin(controllerPanel, new Insets(500, 0, 0, 0));
    }

    @Override
    public void start(Stage stage) {
        Arrays.asList(kpBtn, autoBtn, ruBtn, scBtn).forEach(btn -> btn.getStyleClass().add("right-panel_btn"));
        Arrays.asList(autoBtn).forEach(btn -> btn.getStyleClass().add("blue"));
        BorderPane pane = new BorderPane();
        pane.setCenter(locatorPanel);
        pane.setRight(getRightPane());
        pane.setBottom(bottomTable);

        pane.setBackground(new Background(new BackgroundFill(Color.web("#BEC85D"), CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setPadding(new Insets(30));

        Scene scene = new Scene(pane, 1300, 1000);
        scene.getStylesheets().add(getClass().getResource("btn.css").toExternalForm());
        stage.setScene(scene);
        //TODO check on computers in university and thinking about correct way
//        stage.setResizable(false);
        stage.show();
    }

    private Pane getRightPane() {
        BorderPane.setMargin(kpBtn, new Insets(0, 100, 0, 0));
        BorderPane topPane = new BorderPane();
        BorderPane topLeftPane = new BorderPane();
        topLeftPane.setTop(kpBtn);
        topLeftPane.setBottom(kpLabel);
        BorderPane topRightPane = new BorderPane();
        topRightPane.setTop(autoBtn);
        topRightPane.setBottom(autoLabel);
        topPane.setLeft(topLeftPane); topPane.setRight(topRightPane);
        BorderPane bottomPane = new BorderPane();
        BorderPane bottomRightPane = new BorderPane();
        bottomRightPane.setBottom(ruLable);
        bottomRightPane.setTop(ruBtn);
//        bottomPane.setLeft(scBtn);
        bottomPane.setRight(bottomRightPane);

        BorderPane paneForReturn = new BorderPane();
        paneForReturn.setTop(topPane);
        paneForReturn.setBottom(bottomPane);
        BorderPane.setMargin(controllerPanel, new Insets(100, 0, 100,  0));
        paneForReturn.setCenter(controllerPanel);

        scBtn.setDisable(true);

        return paneForReturn;
    }

    private void scDisableHook(Boolean isDisable) {
        scBtn.setDisable(isDisable);
    }

    @Override
    public void stop() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
