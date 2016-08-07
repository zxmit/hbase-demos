package com.zxm.hbase.coprocessor.observer;

import com.sun.org.apache.commons.logging.Log;
import com.sun.org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CoprocessorEnvironment;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.InternalScanner;
import org.apache.hadoop.hbase.regionserver.Region;
import org.apache.hadoop.hbase.regionserver.RegionServerServices;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.zookeeper.ZKUtil;
import org.apache.hadoop.hbase.zookeeper.ZooKeeperWatcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxm on 7/22/16.
 */
public class RowCountObserver extends BaseRegionObserver {

    Log LOG = LogFactory.getLog(RowCountObserver.class);
    RegionCoprocessorEnvironment env;
    private String zNodePath = "/hbase/rowcount/demo";
    private ZooKeeperWatcher zkw = null;
    private Region m_region= null;
    private long myrowcount = 0;

    @Override
    public void start(CoprocessorEnvironment e) throws IOException {
        env = (RegionCoprocessorEnvironment) e;
        RegionServerServices rss = env.getRegionServerServices();
        zkw = rss.getZooKeeper();
        m_region = this.env.getRegion();

        myrowcount = 0 ; //count;
        try{
            if(ZKUtil.checkExists(zkw,zNodePath) == -1) {
                LOG.error("LIULIUMI: cannot find the znode");
                ZKUtil.createWithParents(zkw, zNodePath);
                LOG.info("znode path is : " + zNodePath);
            }
        } catch (Exception ee) {
            LOG.error("LIULIUMI: create znode fail");
        }
    }

    @Override
    public void postOpen(ObserverContext<RegionCoprocessorEnvironment> e) {
        long count = 0;
        //Scan 获取当前 region 保存的行数
        try{
            Scan scan = new Scan();
            InternalScanner scanner = null;
            scanner = m_region.getScanner(scan);
            List<Cell> results = new ArrayList<Cell>();
            boolean hasMore = false;
            do {
                hasMore = scanner.next(results);
                if(results.size()>0)
                    count++;
            } while (hasMore);

            //用当前的行数设置 ZooKeeper 中的计数器初始值
            ZKUtil.setData(zkw,zNodePath, Bytes.toBytes(count));
            //设置 myrowcount 类成员，用来表示当前 Region 的 rowcount
            myrowcount = count;
        } catch (Exception ee) {
            LOG.info("setData exception");
        }

    }

    @Override
    public void postPut(ObserverContext<RegionCoprocessorEnvironment> e, Put put, WALEdit edit, Durability durability) throws IOException {
        //计数器加 1
        myrowcount++;
        //更新 znode
        try{
            ZKUtil.setData(zkw,zNodePath,Bytes.toBytes(myrowcount));
        } catch (Exception ee) {
            LOG.info("setData exception");
        }
    }

    @Override
    public void postDelete(ObserverContext<RegionCoprocessorEnvironment> e, Delete delete, WALEdit edit, Durability durability) throws IOException {
        //计数器减 1
        myrowcount--;
        //更新 znode
        try{
            ZKUtil.setData(zkw,zNodePath,Bytes.toBytes(myrowcount));
        } catch (Exception ee) {
            LOG.info("setData exception");
        }
    }
}
