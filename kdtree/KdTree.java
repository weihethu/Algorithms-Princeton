import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private static final int XMAX = 1;
    private static final int XMIN = 0;
    private static final int YMAX = 1;
    private static final int YMIN = 0;

    private int numPoints;
    private TreeNode root;

    public KdTree() {
        this.numPoints = 0;
        this.root = null;
    }

    public boolean isEmpty() {
        return this.numPoints == 0;
    }

    public int size() {
        return numPoints;
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }

        if (root == null) {
            root = new TreeNode(p, new RectHV(XMIN, YMIN, XMAX, YMAX), true);
            numPoints++;
        } else {
            if (root.insert(p)) {
                numPoints++;
            }
        }
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }

        if (root == null) {
            return false;
        }

        return root.search(p) != null;
    }

    public void draw() {
        if (this.root != null) {
            this.root.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new NullPointerException();
        }

        if (this.root == null) {
            return new ArrayList<Point2D>();
        }

        Collection<Point2D> results = this.root.range(rect);

        return results;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }

        if (this.root == null) {
            return null;
        } else {
            return this.root.nearest(p, new Pair<Double, Point2D>(
                    Double.MAX_VALUE, null)).getY();
        }
    }

    public static void main(String[] args) {
    }

    private class TreeNode {
        public TreeNode(Point2D point, RectHV rect, boolean useXCoordinate) {
            this.point = point;
            this.rect = rect;
            left = null;
            right = null;
            this.useXCoordinate = useXCoordinate;
        }

        public boolean insert(Point2D otherPoint) {
            if (otherPoint.compareTo(this.point) == 0) {
                return false;
            }

            if (goLeft(otherPoint)) {
                if (this.left == null) {
                    this.left = new TreeNode(otherPoint, this.getLeftRect(),
                            !this.useXCoordinate);
                    return true;
                } else {
                    return this.left.insert(otherPoint);
                }
            } else {
                if (this.right == null) {
                    this.right = new TreeNode(otherPoint, this.getRightRect(),
                            !this.useXCoordinate);
                    return true;
                } else {
                    return this.right.insert(otherPoint);
                }
            }
        }

        public TreeNode search(Point2D otherPoint) {
            if (otherPoint.compareTo(this.point) == 0) {
                return this;
            } else {
                if (goLeft(otherPoint)) {
                    if (this.left == null) {
                        return null;
                    } else {
                        return this.left.search(otherPoint);
                    }
                } else {
                    if (this.right == null) {
                        return null;
                    } else {
                        return this.right.search(otherPoint);
                    }
                }
            }
        }

        public Collection<Point2D> range(RectHV otherRect) {
            List<Point2D> results = new ArrayList<Point2D>();

            if (otherRect.contains(this.point)) {
                results.add(this.point);
            }

            if (this.left != null && this.getLeftRect().intersects(otherRect)) {
                results.addAll(this.left.range(otherRect));
            }

            if (this.right != null && this.getRightRect().intersects(otherRect)) {
                results.addAll(this.right.range(otherRect));
            }

            return results;
        }

        public Pair<Double, Point2D> nearest(Point2D otherPoint,
                Pair<Double, Point2D> shortestSoFar) {
            if (this.rect.distanceTo(otherPoint) > shortestSoFar.getX()) {
                // no need to search further
                return shortestSoFar;
            }

            double dist = this.point.distanceTo(otherPoint);

            if (dist < shortestSoFar.getX()) {
                shortestSoFar.setX(dist);
                shortestSoFar.setY(this.point);
            }

            if (this.left != null && this.getLeftRect().contains(otherPoint)) {
                shortestSoFar = this.left.nearest(otherPoint, shortestSoFar);
            }

            if (this.right != null && this.getRightRect().contains(otherPoint)) {
                shortestSoFar = this.right.nearest(otherPoint, shortestSoFar);
            }

            if (this.left != null && !this.getLeftRect().contains(otherPoint)) {
                shortestSoFar = this.left.nearest(otherPoint, shortestSoFar);
            }

            if (this.right != null && !this.getRightRect().contains(otherPoint)) {
                shortestSoFar = this.right.nearest(otherPoint, shortestSoFar);
            }

            return shortestSoFar;
        }

        public void draw() {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            this.point.draw();

            StdDraw.setPenRadius();
            if (this.useXCoordinate) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(this.point.x(), this.rect.ymin(), this.point.x(),
                        this.rect.ymax());
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(this.rect.xmin(), this.point.y(),
                        this.rect.xmax(), this.point.y());
            }

            if (this.left != null) {
                this.left.draw();
            }

            if (this.right != null) {
                this.right.draw();
            }
        }

        private boolean goLeft(Point2D otherPoint) {
            return (this.useXCoordinate && (otherPoint.x() < this.point.x()))
                    || (!this.useXCoordinate && (otherPoint.y() < this.point
                            .y()));
        }

        private RectHV getLeftRect() {
            if (this.useXCoordinate) {
                return new RectHV(this.rect.xmin(), this.rect.ymin(),
                        this.point.x(), this.rect.ymax());
            } else {
                return new RectHV(this.rect.xmin(), this.rect.ymin(),
                        this.rect.xmax(), this.point.y());
            }
        }

        private RectHV getRightRect() {
            if (this.useXCoordinate) {
                return new RectHV(this.point.x(), this.rect.ymin(),
                        this.rect.xmax(), this.rect.ymax());
            } else {
                return new RectHV(this.rect.xmin(), this.point.y(),
                        this.rect.xmax(), this.rect.ymax());
            }
        }

        private Point2D point;
        private RectHV rect;
        private boolean useXCoordinate;
        private TreeNode left, right;
    }

    private class Pair<X, Y> {
        private X x;
        private Y y;

        public Pair(X x, Y y) {
            this.setX(x);
            this.setY(y);
        }

        public X getX() {
            return x;
        }

        public void setX(X x) {
            this.x = x;
        }

        public Y getY() {
            return y;
        }

        public void setY(Y y) {
            this.y = y;
        }
    }

}