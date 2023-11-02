package ma.sid;

import ma.sid.Parallel_Kmeans_algorithm.KMeansMapper;
import ma.sid.Parallel_Kmeans_algorithm.KMeansReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

//        List<Point> points = new ArrayList<>();
//        for (int i = 0 ; i<100 ; i++){
//            Point point = new Point();
//            point.setPoint("P"+(i+1));
//            Map<String, Double> coordinates = new HashMap<>();
//            Random random = new Random();
//            double x = random.nextDouble() * 100; // Random x-coordinate between 0 and 100
//            double y = random.nextDouble() * 100; // Random y-coordinate between 0 and 100
//
//            coordinates.put("x", x);
//            coordinates.put("y", y);
//            point.setCoordinates(coordinates);
//            points.add(point);
//        }
//
//        for (Point p : points){
//            System.out.println("[ "+p.getPoint()+" : "+p.getCoordinates()+ " ]");
//        }
//
//        Kmeans kmeans = new Kmeans();
//        Map<Centroid, List<Point>> clusters = kmeans.fit(points, 3, new EuclideanDistance(), 100);
//        clusters.forEach((key, value) -> {
//            System.out.println("-------------------------- CLUSTER ----------------------------");
//
//            // Sorting the coordinates to see the most significant tags first.
//            String members = String.join(", ", value.stream().map(Point::getPoint).collect(toSet()));
//            System.out.print(members);
//
//            System.out.println();
//            System.out.println();
//        });


//        String filePath = "./src/main/resources/data.txt"; // Provide the correct file path here
//        try {
//            String data = loadDataFromFile(filePath);
//            String[] lines = data.split("\n");
//            List<String> dataList = new ArrayList<>(Arrays.asList(lines));
//            Centroids.CENTROIDS = getRandomElements(dataList, 3);
//            System.out.println(Centroids.CENTROIDS);
//        } catch (IOException e) {
//            System.err.println("Error reading the file: " + e.getMessage());
//        }

        Configuration conf=new Configuration();
        Job job=Job.getInstance(conf);
        //Mapper & Reduce Class
        job.setJarByClass(Main.class);
        job.setMapperClass(KMeansMapper.class);
        job.setReducerClass(KMeansReducer.class);
        //The Mapper output Format
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        //the Job output Format
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        //The input Format
        job.setInputFormatClass(TextInputFormat.class);
        //input/output path files (command line)
        FileInputFormat.addInputPath(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]));

        job.waitForCompletion(true);
    }

    public static String loadDataFromFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }

        return content.toString();
    }

    public static List<String> getRandomElements(List<String> elements, int k){

        List<String> randomElements = new ArrayList<>();
        Random random = new Random();

        List<String> tmpList = new ArrayList<>(elements);
        for(int i=0 ; i<k ; i++){
            if(!tmpList.isEmpty()){
                int randomIndex = random.nextInt(tmpList.size());
                randomElements.add(tmpList.remove(randomIndex));
            }else{
                break;
            }
        }
        return randomElements;
    }
}