package com.zxm.load;

import com.alibaba.fastjson.JSON;
import com.zxm.load.bean.EMAIL;
import com.zxm.load.es.ESClientPools;
import com.zxm.load.es.ESLoader;
import com.zxm.load.hbase.HBaseClientPools;
import com.zxm.load.hbase.HBaseLoader;
import com.zxm.load.hbase.IncrementCounter;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;
import org.elasticsearch.client.transport.TransportClient;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by zxm on 7/26/16.
 */
public class EmailLoader {

	private String TABLE_NAME;
	private String CF_DEFAULT;
	private String ES_INDEX;
	private int batch_size;
	private Connection connection = null;
	private ExecutorService hexecutor;
	private ExecutorService eexecutor;
	private static org.apache.hadoop.conf.Configuration conf = null;
	private ESClientPools esClientPools;
	private HBaseClientPools hbClientPools;
	private IncrementCounter es_counter;
	private IncrementCounter hbase_counter;
	private static long count;
	public EmailLoader(String htable, String cf, String ex_index, int thread_size, int batch_size) throws IOException {
		this.TABLE_NAME = htable;
		this.CF_DEFAULT = cf;
		this.ES_INDEX = ex_index;
		this.batch_size = batch_size;
		hexecutor = Executors.newFixedThreadPool(thread_size);
		eexecutor = Executors.newFixedThreadPool(thread_size);
		conf = HBaseConfiguration.create();
		conf.addResource(new Path(
				"core-site.xml"));
		conf.addResource(new Path(
				"hbase-site.xml"));
		hbase_counter = new IncrementCounter();
		es_counter = new IncrementCounter();
		esClientPools = new ESClientPools(thread_size);
		connection = ConnectionFactory.createConnection(conf);
		hbClientPools = new HBaseClientPools(thread_size, connection, htable);
	}

	public static void main(String[] args) throws IOException {
		if(args.length < 6) {
			System.out.println("设置相关运行参数....");
			System.exit(0);
		}
		
		String htable = args[0];
		String hcf = args[1];
		String es_index = args[2];
		int thread_size = Integer.parseInt(args[3]);
		int batch_size = Integer.parseInt(args[4]);
		String file = args[5];
		

		EmailLoader loader = new EmailLoader(htable, hcf, es_index, thread_size, batch_size);
        
        Thread dameon = new Thread(new Runnable(){

        	public void run() {
        		long start = System.currentTimeMillis();
        		try {
        			while(true) {
        				TimeUnit.SECONDS.sleep(1);
        				System.out.println("Time cost: " + (System.currentTimeMillis() - start));
        			}
        		} catch(Exception e) {
        			
        		}
        	}
        });
        dameon.setDaemon(true);
        dameon.start();
        loader.start(new File(file));
        
        loader.eexecutor.shutdown();
        loader.hexecutor.shutdown();
    }

	public void connect() throws IOException {
		connection = ConnectionFactory.createConnection(conf);
	}

	
	
	private void start(File file) throws FileNotFoundException, UnsupportedEncodingException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
		String line = "";
		try {
			List<EMAIL> emails = new ArrayList<EMAIL>(batch_size);
			List<String> jemails = new ArrayList<String>(batch_size);
			int batch = 0;
			while ((line = reader.readLine()) != null) {
				EMAIL e = JSON.parseObject(line, EMAIL.class);
				e.setDATA_UUID(getLongId());
				emails.add(e);
				jemails.add(line);
				if (++batch == batch_size) {
//					 borrow client
					TransportClient client = esClientPools.borrow();
					eexecutor.submit(new ESLoader(esClientPools, client, ES_INDEX, jemails, emails, es_counter));
//					Table table = hbClientPools.borrow();
//					hexecutor.submit(new HBaseLoader(hbClientPools, table, CF_DEFAULT, emails, hbase_counter));
					batch = 0;
					emails = new ArrayList<EMAIL>(batch_size);
					jemails = new ArrayList<String>(batch_size);
					count += batch_size;
				}
			}
			if (batch != 0) {
				count += emails.size();
				TransportClient client = esClientPools.borrow();
				eexecutor.submit(new ESLoader(esClientPools, client, ES_INDEX, jemails, emails, es_counter));
//				Table table = hbClientPools.borrow();
//				hexecutor.submit(new HBaseLoader(hbClientPools, table, CF_DEFAULT, emails, hbase_counter));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static synchronized long getLongId() {
		StringBuffer sb = new StringBuffer(System.nanoTime()+"");
		Random random = new Random();
		String a = (random.nextInt(9)+1) + sb.toString();
		return Long.parseLong(a);
	}

	private void stop() throws IOException {
		connection.close();
	}

}