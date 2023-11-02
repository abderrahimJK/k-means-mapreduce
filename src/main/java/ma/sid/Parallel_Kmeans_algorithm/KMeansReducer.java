package ma.sid.Parallel_Kmeans_algorithm;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class KMeansReducer extends Reducer<Text, Text, Text, Text> {
    private Text newCentroid = new Text();
    @Override
    protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, Text, Text>.Context context) throws IOException, InterruptedException {

        double sumX = 0.0;
        double sumY = 0.0;
        int count = 0;

        for (Text value : values) {
            String[] tokens = value.toString().split(",");
            sumX += Double.parseDouble(tokens[0]);
            sumY += Double.parseDouble(tokens[1]);
            count++;
        }

        double newX = sumX / count;
        double newY = sumY / count;
        newCentroid.set(newX + "," + newY);
        context.write(key, newCentroid);
    }
}
