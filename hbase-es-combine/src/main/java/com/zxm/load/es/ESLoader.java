package com.zxm.load.es;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.sun.org.apache.commons.logging.Log;
import com.sun.org.apache.commons.logging.LogFactory;
import com.zxm.load.bean.EMAIL;
import com.zxm.load.hbase.IncrementCounter;

import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

/**
 * Created by zxm on 7/27/16.
 */
public class ESLoader implements Runnable {
    Log LOG = LogFactory.getLog(ESLoader.class);
    private static int thread_num = 0;

    private List<String> jemails;
    private List<EMAIL> emails;
    private TransportClient client;
    private String ES_INDEX = "xm_email_index_2";
    private String ES_TYPE = "resource";
    private ESClientPools clientPools;
    private IncrementCounter counter; 


    public ESLoader(ESClientPools clientPools, TransportClient client, String es_index, List<String> jemails, List<EMAIL> emails, IncrementCounter counter) {
    	this.ES_INDEX = es_index;
        this.jemails = jemails;
        this.emails = emails;
        this.counter = counter;
        this.client = client;
        this.clientPools = clientPools;
    }

    public void run() {
        try {
            BulkRequestBuilder bulkRequest = client.prepareBulk();
            for(int i=0; i<jemails.size(); i++) {
                bulkRequest.add(client.prepareIndex(ES_INDEX, ES_TYPE, emails.get(i).getDATA_UUID() + "")
                                .setSource(jemails.get(i))
                );
            }
            counter.addAndGet(emails.size());
            BulkResponse bulkResponse = bulkRequest.get();
            if (bulkResponse.hasFailures()) {
                Iterator<BulkItemResponse> iterator = bulkResponse.iterator();
                while(iterator.hasNext()) {
                	counter.reduceAndGet(1);
                    BulkItemResponse response = iterator.next();
                    if(response.isFailed()) {
                        System.out.println("Failed: =======>>>>  " + response.getFailureMessage());
                    }
                }
            }
            System.out.println("【ES】 ::: Insert data ===================>>>: " + counter.get());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            clientPools.giveBack(client);
        }

    }
}
