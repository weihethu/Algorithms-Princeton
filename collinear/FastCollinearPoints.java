import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;


public class FastCollinearPoints {
    
    private LineSegment[] segments = null;
    
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new NullPointerException();
        }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new NullPointerException();
            }
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }
        points = Arrays.copyOf(points, points.length);
        
        Arrays.sort(points);
        
        List<LineSegment> results = new ArrayList<LineSegment>();
        for (int i = 0; i < points.length; i++) {
            Point[] copy = Arrays.copyOf(points, points.length);
            // stable sort
            Arrays.sort(copy, points[i].slopeOrder());
            
            // after sort, the first in copy is points[i]
            assert (copy[0] == points[i]);
            int len = 1, startIndex = 1;
            for (int j = 2; j < copy.length; j++) {
                if (points[i].slopeOrder().compare(copy[j], copy[j - 1]) == 0) {
                    len++;
                } else {
                    if (len >= 3 && copy[startIndex].compareTo(points[i]) > 0) {
                        results.add(new LineSegment(points[i], copy[j - 1]));
                    }
                    len = 1;
                    startIndex = j;
                }
            }
            
            if (len >= 3 && copy[startIndex].compareTo(points[i]) > 0) {
                results.add(new LineSegment(points[i], copy[points.length - 1]));
            }
        }

        segments = new LineSegment[results.size()];
        results.toArray(segments);
    }
    
    public int numberOfSegments() {
        return segments.length;
    }
    
    public LineSegment[] segments() {
        return segments.clone();
    }
    
    public static void main(String[] args) {
        // read the N points from a file
        In in = new In(args[0]);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.show(0);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
    }
}
