/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mapjoin;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


public class MapJoin 
{	
	public static class Map extends Mapper<LongWritable, Text, Text, Text>
	{
		public static HashMap< String , ArrayList < String > > smallRelationMap;
		public FSDataInputStream in;
		public BufferedReader br;
		
		protected void setup(org.apache.hadoop.mapreduce.Mapper.Context context) throws IOException, InterruptedException
		{
			//Read the broadcasted file
			 try 
			 {
				 FileSystem fs = FileSystem.get(context.getConfiguration());
				 URI files[]=DistributedCache.getCacheFiles(context.getConfiguration());
				 org.apache.hadoop.fs.Path path= new org.apache.hadoop.fs.Path(files[0].toString());
				 in = fs.open(path);
				 br  = new BufferedReader(new InputStreamReader(in));
				
			 } 
			 catch (FileNotFoundException e1) 
			 {	  
				 e1.printStackTrace();
				 System.out.println("read from distributed cache: file not found!");
			 } 
			 catch (IOException e1) 
			 {
				 e1.printStackTrace();
				 System.out.println("read from distributed cache: IO exception!");
			 }
			 //Hashtable to store the tuples
			smallRelationMap = new HashMap < String , ArrayList < String > >() ;
			String line = null;
			try
			{			
				while(( line = br . readLine () ) !=null)
				{
					String record [] = line . split ("\t") ;
					if( record . length == 2)
					{
						//Insert into Hashtable
						if( smallRelationMap.containsKey ( record [0]) )
						{
							smallRelationMap.get( record [0]).add( record [1]) ;
						}
						else
						{
							ArrayList < String > value = new ArrayList <String >() ;
							value . add ( record [1]) ;
							smallRelationMap.put ( record [0] , value );
						}
					}
				}
			}
			catch( Exception e)								
			{	
				e. printStackTrace () ;			
			}	
		}
		
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
		{
			StringTokenizer strTok = new StringTokenizer(value.toString(), " |\t");
			String joinKey = strTok.nextToken().trim();
			String restOfTuple = "";
			while(strTok.hasMoreTokens())
			{
				restOfTuple += strTok.nextToken()+" ";
			}
			
			if(MapJoin.Map.smallRelationMap.containsKey(joinKey))
			{
				for( String leftRecord : MapJoin.Map.smallRelationMap.get ( joinKey) )					
				{							
					context.write(new Text(joinKey) , new Text(leftRecord + "\t" + restOfTuple));
				}
			}
		}
	}
	
	public static void main(String[] args) throws Exception 
	{
		Configuration conf = new Configuration();
		DistributedCache.addCacheFile(new URI(args[1]), conf);       
		Job job = new Job(conf, "MapJoin");
		     
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		         
		job.setJarByClass(MapJoin.class);
		job.setMapperClass(Map.class);
	
		         
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		         
		FileInputFormat.addInputPath(job, new  org.apache.hadoop.fs.Path(args[0]));
		FileOutputFormat.setOutputPath(job, new org.apache.hadoop.fs.Path(args[2]));                
                        
		job.waitForCompletion(true);
	}
}
