package ma.sid.Parallel_Kmeans_algorithm;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KMeansMapper extends Mapper<LongWritable, Text,Text,Text> {

    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] tokens = line.split(",");

        if (tokens.length >= 2) {
            try {
                double x = Double.parseDouble(tokens[0]);
                double y = Double.parseDouble(tokens[1]);

                String nearestCentroid = closestCentroid(x,y);
                context.write(new Text(nearestCentroid), new Text(x+","+y));
            } catch (Exception e) {
               e.printStackTrace();
            }
        } else {
            System.out.println("[ length < 2 ]");
        }
    }

    private String closestCentroid(double x, double y ){
        double minimumDistance = Double.MAX_VALUE;
        String nearest = null;
        double currentDist = 0;
        String[] centroids = "18.070082172708354,87.71180266095584;17.496235883190796,32.24014953962695;70.73558741764516,47.09561764565555".split(";");
        for(String c : centroids){
            System.out.println("[#]|_______"+c);
            String[] centroid = c.split(",");
            currentDist = Math.sqrt(
                    Math.pow(Double.parseDouble(centroid[0])-x, 2)+Math.pow(Double.parseDouble(centroid[1])-y, 2
                    ));
            if(currentDist< minimumDistance ){
                minimumDistance = currentDist;
                nearest = c;
            }
            System.out.println("[*]|_______"+nearest);
        }
        return nearest;
    }
}




