import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.IntSumReducer;



public class WordCounter {
	
	// My Mapper class
		public static class mapper extends Mapper<Text,BytesWritable,Text,IntWritable> {
			
			public void map(Text key, BytesWritable value, Context output) throws IOException, InterruptedException {
				// TODO Auto-generated method stub
				Hashtable<String,Integer> dic=new Hashtable<String,Integer>();
				String content = new String( value.getBytes(), "UTF-8" );
				StringTokenizer words = new StringTokenizer(content); // Split the text into words
				while(words.hasMoreTokens()) { // For each word
					String str = words.nextToken();
					if(dic.containsKey(str)) {
						dic.put(str,dic.get(str)+1);
					}
					else {
						dic.put(str,1);
					}
				}
				Enumeration<String> _enum = dic.keys();
				while(_enum.hasMoreElements()) {
					String k = _enum.nextElement();
					output.write(new Text(k), new IntWritable(dic.get(k))); // add the tuple (<word>,1) to the output
				}
			}
		}
		
		// My reducer class
		public static class reducer extends Reducer< Text, IntWritable, Text, IntWritable> {

			public void reduce(Text key, Iterator<IntWritable> value, Context output) throws IOException, InterruptedException {
				// TODO Auto-generated method stub
				int cnt = 0;
				while(value.hasNext()) { // For each element in the list
					cnt+=value.next().get(); // add one
				}
				output.write(key, new IntWritable(cnt));
			}
		}
	
	
	public static void main(String args[]) throws Exception {
	    Configuration conf = new Configuration();
	    Job job = Job.getInstance(conf, "word count");
	    job.setJarByClass(WordCounter.class);
	    job.setMapperClass(mapper.class);
	    job.setCombinerClass(reducer.class);
	    job.setReducerClass(reducer.class);
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(IntWritable.class);
	    job.setInputFormatClass(ZipFileInputFormat.class);
	    // We want to be fault-tolerant	
	    ZipFileInputFormat.setLenient( true );
	    ZipFileInputFormat.addInputPath(job, new Path(args[0]));
	    FileOutputFormat.setOutputPath(job, new Path(args[1]));

	    

	    
	    
	    System.exit(job.waitForCompletion(true) ? 0 : 1);
	   } 

}
