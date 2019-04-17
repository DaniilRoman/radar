package src;

import javafx.beans.DefaultProperty;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;


@DefaultProperty("children")
public class Locator extends Region {
    private double size;
    private double radius;
    private double width;
    private double height;
    private Circle background = new Circle();
    private Circle foreground = new Circle();
    private Rectangle indicator = new Rectangle();
    private Rectangle indicatorTop = new Rectangle();
    private Rectangle indicatorBottom = new Rectangle();
    private Rectangle upperRect = new Rectangle();
    private List<Rectangle> targetsRect = new ArrayList<>();
    private List<Target> targets = new ArrayList<>();
    private Pane pane;
    private Rotate rotate = new Rotate();
    private Rotate rotateTop = new Rotate();
    private Rotate rotateBottom = new Rotate();
    private double _angle = 0.0;
    private double tempAngle = 0;
    private double speed = 0.15;
    private Paint _backgroundPaint;
    private Paint _foregroundPaint;
    private Paint _indicatorPaint;
    private InnerShadow innerShadow;
    private int currentTargetIdx = 0;

    // Center
    private Rectangle centerHorRect = new Rectangle();
    private Rectangle centerVerRect = new Rectangle();




    private int topAngle = 52;
    private int bottomAngle = 360 - topAngle;

    private boolean scTurnOn = true;

    // ******************** Constructors **************************************
    public Locator() throws Exception {
        getStylesheets().add(Locator.class.getResource("radar.css").toExternalForm());
        _backgroundPaint = Color.rgb(190, 200, 93);
        _foregroundPaint = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.rgb(61, 61, 61)),
                new Stop(0.0, Color.rgb(112, 242, 255)),
                new Stop(1.0, Color.rgb(42, 42, 42)));
        _indicatorPaint = Color.rgb(15, 114, 0);
        for (int i = 0; i < 6; i++) {
            targetsRect.add(new Rectangle());
        }

        initGraphics();
        registerListeners();
    }


    // ******************** Initialization ************************************
    private void initGraphics() {

//        getStyleClass().add("angle-picker");

        rotate.setAngle(0);
        rotateTop.setAngle(topAngle);
        rotateBottom.setAngle(bottomAngle);

        // мишура (верхний градиент)
        innerShadow = new InnerShadow(BlurType.TWO_PASS_BOX, Color.rgb(255, 255, 255, 0.3), 1, 0.0, 0, 0.5);

        background.setFill(_backgroundPaint);
        background.setMouseTransparent(true);

        foreground.setFill(_foregroundPaint);
        foreground.setEffect(innerShadow);

        upperRect.setFill(_indicatorPaint);

        indicator.getTransforms().add(rotate);
        indicator.setMouseTransparent(true);

        indicatorTop.getTransforms().add(rotateTop);
        indicatorTop.setMouseTransparent(true);

        indicatorBottom.getTransforms().add(rotateBottom);
        indicatorBottom.setMouseTransparent(true);

        pane = new Pane(background, foreground, indicator, indicatorTop, indicatorBottom, upperRect, centerHorRect, centerVerRect);
        pane.getChildren().addAll(targetsRect);
        getChildren().setAll(pane);
    }

    private void registerListeners() {
        widthProperty().addListener(o -> resize());
        heightProperty().addListener(o -> resize());
    }


    public void setAngle(final Double angle) {
        int sign = tempAngle <= angle ? 1 : -1;
        tempAngle = angle;
        _angle += sign * speed;
        rotate.setAngle(_angle);
        rotateTop.setAngle(_angle + topAngle);
        rotateBottom.setAngle(_angle + bottomAngle);
    }

    public void onKpClicked() {
        int angle = new Random().nextInt(360);
        Target newTarget = addNewTarget(angle, radius);
        newTarget.startTarget();
    }

    public Target addNewTarget(int angle, double distance) {
        double halfSize = size * 0.5;
        distance = halfSize * distance * 0.01;
        Target newTarget = new Target(angle, targetsRect.get(currentTargetIdx++), radius, distance, size);
        targets.add(newTarget);
        return newTarget;
    }

    public void onAutoClicked() {

        if (scTurnOn) {
            Target target = targets.get(0);
            Executors.newFixedThreadPool(1).submit(() -> {
                try {
                    if (_angle - 30 <= target.angle && target.angle <= _angle + 30) {
                        if (target.angle >= _angle - 30 && target.angle <= _angle) {
                            double diffAngle = Math.abs(_angle - target.angle);
                            for (int i = 0; i < 10; i++) {
                                Thread.sleep(400);
                                setAngle(_angle - diffAngle / 10);
                            }
                            setAngle(target.angle);
                        }
                        if (target.angle <= _angle + 30 && target.angle >= _angle) {
                            double diffAngle = Math.abs(target.angle - _angle);
                            for (int i = 0; i < 10; i++) {
                                Thread.sleep(400);
                                setAngle(_angle + diffAngle / 10);
                            }
                            setAngle(target.angle);
                        }
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            });
        }

    }


    public void onScClicked() {
        scTurnOn = true;
    }


    public void onRuClicked() {

    }

    // ******************** Resizing ******************************************
    private void resize() {
        width = getWidth() - getInsets().getLeft() - getInsets().getRight();
        height = getHeight() - getInsets().getTop() - getInsets().getBottom();
        size = width < height ? width : height;
        radius = size * 0.5;

        System.out.println("============ RESIZE ============");

        if (width > 0 && height > 0) {
            pane.setMaxSize(size, size);
            pane.setPrefSize(size, size);
            pane.relocate((getWidth() - size) * 0.5, (getHeight() - size) * 0.5);

            innerShadow.setRadius(size * 0.0212766);
            innerShadow.setOffsetY(size * 0.0106383);

            background.setRadius(size * 0.5);
            background.relocate(0, 0);

            foreground.setRadius(size * 0.4787234);
            foreground.relocate(size * 0.0212766, size * 0.0212766);


            //
            rotate.setPivotX(-size * 0.37);
            rotate.setPivotY(0);

            indicator.setWidth(size * 0.1);
            indicator.setHeight(size * 0.01587302);
            indicator.relocate(size * 0.87, size * 0.5);
            //

            //
            rotateTop.setPivotX(indicatorTop.getX() - size * 0.1);
            rotateTop.setPivotY(indicatorTop.getHeight() * 0.5);

            indicatorTop.setWidth(size * 0.37);
            indicatorTop.setHeight(size * 0.01587302);
            indicatorTop.relocate(size * 0.6, size * 0.5);
            //

            //
            rotateBottom.setPivotX(indicatorBottom.getX() - size * 0.1);
            rotateBottom.setPivotY(indicatorBottom.getHeight() * 0.5);

            indicatorBottom.setWidth(size * 0.37);
            indicatorBottom.setHeight(size * 0.01587302);
            indicatorBottom.relocate(size * 0.6, size * 0.5);
            //

            upperRect.setWidth(size * 0.01);
            upperRect.setHeight(size * 0.1);
            upperRect.relocate(size * 0.5, size * 0.02);

            //Center
            double w2 = size*0.06, w1 = size*0.01;
            centerHorRect.setWidth(w2); centerHorRect.setHeight(w1);
            centerHorRect.relocate(size * 0.5 - w2*0.5, size * 0.5 - w1*0.5);
            centerVerRect.setWidth(w1); centerVerRect.setHeight(w2);
            centerVerRect.relocate(size * 0.5 - w1*0.5, size * 0.5 - w2*0.5);

            redraw();
        }
    }

    private void redraw() {
        background.setFill(_backgroundPaint);
        foreground.setFill(_foregroundPaint);
        indicator.setFill(_indicatorPaint);
        indicatorTop.setFill(_indicatorPaint);
        indicatorBottom.setFill(_indicatorPaint);
    }
}
