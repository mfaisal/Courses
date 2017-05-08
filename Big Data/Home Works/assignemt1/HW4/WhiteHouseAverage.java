/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
/**
 *
 * @author Rakib
 */
public class WhiteHouseAverage {

  public static class  CountAverageTuple implements Writable
  {
    public int TotalOfVisitorInAMonth;
    public int VisitedYear;
    
    public CountAverageTuple(int totalOfVisitorInMonth, int visitedYear)
    {
      this.TotalOfVisitorInAMonth = totalOfVisitorInMonth;
      this.VisitedYear = visitedYear;
    }
    
    public CountAverageTuple()
    {
      this.TotalOfVisitorInAMonth = 0;
      this.VisitedYear = 0;      
    }
    
    @Override
    public void write(DataOutput d) throws IOException {
        d.writeInt(TotalOfVisitorInAMonth);  
        d.writeInt(VisitedYear);        
    }

    @Override
    public void readFields(DataInput di) throws IOException {
        TotalOfVisitorInAMonth = di.readInt();
        VisitedYear = di.readInt();
    }
  }
    
    
  public static class AverageMapper extends
        Mapper<Object, Text, IntWritable, CountAverageTuple> 
   {
        //private CountAverageTuple outCountAverage = null;
        private final static SimpleDateFormat frmt = new SimpleDateFormat("MM/dd/yy HH:mm");
        //DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
	 
        
      @Override
        public void map(Object key, Text value, Context context)
        throws IOException, InterruptedException {
        
            
        String[] container = value.toString().split(",");
                
        if (container.length > 10) 
        {
	        String appointmentStartDate = container[11].trim();
                if (!"APPT_START_DATE".equals(appointmentStartDate)) 
                {
                    try 
                    {
                           //Date visitedDate = df.parse(appointmentStartDate);
                           Date visitedDate = frmt.parse(appointmentStartDate);
                           Calendar cal = Calendar.getInstance();
                           cal.setTime(visitedDate);
                           
                           int month = cal.get(Calendar.MONTH);
                           CountAverageTuple countAverageTuple = new CountAverageTuple(1, cal.get(Calendar.YEAR));
                           context.write(new IntWritable(month), countAverageTuple);
            
                     
                    }
                    catch (ParseException ex) 
                    {
                        System.out.println("Parsing error : " + appointmentStartDate);
                        
                    }
                    catch (NumberFormatException ex) 
                    {
                      System.out.println("Number format error : " + appointmentStartDate);
                    }
                    catch (Exception ex) 
                    {
                        ex.printStackTrace();
                        throw new IllegalArgumentException("Illegall exception");                     
                    }
                    
                }
              }
        }
    }
	
	    
      public static class AverageReducer extends Reducer<IntWritable, CountAverageTuple, Text, Text> 
      {
        
        public String[] month = {"January", "February", "March", "April", "May", "June", "July",
                                 "August", "September", "October", "November", "December"};
        
        @Override
        public void reduce(IntWritable key, Iterable<CountAverageTuple> values, Context context)
                throws IOException, InterruptedException 
        {
            int sum = 0;
          try
          {              
            List<Integer> visitedYears = new ArrayList<Integer>();
            // Iterate through all input values for this key
            
            for (CountAverageTuple val : values) {
                
              //String value = val.toString();    
              //String[] container = value.split(",");
              sum += val.TotalOfVisitorInAMonth;
              int year = val.VisitedYear;
              
              if (!visitedYears.contains(year)) {
                    visitedYears.add(year);
                }
              
              }
          
            double average = (double)sum / visitedYears.size(); 
            
            String avegareInYears = "";
            
            Collections.sort(visitedYears);
            for (Iterator<Integer> it = visitedYears.iterator(); it.hasNext();) {
                    int year = it.next();
                    //avegareInYears += year + " ";
               }
            
            DecimalFormat format = new DecimalFormat("##.##");
            context.write(new Text(month[key.get()]), new Text(format.format(average) + ""));
         }
            catch (Exception ex)
              {
                  ex.printStackTrace();
                  throw new IllegalArgumentException("Parsing error");              
              }
        }
     }
            
     public static void main(String[] args) throws Exception {
	
         Configuration conf = new Configuration();
            
            
         if (args.length != 2) {
            System.err.println("Usage: whitehouse <in> <out>");
            System.exit(2);
          }

          Job countingJob = new Job(conf, "whitehouse_count");
          countingJob.setJarByClass(WhiteHouseAverage.class);
          countingJob.setMapperClass(AverageMapper.class);
          //countingJob.setCombinerClass(AverageReducer.class);
          countingJob.setReducerClass(AverageReducer.class);
          
          countingJob.setMapOutputKeyClass(IntWritable.class);
          countingJob.setMapOutputValueClass(CountAverageTuple.class);
          
          countingJob.setOutputKeyClass(Text.class);
          countingJob.setOutputValueClass(Text.class);


          FileInputFormat.addInputPath(countingJob, new Path(args[0]));
          FileOutputFormat.setOutputPath(countingJob, new Path(args[1]));

          System.exit(countingJob.waitForCompletion(true) ? 0 : 1);
        }
}
    