package com.zxm.hbase.demo01.curd;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

/**
 * Created by zxm on 7/21/16.
 */
public class BaseOperationTest {

    private static org.apache.hadoop.conf.Configuration conf = null;
    private static HBaseAdmin admin = null;

//    private static final String TABLE_NAME = "MY_TABLE_NAME_TOO";
    private static final String TABLE_NAME = "hbase_es_01";
//    private static final String CF_DEFAULT = "DEFAULT_COLUMN_FAMILY";
private static final String CF_DEFAULT = "hbasees";



    public static void init() {
        conf = HBaseConfiguration.create();
        conf.addResource(new Path("core-site.xml"));
        conf.addResource(new Path("hbase-site.xml"));
    }

    public static void createOrOverwrite(Admin admin, HTableDescriptor table) throws IOException {
        if (admin.tableExists(table.getTableName())) {
            admin.disableTable(table.getTableName());
            admin.deleteTable(table.getTableName());
        }
        admin.createTable(table);
    }

    public static void createTableSchema() throws IOException {
        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();
        HTableDescriptor table = new HTableDescriptor(TableName.valueOf(TABLE_NAME));
        table.addFamily(new HColumnDescriptor(CF_DEFAULT));
        createOrOverwrite(admin, table);
    }

    public static void modifyTableSchema() throws IOException {
        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();

        TableName tableName = TableName.valueOf(TABLE_NAME);
        if (admin.tableExists(tableName)) {
            System.out.println("Table does not exist.");
            System.exit(-1);
        }

        HTableDescriptor table = new HTableDescriptor(tableName);

        // Update existing table
        HColumnDescriptor newColumn = new HColumnDescriptor("NEWCF");
        newColumn.setMaxVersions(HConstants.ALL_VERSIONS);
        admin.addColumn(tableName, newColumn);

        // Update existing column family
        HColumnDescriptor existingColumn = new HColumnDescriptor(CF_DEFAULT);
        existingColumn.setMaxVersions(HConstants.ALL_VERSIONS);
        table.modifyFamily(existingColumn);
        admin.modifyTable(tableName, table);

        // Disable an existing table
        admin.disableTable(tableName);

        // Delete an existing column family
        admin.deleteColumn(tableName, CF_DEFAULT.getBytes("UTF-8"));

        // Delete a table (Need to be disabled first)
        admin.deleteTable(tableName);

    }

    public static void put() throws IOException {
        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf(TABLE_NAME));

        Put put = new Put(Bytes.toBytes("ROW-4000"));
        byte[] family = Bytes.toBytes(CF_DEFAULT);
        byte[] qualifier1 = Bytes.toBytes("Q-01");
        byte[] qualifier2 = Bytes.toBytes("Q-02");
        put.addColumn(family, qualifier1, Bytes.toBytes("V-01"));
        put.addColumn(family, qualifier2, Bytes.toBytes("V-02"));

        table.put(put);
        table.close();
    }

    public static void putWithRowKey(String rowkey) throws IOException {
        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf(TABLE_NAME));

        Put put = new Put(Bytes.toBytes(rowkey));
        byte[] family = Bytes.toBytes(CF_DEFAULT);
        byte[] qualifier1 = Bytes.toBytes("Q-01");
        put.addColumn(family, qualifier1, Bytes.toBytes("V-01"));

        table.put(put);
        table.close();
        connection.close();
    }

    public static void get_01() throws IOException {
        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf(TABLE_NAME));

        Get get = new Get(Bytes.toBytes("ROW-01"));
        get.addColumn(Bytes.toBytes(CF_DEFAULT), Bytes.toBytes("Q-01"));
        Result result = table.get(get);
        List<Cell> cells = result.listCells();
        for(Cell cell : cells) {
            CellUtil.matchingQualifier(cell, Bytes.toBytes("cdcd"));
            StringBuilder sb = new StringBuilder(Bytes.toString(CellUtil.cloneFamily(cell)));
            sb.append(":").append(Bytes.toString(CellUtil.cloneQualifier(cell))).append("--:");
            sb.append(Bytes.toString(CellUtil.cloneValue(cell))).append("  ").append(
                    new Date(cell.getTimestamp()).toLocaleString());
            System.out.println(sb.toString());
        }
    }

    public static void get_02() throws IOException {
        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf(TABLE_NAME));

        Get get = new Get(Bytes.toBytes("ROW-01"));
        get.addColumn(Bytes.toBytes(CF_DEFAULT), Bytes.toBytes("Q-01"));
        Result result = table.get(get);

        result.getMap();
    }



    public static void main(String[] args) throws IOException {

        init();

        // 创建表
//        createTableSchema();

        // 测试数据插入操作
        put();

//        for(int i=4001; i<400000; i++) {
//            putWithRowKey("ROW-"+i);
//        }

        // 测试数据查询操作 01
//        get_01();




    }

}
