package ma.sid.Sequance_KMeans_Algorithm;

import java.util.Map;

public interface Distance {

    double calculate(Map<String, Double> p1, Map<String, Double> p2);
}
