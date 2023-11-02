package ma.sid;

import lombok.*;

import java.util.Map;

@Data @AllArgsConstructor @NoArgsConstructor @ToString @EqualsAndHashCode
public class Point {
    private String point;
    private Map<String, Double> Coordinates;
}
