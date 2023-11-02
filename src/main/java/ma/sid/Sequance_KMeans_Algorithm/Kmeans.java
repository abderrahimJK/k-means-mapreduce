package ma.sid;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class Kmeans {

    public Kmeans() {
    }

    private final  Random random = new Random();

    public Map<Centroid, List<Point>> fit(List<Point> points, int k , Distance distance, int maxIterations){
        List<Centroid> centroids = randomCentroids(points, k);
        Map<Centroid, List<Point>> clusters = new HashMap<>();
        Map<Centroid, List<Point>> lastState = new HashMap<>();

        // iterate for a pre-defined number of times
        for (int i = 0; i < maxIterations; i++) {
            boolean isLastIteration = i == maxIterations - 1;

            // in each iteration we should find the nearest centroid for each record
            for (Point record : points) {
                Centroid centroid = nearestCentroid(record, centroids, distance);
                assignToCluster(clusters, record, centroid);
            }

            // if the assignments do not change, then the algorithm terminates
            boolean shouldTerminate = isLastIteration || clusters.equals(lastState);
            lastState = clusters;
            if (shouldTerminate) {
                break;
            }

            // at the end of each iteration we should relocate the centroids
            centroids = relocateCentroids(clusters);
            clusters = new HashMap<>();
        }

        return lastState;
    }

    private List<Centroid> randomCentroids(List<Point> pointes, int k) {
        List<Centroid> centroids = new ArrayList<>();
        Map<String, Double> maxs = new HashMap<>();
        Map<String, Double> mins = new HashMap<>();

        for (Point point : pointes) {
            point.getCoordinates().forEach((key, value) -> {
                // compares the value with the current max and choose the bigger value between them
                maxs.compute(key, (k1, max) -> max == null || value > max ? value : max);

                // compare the value with the current min and choose the smaller value between them
                mins.compute(key, (k1, min) -> min == null || value < min ? value : min);
            });
        }

        Set<String> attributes = pointes.stream()
                .flatMap(e -> e.getCoordinates().keySet().stream())
                .collect(toSet());
        for (int i = 0; i < k; i++) {
            Map<String, Double> coordinates = new HashMap<>();
            for (String attribute : attributes) {
                double max = maxs.get(attribute);
                double min = mins.get(attribute);
                coordinates.put(attribute, random.nextDouble() * (max - min) + min);
            }

            centroids.add(new Centroid(coordinates));
        }

        return centroids;
    }

    private Centroid nearestCentroid(Point record, List<Centroid> centroids, Distance distance) {
        double minimumDistance = Double.MAX_VALUE;
        Centroid nearest = null;

        for (Centroid centroid : centroids) {
            double currentDistance = distance.calculate(record.getCoordinates(), centroid.getCoordinates());

            if (currentDistance < minimumDistance) {
                minimumDistance = currentDistance;
                nearest = centroid;
            }
        }

        return nearest;
    }

    private void assignToCluster(Map<Centroid, List<Point>> clusters, Point point, Centroid centroid) {
        clusters.compute(centroid, (key, list) -> {
            if (list == null) {
                list = new ArrayList<>();
            }

            list.add(point);
            return list;
        });
    }

    private Centroid average(Centroid centroid, List<Point> pointes) {
        if (pointes == null || pointes.isEmpty()) {
            return centroid;
        }

        Map<String, Double> average = centroid.getCoordinates();
        pointes.stream().flatMap(e -> e.getCoordinates().keySet().stream())
                .forEach(k -> average.put(k, 0.0));

        for (Point record : pointes) {
            record.getCoordinates().forEach(
                    (k, v) -> average.compute(k, (k1, currentValue) -> v + currentValue)
            );
        }

        average.forEach((k, v) -> average.put(k, v / pointes.size()));

        return new Centroid(average);
    }

    private List<Centroid> relocateCentroids(Map<Centroid, List<Point>> clusters) {
        return clusters.entrySet().stream().map(e -> average(e.getKey(), e.getValue())).collect(toList());
    }
}
