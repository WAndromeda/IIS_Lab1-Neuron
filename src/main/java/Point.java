public class Point {
    public double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(String s1, String s2) {
        this.x = Double.parseDouble(s1);
        this.y = Double.parseDouble(s2);
    }
}
