package src;

import javafx.scene.shape.Rectangle;

import java.util.concurrent.Executors;

public class Target {
    private int angle = 0;
    private double x = 0;
    private double y = 0;
    private double sin = 0;
    private double cos = 0;
    private int signX = 1;
    private int signY = 1;
    private Rectangle target;
    private double radius;
    private double size;

    public Target(int angle, Rectangle target, double radius, double size) {
        this.angle = angle;
        this.target = target;
        this.radius = radius;
        this.size = size;
        sin = Math.sin(gradusToRodian(angle));
        cos = Math.cos(gradusToRodian(angle));
        Executors.newFixedThreadPool(1).submit(this::startTarget);
    }

    private void startTarget() {


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 100; i++) {
            try {
                if ( angle >= 0 && angle <= 90 ) { signX = -1; signY = -1; }
                if ( angle > 90 && angle <= 180 ) { signX = +1; signY = -1; }
                if ( angle > 180 && angle <= 270 ) { signX = +1; signY = +1; }
                if ( angle > 270 && angle < 360 ) { signX = -1; signY = +1; }
                redrawTarget();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void redrawTarget() {

        target.setVisible(true);


        double xStart = radius + radius * cos;
        double yStart = radius + radius * sin;

        if (x == 0) { x = xStart; }
        if (y == 0) { y = yStart; }

        System.out.println("1:::::" + x + "     "+ y);

        double oldX = x;
        if (angle == 270) {
            x = oldX;
            y += 5;
        } else if (angle == 90){
            x = oldX;
            y -= 5;
        } else {
            x += 5 * signX;
            y = (((x - radius) * (y - radius))/(oldX - radius)) + radius;
        }
        target.relocate(x, y);

        target.setWidth(size * 0.03);
        target.setHeight(size * 0.03);
    }

    private double gradusToRodian(double gradus) {
        return gradus * (3.14 / 180);
    }

}