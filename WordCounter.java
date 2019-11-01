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
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.IntSumReducer;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;



public class WordCounter {
	
    // My Mapper class
    public static class MyMapper
	extends Mapper<Text,BytesWritable,Text,IntWritable>{
	
	private final static IntWritable one = new IntWritable( 1 );
	private Text word = new Text();
			
	public void map( Text key, BytesWritable value, Context context )
	    throws IOException, InterruptedException {		
	    // Prepare the content 
	    String content = new String( value.getBytes(), "UTF-8" );
		
	    // Clean
	    content = content.replaceAll( "[^A-Za-z \n]", "" ).toLowerCase();
		
	    // Tokenize the content
	    StringTokenizer tokenizer = new StringTokenizer( content );
	    while ( tokenizer.hasMoreTokens() )
		{
		    word.set( tokenizer.nextToken() );
		    context.write( word, one );
		}
	}
    }
		
    // My reducer class
    public static class MyReducer
	extends Reducer<Text,IntWritable,Text,IntWritable> {
	private IntWritable result = new IntWritable();
		
	public void reduce(Text key, Iterable<IntWritable> values,
			   Context context
			   ) throws IOException, InterruptedException {
	    int sum = 0;
	    for (IntWritable val : values) {
		sum += val.get();
	    }
	    result.set(sum);
	    context.write(key, result);
	}
    }
		
    public static void main(String[] args) throws Exception {
	Configuration conf = new Configuration();
	    
	Job job = Job.getInstance(conf, "word count");
	System.out.println(conf);
	job.setJarByClass(WordCounter.class);
	    
	job.setMapperClass(MyMapper.class);
	job.setReducerClass(MyReducer.class);

	job.setInputFormatClass(ZipFileInputFormat.class);
	job.setOutputFormatClass(TextOutputFormat.class);

	job.setOutputKeyClass(Text.class);
	job.setOutputValueClass(IntWritable.class);
	    
	// We want to be fault-tolerant	
	ZipFileInputFormat.setLenient( true );
	    
	ZipFileInputFormat.setInputPaths(job, new Path(args[0]));
	TextOutputFormat.setOutputPath(job, new Path(args[1]));
	    
	System.exit(job.waitForCompletion(true) ? 0 : 1);
    } 

}
