package ma.sid.Sequance_KMeans_Algorithm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Map;

@Data @AllArgsConstructor @ToString @EqualsAndHashCode
public class Centroid {

    private final Map<String, Double> coordinates;

}
