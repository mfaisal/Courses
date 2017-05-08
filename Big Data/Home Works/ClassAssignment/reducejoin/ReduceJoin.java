/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package reducejoin;


import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.TreeMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

/**
 *
 * @author Rakib
 */
public class ReduceJoin {

        public static class ReduceJoinMap extends Mapper<LongWritable, Text, TextPair, TextPair>
	{
            @Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
		{
			String tag = "";
                        String[] container = value.toString().split(" |\t");
                
                        if (container.length > 0) {
                           String reduceSideJoinKey = container[0].trim();
                           
                           
                           if (container.length == 2)
                               tag = "0";
                           else
                               tag = "1";
                           
                           int count = 1;
                           String rest = "";
                           while(count < container.length)
                               rest += container[count++] + " ";
                           
                           context.write(new TextPair(reduceSideJoinKey.trim(), tag), new TextPair(rest.trim(), tag));
                               
                        }
                        
                        
                        
		}
	
        }
        public static class PartitionForReducedJoin extends Partitioner<TextPair, TextPair>  
        {

                 @Override
                 public int getPartition(TextPair key, TextPair value, int numPartitions) {
                     return ( key.getFirst().hashCode() & Integer.MAX_VALUE ) % numPartitions ;
                 }
        }

              
       
        public static class ReduceJoinReduce extends Reducer<TextPair, TextPair, Text, Text>
	{
            @Override
		public void reduce(TextPair key, Iterable<TextPair> values, Context context) throws IOException, InterruptedException
		{
                    /* ArrayList of Text type make some major problems*/
			ArrayList <String> T1 = new ArrayList <String>() ;
			String tag = key.getSecond().toString();
			for( TextPair value : values )
			{
				if( value.getSecond().toString().equals(tag))
				{
					T1.add(value.getFirst().toString());
				}
				else
				{
					for(String val:T1)
					{
						context.write( key.getFirst(), new Text(val + "\t"+ value.getFirst().toString()));
					}
				}
			}
	        }
	 }
	    
            
            public static void main(String[] args) throws Exception {
	      
            
            Configuration conf = new Configuration();
            
            
            if (args.length != 2) {
              System.err.println("Usage: whitehouse <in> <out>");
              System.exit(2);
            }
            
            Job reduceJoinJob = new Job(conf, "Reduced Side Join");
            reduceJoinJob.setJarByClass(ReduceJoin.class);
            reduceJoinJob.setMapperClass(ReduceJoinMap.class);
            reduceJoinJob.setPartitionerClass(PartitionForReducedJoin.class);
            reduceJoinJob.setGroupingComparatorClass(TextPair.FirstComparator.class);
            reduceJoinJob.setReducerClass(ReduceJoinReduce.class);
            
            reduceJoinJob.setMapOutputKeyClass(TextPair.class);
            reduceJoinJob.setMapOutputValueClass(TextPair.class);
            
            reduceJoinJob.setOutputKeyClass(Text.class);
            reduceJoinJob.setOutputValueClass(Text.class);
            
            
            FileInputFormat.addInputPath(reduceJoinJob, new Path(args[0]));
            FileOutputFormat.setOutputPath(reduceJoinJob, new Path(args[1]));

            System.exit(reduceJoinJob.waitForCompletion(true) ? 0 : 1);            
            
            
            	        
		/*Job job = new Job(conf, "JoinReduceSide");
		     
		job.setMapOutputKeyClass(TextPair.class);
		job.setMapOutputValueClass(TextPair.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		         
		job.setJarByClass(ReduceJoin.class);
		job.setMapperClass(ReduceJoinMap.class);
		job.setReducerClass(ReduceJoinReduce.class);
		job.setPartitionerClass(PartitionForReducedJoin.class);
		job.setGroupingComparatorClass(TextPair.FirstComparator.class);
		         
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		         
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		        
		job.waitForCompletion(true);
                */
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
	    }
    }
