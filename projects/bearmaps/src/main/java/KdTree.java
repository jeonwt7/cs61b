import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class KdTree {

    ArrayList<Point2D> pointList;
    Node root;
    Map<Point2D, Long> pointID;

    KdTree(Map<Long, Vertex> idVertex) {
        ArrayList<Point2D> list = new ArrayList<>();
        Map<Point2D, Long> dict = new HashMap<>();
        for (long id : idVertex.keySet()) {
            Point2D point = new Point2D.Double(
                    GraphDB.projectToX(idVertex.get(id).lon, idVertex.get(id).lat),
                    GraphDB.projectToY(idVertex.get(id).lon, idVertex.get(id).lat));
            list.add(point);
            dict.put(point, id);
        }
        this.root = make(list, 0);
        this.pointList = list;
        this.pointID = dict;
    }

    Node make(ArrayList<Point2D> list, int depth) {
        if (list.size() == 1) {
            return new Node(list.get(0), null, null);
        } else if (depth % 2 == 0) {
            list.sort(Comparator.comparingDouble(Point2D::getX));
        } else {
            list.sort(Comparator.comparingDouble(Point2D::getY));
        }
        ArrayList<Point2D> before = new ArrayList<>(list.subList(0, list.size() / 2));
        ArrayList<Point2D> after = new ArrayList<>(list.subList(list.size() / 2 + 1, list.size()));
        if (before.isEmpty()) {
            return new Node(list.get(list.size() / 2), null, make(after, depth + 1));
        } else if (after.isEmpty()) {
            return new Node(list.get(list.size() / 2), make(before, depth + 1), null);
        } else {
            return new Node(list.get(list.size() / 2), make(before, depth + 1),
                    make(after, depth + 1));
        }
    }

    long nearest(double x, double y) {
        return pointID.get(root.closest(x, y, 0));
    }

    public class Node {
        Point2D location;
        Node left; // root of left subtree
        Node right;

        public Node(Point2D location, Node left, Node right) {
            this.location = location;
            this.left = left;
            this.right = right;
        }

        Point2D closest(double x, double y, int depth) {  // x and y should be the "projected"
            Point2D query = new Point2D.Double(x, y);
            Point2D best;
            Point2D consider;
            if (left == null && right == null) {
                return location;
            } else if (left == null) {
                best = location;
                consider = right.closest(x, y, depth + 1);
                if (query.distance(best) < query.distance(consider)) {
                    return best;
                } else {
                    return consider;
                }
            } else if (right == null) {
                best = location;
                consider = left.closest(x, y, depth + 1);
                if (query.distance(best) < query.distance(consider)) {
                    return best;
                } else {
                    return consider;
                }
            } else {
                if (depth % 2 == 0) { // sorted by x-axis
                    if (x < location.getX()) { // located left of the axis
                        best = left.closest(x, y, depth + 1);
                        if (query.distance(best) > location.getX() - query.getX()) {
                            consider = right.closest(x, y, depth + 1);
                            if (query.distance(location) < query.distance(consider)) {
                                consider = location;
                            }
                            if (query.distance(consider) < query.distance(best)) {
                                return consider;
                            }
                        }
                        return best;
                    } else { // located right of the axis
                        best = right.closest(x, y, depth + 1);
                        if (query.distance(best) > query.getX() - location.getX()) {
                            consider = left.closest(x, y, depth + 1);
                            if (query.distance(location) < query.distance(consider)) {
                                consider = location;
                            }
                            if (query.distance(consider) < query.distance(best)) {
                                return consider;
                            }
                        }
                        return best;
                    }
                } else { // sorted by y-axis
                    if (y < location.getY()) { // query located below the axis
                        best = left.closest(x, y, depth + 1);
                        if (query.distance(best) > location.getY() - query.getY()) {
                            consider = right.closest(x, y, depth + 1);
                            if (query.distance(location) < query.distance(consider)) {
                                consider = location;
                            }
                            if (query.distance(consider) < query.distance(best)) {
                                return consider;
                            }
                        }
                        return best;
                    } else { // query located above the axis
                        best = right.closest(x, y, depth + 1);
                        if (query.distance(best) > query.getY() - location.getY()) {
                            consider = left.closest(x, y, depth + 1);
                            if (query.distance(location) < query.distance(consider)) {
                                consider = location;
                            }
                            if (query.distance(consider) < query.distance(best)) {
                                return consider;
                            }
                        }
                        return best;
                    }
                }
            }
        }
    }
}
