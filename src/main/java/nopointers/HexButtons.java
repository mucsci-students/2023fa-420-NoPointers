package nopointers;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;

public class HexButtons {

    private Polygon polygon;
    private double radius;
    private double radianStep = (2 * Math.PI) / 6;

    private double offsetY;
    private double offsetX;

    public HexButtons(double radius, Paint color) {
        this.radius = radius;
        makeHexagon(radius, color);
        offsetY = calculateApothem();



        offsetX = radius * 1.5;

    }

    private void makeHexagon(double radius, Paint color) {
        polygon = new Polygon();
        polygon.setFill(color);
        polygon.setStroke(Color.WHITESMOKE);
        polygon.setEffect(new DropShadow(10, Color.BLACK));
        polygon.setStrokeWidth(10);
        polygon.setStrokeType(StrokeType.INSIDE);

        for (int i = 0; i < 6; i++) {
            double angle = radianStep * i;

            polygon.getPoints().add(Math.cos(angle) * radius / 1.1);
            polygon.getPoints().add(Math.sin(angle) * radius / 1.1);

        }
    }

    public double getOffsetY() {
        return offsetY;
    }

    public double getOffsetX() {
        return offsetX;
    }

    private double calculateApothem() {

        return (Math.tan(radianStep) * radius) / 2;

    }

}