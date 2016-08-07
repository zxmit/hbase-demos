package com.zxm.hbase.coprocessor.endpoint;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.google.protobuf.Service;
import com.sun.org.apache.commons.logging.Log;
import com.sun.org.apache.commons.logging.LogFactory;
import com.zxm.hbase.coprocessor.endpoint.generated.GetRowCountProtos;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.Coprocessor;
import org.apache.hadoop.hbase.CoprocessorEnvironment;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.coprocessor.CoprocessorException;
import org.apache.hadoop.hbase.coprocessor.CoprocessorService;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.InternalScanner;
import org.apache.hadoop.hbase.regionserver.RegionServerServices;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.zookeeper.ZKUtil;
import org.apache.hadoop.hbase.zookeeper.ZooKeeperWatcher;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by zxm on 7/22/16.
 */
public class RowCountEndpoint extends GetRowCountProtos.rowCountService
        implements Coprocessor, CoprocessorService {

    Log LOG = LogFactory.getLog(RowCountEndpoint.class);
    private RegionCoprocessorEnvironment env;
    private String zNodePath = "/hbase/rowcount/demo";
    private ZooKeeperWatcher zkw = null;

    public void start(CoprocessorEnvironment env) throws IOException {
        if (env instanceof RegionCoprocessorEnvironment) {
            this.env = (RegionCoprocessorEnvironment)env;
            RegionServerServices rss = this.env.getRegionServerServices();
            zkw = rss.getZooKeeper();
//            zNodePath = zNodePath + this.env.getRegion().getRegionInfo().getRegionNameAsString();
            System.out.println("++++++++++++++++++++++++++++++++++++  " + zNodePath + " |");
            System.out.println("|" + this.env.getRegion().getRegionInfo().getRegionNameAsString() + "|");
        } else {
            throw new CoprocessorException("Must be loaded on a table region!");
        }
    }

    @Override
    public void getRowCount(RpcController controller, GetRowCountProtos.getRowCountRequest request,
                            RpcCallback<GetRowCountProtos.getRowCountResponse> done) {

        boolean reCount = request.getReCount();
        long rowcount = 0;
        if(reCount) {
            long start = System.currentTimeMillis();
            Scan scan = new Scan();
            InternalScanner scanner = null;
            try {
                List<Cell> results = new ArrayList<Cell>();
                scanner = env.getRegion().getScanner(scan);
                boolean hasMore = false;
                do {
                    hasMore = scanner.next(results);
                    rowcount++;
                } while (hasMore);
            } catch (IOException e) {
                e.printStackTrace();
            }
            LOG.info("Scan region cost: " + (System.currentTimeMillis()-start));
        } else {
            long start = System.currentTimeMillis();
            try{
                System.out.println(zNodePath + "********************");
                byte[] data = ZKUtil.getData(zkw, zNodePath);
                rowcount = Bytes.toLong(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
            LOG.info("Get from zk: " + (System.currentTimeMillis()-start));
        }

        GetRowCountProtos.getRowCountResponse response = null;
        response = GetRowCountProtos.getRowCountResponse.newBuilder()
                .setRowCount(rowcount).build();
        //将 rowcount 设置为 CountResponse 消息的 rowCount
        done.run(response); //Protobuf 的返回
    }

    public void stop(CoprocessorEnvironment env) throws IOException {
        // nothing to do
    }

    public Service getService() {
        return this;
    }
}
