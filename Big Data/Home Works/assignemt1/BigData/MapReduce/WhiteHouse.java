import java.io.IOException;
import java.util.TreeMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


/**
 *
 * @author Rakib
 */
public class WhiteHouse {

            public static class WhiteHouseCountMap extends Mapper<Object, Text, Text, IntWritable>
            {
	      private final static IntWritable one = new IntWritable(1);
	      private Text word = new Text();
	
              @Override
              public void map(Object key, Text value, org.apache.hadoop.mapreduce.Mapper.Context context) throws IOException, InterruptedException {
              
              String[] container = value.toString().split(",");
                
                //StringTokenizer tokenizer = new StringTokenizer(line, ",");
	       if (container.length > 0) {
	          String firstName = container[1].trim();
                  String lastName = container[0].trim();
                  
                  if (!"NAMELAST".equals(lastName) && !"NAMEFIRST".equals(firstName)) {
                        String name = lastName + "\t" + firstName;
                        word.set(name);
                        context.write(word, one);
                    }
	        }
	      }
	    }
	
	    
            public static class WhiteHouseCountReduce extends Reducer<Text, IntWritable, Text, IntWritable> {
             @Override
	      public void reduce(Text key, Iterable<IntWritable> values, Context context
                       ) throws IOException, InterruptedException {
	        int sum = 0;
	        for (IntWritable val : values) {
                    sum += val.get();
                  }
                
	        context.write(key, new IntWritable(sum));
	      }
	    }
	    
            
            public static class WhiteHouseCountTopMap extends Mapper<Object, Text, NullWritable, Text> {
	      
                
              private TreeMap<Integer, Text> visitorToRecordMap = new TreeMap<Integer, Text>();
	      	
              @Override
	      public void map(Object key, Text value, org.apache.hadoop.mapreduce.Mapper.Context context) throws IOException, InterruptedException {
	      String[] container = value.toString().split("\t");
                
               if (container.length > 0) 
               {
	          
                   String containerValue = container[2].trim() + "\t" + container[0].trim() + "\t" + container[1].trim();
                   
                   visitorToRecordMap.put(Integer.parseInt(container[2].trim()), new Text(containerValue));
                  
                  if (visitorToRecordMap.size() > 10) 
                  {
                      visitorToRecordMap.remove(visitorToRecordMap.firstKey());
                  }
               }
	    }
              
              @Override
              protected void cleanup(org.apache.hadoop.mapreduce.Mapper.Context context) throws IOException,
                InterruptedException {
                // Output our ten records to the reducers with a null key
                for (Text t : visitorToRecordMap.values()) {
                    
                   context.write(NullWritable.get(), t);
                   
                }
                }
            }
            
            public static class WhiteHouseCountTopReduce extends Reducer<NullWritable, Text, NullWritable, Text> {
              
            private TreeMap<Integer, Text> visitorToRecordMap = new TreeMap<Integer, Text>();
              
            public void reduce(NullWritable key, Iterable<Text> values, Reducer.Context context
                       ) throws IOException, InterruptedException {
	      
              for (Text val : values)
              {
             
              String value = val.toString();    
              String[] container = value.split("\t");
                
               //StringTokenizer tokenizer = new StringTokenizer(line, ",");
	       if (container.length > 0) 
               {
	          String count = container[0].trim(); 
                  visitorToRecordMap.put(Integer.parseInt(count), new Text(value));
                  
                  if (visitorToRecordMap.size() > 10) 
                  {
                      visitorToRecordMap.remove(visitorToRecordMap.firstKey());
                  }
               }   
              }   
               
               
               for (Text t : visitorToRecordMap.values()) {
                   //System.out.println(t.toString()); 
                   context.write(NullWritable.get(), t);
                }
	      }
	    }
            
            public static void main(String[] args) throws Exception {
	      
            
            Configuration conf = new Configuration();
            
            
            if (args.length != 3) {
              System.err.println("Usage: whitehouse <in> <temp> <out>");
              System.exit(2);
            }
            
            FileSystem fs = FileSystem.get(conf);
	    fs.delete(new Path(args[1]), true);
	    fs.delete(new Path(args[2]), true);
            
            Job countingJob = new Job(conf, "whitehouse_count");
            countingJob.setJarByClass(WhiteHouse.class);
            countingJob.setMapperClass(WhiteHouseCountMap.class);
            countingJob.setCombinerClass(WhiteHouseCountReduce.class);
            countingJob.setReducerClass(WhiteHouseCountReduce.class);
            
            countingJob.setOutputKeyClass(Text.class);
            countingJob.setOutputValueClass(IntWritable.class);
            
            
            FileInputFormat.addInputPath(countingJob, new Path(args[0]));
            FileOutputFormat.setOutputPath(countingJob, new Path(args[1]));

            if (countingJob.waitForCompletion(true))
            {
                Job countingTopJob = new Job(conf, "whitehouse_top");
                countingTopJob.setJarByClass(WhiteHouse.class);
                countingTopJob.setMapperClass(WhiteHouseCountTopMap.class);
                countingTopJob.setCombinerClass(WhiteHouseCountTopReduce.class);
                countingTopJob.setReducerClass(WhiteHouseCountTopReduce.class);

                countingTopJob.setOutputKeyClass(NullWritable.class);
                countingTopJob.setOutputValueClass(Text.class);


                FileInputFormat.addInputPath(countingTopJob, new Path(args[1]));
                FileOutputFormat.setOutputPath(countingTopJob, new Path(args[2]));
                
                System.exit(countingTopJob.waitForCompletion(true) ? 0 : 1);
            }
            
	    }
}
