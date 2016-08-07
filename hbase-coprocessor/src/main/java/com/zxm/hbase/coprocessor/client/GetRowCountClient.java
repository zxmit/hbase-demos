package com.zxm.hbase.coprocessor.client;

import com.google.protobuf.ServiceException;
import com.zxm.hbase.coprocessor.endpoint.generated.GetRowCountProtos;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.client.coprocessor.Batch;
import org.apache.hadoop.hbase.ipc.BlockingRpcCallback;
import org.apache.hadoop.hbase.ipc.CoprocessorRpcChannel;
import org.apache.hadoop.hbase.ipc.ServerRpcController;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by zxm on 7/22/16.
 */
public class GetRowCountClient {

    private static org.apache.hadoop.conf.Configuration config = null;


    private static final String TABLE_NAME = "MY_TABLE_NAME_TOO";
    private static final String CF_DEFAULT = "DEFAULT_COLUMN_FAMILY";
    private static final String ROW_KEY = "ROW-10";
    public static void init() {
        config = HBaseConfiguration.create();
        config.addResource(new Path("core-site.xml"));
        config.addResource(new Path("hbase-site.xml"));
    }

    public static void main(String[] args) {
        System.out.println( "Hello HBase Coprocessor!" );
        GetRowCountClient client = new GetRowCountClient();
        client.init();

        long start = System.currentTimeMillis();
        boolean fastMethod = false;
        long singleCount = client.singleRegionCount(TABLE_NAME, ROW_KEY, fastMethod);
        System.out.println("Get single count: " + singleCount);
        System.out.println("Cost time: " + (System.currentTimeMillis() - start));

        System.out.println( "bye!" );
    }

    private long singleRegionCount(String tableName, String rowkey,boolean reCount) {
        long rowcount = 0;
        try{

            Connection connection =  ConnectionFactory.createConnection(config);
            Table table = connection.getTable(TableName.valueOf(tableName.getBytes()));
            //获取 Channel
            CoprocessorRpcChannel channel = table.coprocessorService(rowkey.getBytes());
            GetRowCountProtos.rowCountService.BlockingInterface service =
                    GetRowCountProtos.rowCountService.newBlockingStub(channel);

            //设置 RPC 入口参数
            GetRowCountProtos.getRowCountRequest.Builder request =
                    GetRowCountProtos.getRowCountRequest.newBuilder();
            request.setReCount(reCount);

            //调用 RPC
            GetRowCountProtos.getRowCountResponse ret =
                    service.getRowCount(null, request.build());

            //解析结果
            rowcount = ret.getRowCount();
        }
        catch(Exception e) {e.printStackTrace();}
        return rowcount;
    }

    private long getTableRowCountFast(String tableName) {
        final AtomicLong totalRowCount = new AtomicLong();
        Map<byte[], GetRowCountProtos.getRowCountResponse> results = null;
        try {

            Connection connection =  ConnectionFactory.createConnection(config);
            Table table = connection.getTable(TableName.valueOf(tableName.getBytes()));

            Batch.Call<GetRowCountProtos.rowCountService, GetRowCountProtos.getRowCountResponse> callable =
                    new Batch.Call<GetRowCountProtos.rowCountService, GetRowCountProtos.getRowCountResponse>() {

                        ServerRpcController controller = new ServerRpcController();
                        BlockingRpcCallback<GetRowCountProtos.getRowCountResponse> rpcCallback =
                                new BlockingRpcCallback<GetRowCountProtos.getRowCountResponse>();

                        //下面重载 call 方法
                        @Override
                        public GetRowCountProtos.getRowCountResponse call(GetRowCountProtos.rowCountService instance) throws IOException {
                            //初始化 RPC 的入口参数，设置 reCount 为 true
                            //Server 端会进行慢速的遍历 region 的方法进行统计
                            GetRowCountProtos.getRowCountRequest.Builder builder =
                                    GetRowCountProtos.getRowCountRequest.newBuilder();
                            builder.setReCount(true);
                            //RPC 调用
                            instance.getRowCount(controller, builder.build(), rpcCallback);
                            //直接返回结果，即该 Region 的 rowCount
                            return rpcCallback.get();
                        }
                    };

            Batch.Callback<GetRowCountProtos.getRowCountResponse> callback =
                    new Batch.Callback<GetRowCountProtos.getRowCountResponse>() {
                        @Override
                        public void update(byte[] region, byte[] row, GetRowCountProtos.getRowCountResponse result) {
                            totalRowCount.getAndAdd(result.getRowCount());
                        }
                    };

            table.coprocessorService(GetRowCountProtos.rowCountService.class, null, null,
                    callable, callback);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }


        return totalRowCount.get();
    }

    private long getTableRowCountSlow(String tableName) {
        long totalRowCount = 0;
        Map<byte[], GetRowCountProtos.getRowCountResponse> results = null;
        try {

            Connection connection =  ConnectionFactory.createConnection(config);
            Table table = connection.getTable(TableName.valueOf(tableName.getBytes()));

            Batch.Call<GetRowCountProtos.rowCountService, GetRowCountProtos.getRowCountResponse> callable =
                    new Batch.Call<GetRowCountProtos.rowCountService, GetRowCountProtos.getRowCountResponse>() {

                        ServerRpcController controller = new ServerRpcController();
                        BlockingRpcCallback<GetRowCountProtos.getRowCountResponse> rpcCallback =
                                new BlockingRpcCallback<GetRowCountProtos.getRowCountResponse>();

                        //下面重载 call 方法
                        @Override
                        public GetRowCountProtos.getRowCountResponse call(GetRowCountProtos.rowCountService instance) throws IOException {
                            //初始化 RPC 的入口参数，设置 reCount 为 true
                            //Server 端会进行慢速的遍历 region 的方法进行统计
                            GetRowCountProtos.getRowCountRequest.Builder builder =
                                    GetRowCountProtos.getRowCountRequest.newBuilder();
                            builder.setReCount(true);
                            //RPC 调用
                            instance.getRowCount(controller, builder.build(), rpcCallback);
                            //直接返回结果，即该 Region 的 rowCount
                            return rpcCallback.get();
                        }
                    };

            results = table.coprocessorService(GetRowCountProtos.rowCountService.class, null, null,
                    callable);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        Collection<GetRowCountProtos.getRowCountResponse> resultsc = results.values();
        for( GetRowCountProtos.getRowCountResponse r : resultsc)
        {
            totalRowCount += r.getRowCount();
        }
        return totalRowCount;
    }
}
