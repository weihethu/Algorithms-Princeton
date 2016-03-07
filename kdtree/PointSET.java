import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;


public class PointSET {
    private SET<Point2D> points;
    
    public PointSET() {
        points = new SET<Point2D>();
    }
    
    public boolean isEmpty() {
        return points.isEmpty();
    }
    
    public int size() {
        return points.size();
    }
    
    public void insert(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }
        points.add(p);
    }
    
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }
        return points.contains(p);
    }
    
    public void draw() {
        for (Point2D pt : points) {
            pt.draw();
        }
    }
    
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new NullPointerException();
        }
        
        List<Point2D> results = new ArrayList<Point2D>();
        
        for (Point2D pt : points) {   
            if (rect.contains(pt)) {
                results.add(pt);
            }
        }
        
        return results;
    }
    
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }
        
        double maxDist = Double.MAX_VALUE;
        Point2D result = null;
        
        for (Point2D pt : points) {
            double dist = pt.distanceTo(p);
            if (dist < maxDist) {
                maxDist = dist;
                result = pt;
            }
        }
        return result;
    }
    
    public static void main(String[] args) {
        
    }
}
