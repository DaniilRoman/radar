package src;

import javafx.beans.DefaultProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@DefaultProperty("children")
public class BottomTable extends Region {
    private Pane pane;
    private int currentRaw = 1;
    private List<TextField> firstInputs = new ArrayList<>();
    private List<TextField> secondInputs = new ArrayList<>();
    private List<Button> scBtns = new ArrayList<>();
    private List<Button> kpBtns = new ArrayList<>();
    private List<Button> ruBtns = new ArrayList<>();
    private int currentOffsetY = 10;
    private int fieldW = 70;
    private int btnW = 50;
    private int rawHeight = 20;

    private int startedOffset = 10;
    private int currentOffset = startedOffset;
    private int fieldWidth = fieldW + currentOffset;
    private int btnWidth = btnW + currentOffset;

    private Button test;
    private ScrollPane scrollPane;
    private AddTarget addTargetFunc;
    private int height = 6 * (rawHeight + startedOffset);

    private Locator locator;


    // ******************** Constructors **************************************
    public BottomTable(Locator locator) {
        this.locator = locator;
        Rectangle rect1 = new Rectangle(500, height);
        this.addTargetFunc = locator::addNewTarget;
        scrollPane = new ScrollPane(pane);
        scrollPane.setMaxSize(500, 150);

        ///////////////////////////////////////////////////////////////////////////////////
        test = new Button("BTN");
        test.getStyleClass().add("btn-test");

        test.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
//            Button rect = new Button("test");
//            rect.getStyleClass().add("test");
//            rect.relocate(10 + 20  + 20, 20);
//            pane.getChildren().add(rect);
            getNewRaw();
        });

        test.relocate(400, 10);
        ///////////////////////////////////////////////////////////////////////////////////

        pane = new Pane(scrollPane);
        pane.getChildren().addAll(rect1, test);
//        pane.setMaxSize(500, 50);
        pane.setPrefSize(500, 50);
        getChildren().setAll(pane);
        getNewRaw();
    }

    private void getNewRaw() {

        TextField textField1 = new TextField();
        textField1.setMaxSize(fieldW, rawHeight);
        TextField textField2 = new TextField();
        textField2.setMaxSize(fieldW, rawHeight);
        Button btn1 = new Button("СЦ");
        btn1.setPrefSize(btnW, rawHeight);
        Button btn2 = new Button("АВТО");
        btn2.setPrefSize(btnW, rawHeight);
        Button btn3 = new Button("РУ");
        btn3.setPrefSize(btnW, rawHeight);

        textField1.relocate(currentOffset, currentOffsetY);
        textField2.relocate(currentOffset += fieldWidth, currentOffsetY);
        btn1.relocate(currentOffset += fieldWidth, currentOffsetY);
        btn2.relocate(currentOffset += btnWidth, currentOffsetY);
        btn3.relocate(currentOffset += btnWidth, currentOffsetY);
        currentOffset = startedOffset;

        firstInputs.add(textField1);
        secondInputs.add(textField2);
        scBtns.add(btn1);
        kpBtns.add(btn2);
        ruBtns.add(btn3);


        btn1.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            int i = scBtns.indexOf(btn1);
            int angle = Integer.valueOf(firstInputs.get(i).getText());
            double distance = Double.valueOf(secondInputs.get(i).getText());

//            System.out.println(angle);
//            System.out.println(distance);
            if(distance>0 && distance<100 && angle >= 0 && angle < 360) {
                addTargetFunc.apply(angle + 90, distance);
            }
        });

        btn2.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            int i = kpBtns.indexOf(btn2);
            int angle = Integer.valueOf(firstInputs.get(i).getText());
            double distance = Double.valueOf(secondInputs.get(i).getText());

            locator.onAutoBtnClicked();
        });
//        textField1.textProperty().addListener((obs, oldText, newText) -> {
//            System.out.println(newText);
//        });

//        scrollPane.setVvalue(1.0);

        pane.getChildren().addAll(textField1, textField2, btn1, btn2, btn3);

        currentOffsetY += startedOffset + rawHeight;

        currentRaw++;

        if (currentRaw == 6) {
            test.setVisible(false);
        }
    }

}

