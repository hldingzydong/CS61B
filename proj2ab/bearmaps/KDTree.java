package bearmaps;

import java.util.List;
import java.util.Objects;

import static bearmaps.Point.distance;

public class KDTree implements PointSet {
    private static final boolean HORIZONTAL = true;
    private static final boolean VERTICAL = false;

    private KDTreeNode root;

    public KDTree(List<Point> points) {
        for (Point point : points) {
            root = insert(root, point, HORIZONTAL);
        }
    }

    private KDTreeNode insert(KDTreeNode node, Point p, boolean parentDir) {
        if(node == null) {
            return new KDTreeNode(p, !parentDir);
        }
        if(!node.p.equals(p)) {
            if(node.vertical == VERTICAL) {
                double nodeX = node.p.getX();
                if(p.getX() <= nodeX) {
                    node.left = insert(node.left, p, node.vertical);
                } else {
                    node.right = insert(node.right, p, node.vertical);
                }
            } else {
                double nodeY = node.p.getY();
                if(p.getY() <= nodeY) {
                    node.left = insert(node.left, p, node.vertical);
                } else {
                    node.right = insert(node.right, p, node.vertical);
                }
            }
        }
        return node;
    }

    @Override
    public Point nearest(double x, double y) {
        return nearest(root, new Point(x, y), root).p;
    }

    private KDTreeNode nearest(KDTreeNode node, Point goal, KDTreeNode best) {
        if(node == null) {
            return best;
        }
        if(distance(node.p, goal) < distance(best.p, goal)) {
            best = node;
        }
        KDTreeNode goodSide;
        KDTreeNode badSide;
        if(node.vertical == VERTICAL) {
            if(goal.getX() <= node.p.getX()) {
                goodSide = node.left;
                badSide = node.right;
            } else {
                goodSide = node.right;
                badSide = node.left;
            }
        } else {
            if(goal.getY() <= node.p.getY()) {
                goodSide = node.left;
                badSide = node.right;
            } else {
                goodSide = node.right;
                badSide = node.left;
            }
        }
        best = nearest(goodSide, goal, best);
        // 1) create a best point from badSide, just use one dimension
        // 2) compare distance(bestBadPoint, goal) & distance(best, goal)
        if(badSide != null) {
            double bestBadSideDistance = findBestBadSideDistance(node, goal);
            if(bestBadSideDistance <= distance(best.p, goal)) {
                best = nearest(badSide, goal, best);
            }
        }
        return best;
    }

    private double findBestBadSideDistance(KDTreeNode node, Point goal) {
        if(node.vertical == VERTICAL) {
            return Math.pow(node.p.getX() - goal.getX(), 2);
        } else {
            return Math.pow(node.p.getY() - goal.getY(), 2);
        }
    }

    private static class KDTreeNode {
        Point p;
        boolean vertical;
        KDTreeNode left;
        KDTreeNode right;

        KDTreeNode(Point p, boolean vertical) {
            this.p = p;
            this.vertical = vertical;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            KDTreeNode that = (KDTreeNode) o;
            return vertical == that.vertical &&
                    Objects.equals(p, that.p);
        }

        @Override
        public int hashCode() {
            return Objects.hash(p, vertical);
        }
    }
}
