package bearmaps.proj2c;

import bearmaps.hw4.streetmap.Node;
import bearmaps.hw4.streetmap.StreetMapGraph;
import bearmaps.lab9.MyTrieSet;
import bearmaps.lab9.TrieSet61B;
import bearmaps.proj2ab.Point;
import bearmaps.proj2ab.WeirdPointSet;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {

    private WeirdPointSet weirdPointSet;
    private Map<Point, Long> pointToNode;
    private Map<String, List<Node>> locationMap;
    private TrieSet61B trieSet61B;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // Part II Routing
        List<Node> nodes = this.getNodes();
        this.pointToNode = new HashMap<>();
        nodes.stream()
                .filter(node -> !neighbors(node.id()).isEmpty())
                .forEach(node -> pointToNode.put(new Point(node.lon(), node.lat()), node.id()));

        List<Point> points = new ArrayList<>(pointToNode.keySet());
        this.weirdPointSet = new WeirdPointSet(points);
        // Part III Autocomplete
        this.trieSet61B = new MyTrieSet();
        List<Node> placeNodes = nodes.stream()
                .filter(node -> neighbors(node.id()).isEmpty())
                .collect(Collectors.toList());

        List<String> places = placeNodes.stream()
                .map(node -> name(node.id()))
                .collect(Collectors.toList());
        for(String place : places) {
            trieSet61B.add(place);
        }
        // Part III Search
        this.locationMap = new HashMap<>();
        for(Node placeNode : placeNodes) {
            String placeName = name(placeNode.id());
            if(!locationMap.containsKey(placeName)) {
                locationMap.put(placeName, new ArrayList<>());
            }
            locationMap.get(placeName).add(placeNode);
        }
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        return pointToNode.get(this.weirdPointSet.nearest(lon, lat));
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        return this.trieSet61B.keysWithPrefix(prefix);
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        List<Map<String, Object>> res = new ArrayList<>();
        List<Node> targetLocationNodes = this.locationMap.get(locationName);
        if(targetLocationNodes != null && !targetLocationNodes.isEmpty()) {
            targetLocationNodes
                    .forEach(node -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("lat", node.lat());
                        map.put("lon", node.lon());
                        map.put("name", name(node.id()));
                        map.put("id", node.id());
                        res.add(map);
                    });
        }
        return res;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
