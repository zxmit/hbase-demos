package com.zxm.load.es;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;

public class ESClientPools {
	
	private int poolSize;
	private LinkedList<TransportClient> clientPools;
	private int currentSize;
	private static Object lock = new Object();

	private static final String HOST = "node1";
	private static final int PORT = 9300;
	private Settings settings = Settings.settingsBuilder()
			.put("cluster.name","es-cluster")
			.put("client.transport.sniff", true)
			.build();

	public ESClientPools(int pool_size) {
		this.poolSize = pool_size;
		clientPools = new LinkedList<TransportClient>();
		currentSize = 0;
	}
	
	public TransportClient borrow() {
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
			TransportClient client = clientPools.removeLast();
			return client;
		}
	}
	
	public void giveBack(TransportClient client) {
		synchronized (lock) {
			clientPools.add(client);
			lock.notify();
		}
	}

	private TransportClient getClient() {
		TransportClient client = null;
		try {
			client = TransportClient.builder().settings(settings).build()
                    .addTransportAddress(new InetSocketTransportAddress(
							InetAddress.getByName(HOST), PORT));
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return client;
	}

}

