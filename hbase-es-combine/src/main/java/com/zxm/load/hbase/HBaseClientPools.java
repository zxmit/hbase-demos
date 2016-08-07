package com.zxm.load.hbase;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Table;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;

public class HBaseClientPools {

	private int poolSize;
	private LinkedList<Table> clientPools;
	private int currentSize;
	private static Object lock = new Object();

	private String TABLE_NAME = "hbase_es_03";
	private Connection connection = null;

	public HBaseClientPools(int pool_size, Connection connection, String table_name) {
		this.TABLE_NAME = table_name;
		this.poolSize = pool_size;
		this.connection = connection;
		clientPools = new LinkedList<Table>();
		currentSize = 0;
	}
	
	public Table borrow() {
		synchronized (lock) {
			if(currentSize < poolSize) {
				currentSize++;
				clientPools.add(getClient());
			}
			if(clientPools.size() == 0) {
				try {
					System.out.println("+++++++++++++++++ WAITING +++++++++++++++++");
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Table client = clientPools.removeLast();
			return client;
		}
	}
	
	public void giveBack(Table client) {
		synchronized (lock) {
			clientPools.add(client);
			lock.notify();
		}
	}

	private Table getClient() {
		Table table = null;
		try {
			table = connection.getTable(TableName.valueOf(TABLE_NAME));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return table;
	}

}

