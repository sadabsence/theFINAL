package stepa.project;


public class Point {

    public double x;
    public double y;


    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }


    public double getLength(Point p2) {
        return Math.sqrt(
                Math.pow(this.x - p2.x, 2) +
                        Math.pow(this.y - p2.y, 2)
        );
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
