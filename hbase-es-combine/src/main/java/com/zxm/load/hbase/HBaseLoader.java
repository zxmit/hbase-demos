package com.zxm.load.hbase;

import com.sun.org.apache.commons.logging.Log;
import com.sun.org.apache.commons.logging.LogFactory;
import com.zxm.load.bean.EMAIL;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by zxm on 7/27/16.
 */
public class HBaseLoader implements Runnable{

    Log LOG = LogFactory.getLog(HBaseLoader.class);
    private String CF_DEFAULT = "hbasees";
    HBaseClientPools clientPools;
    private IncrementCounter counter;
    private Table table;
    List<EMAIL> emails = null;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public HBaseLoader(HBaseClientPools clientPools, Table table, String cf, List<EMAIL> emails, IncrementCounter counter) {
//        System.out.println("Load email into HBase, Thread " + thread_num++);
    	this.CF_DEFAULT = cf;
        this.clientPools = clientPools;
        this.table = table;
        this.emails = emails;
        this.counter = counter;
    }

    public void run() {
        try {
            byte[] family = Bytes.toBytes(CF_DEFAULT);
            List<Put> puts = new ArrayList<Put>(emails.size());
            for(EMAIL email : emails) {
                Put put = generatePut(email, family);
                if(put != null) {
                    puts.add(put);
                }
                puts.add(put);
            }
            table.put(puts);
            table.close();
//            System.out.println("cdcdcd");
            System.out.println("【HBase】 ::: Insert data ==========>>>: " + counter.addAndGet(emails.size()));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            clientPools.giveBack(table);
        }
    }

    private Put generatePut(EMAIL email, byte[] family) {

        Put put = new Put(Bytes.toBytes(email.getDATA_UUID()+""));
        put.setDurability(Durability.SKIP_WAL);

        if(email.getDATA_TYPE() != null)
            put.addColumn(family, Bytes.toBytes("DATA_TYPE"),
                    Bytes.toBytes(email.getDATA_TYPE()));
        if(email.getPROTO_TYPE() != null)
            put.addColumn(family, Bytes.toBytes("PROTOCOL_TYPE"),
                    Bytes.toBytes(email.getPROTO_TYPE()));
        if(email.getCAPTURE_TIME() != null)
            put.addColumn(family, Bytes.toBytes("CAPTURE_TIME"),
                    Bytes.toBytes(format.format(email.getCAPTURE_TIME())));
        if(email.getUSERNAME() != null)
            put.addColumn(family, Bytes.toBytes("USERNAME"),
                    Bytes.toBytes(email.getUSERNAME()));
        if(email.getPASSWD() != null)
            put.addColumn(family, Bytes.toBytes("PASSWD"),
                    Bytes.toBytes(email.getPASSWD()));

        if(email.getEMAIL_TYPE() != null)
            put.addColumn(family, Bytes.toBytes("EMAIL_TYPE"), Bytes.toBytes(email.getEMAIL_TYPE()));

        if(email.getSRC_IP() != null)
            put.addColumn(family, Bytes.toBytes("SRC_IP"),
                    Bytes.toBytes(email.getSRC_IP()));

        if(email.getDST_IP() != null)
            put.addColumn(family, Bytes.toBytes("DST_IP"),
                    Bytes.toBytes(email.getDST_IP()));

        if(email.getSRC_PORT() != 0)
            put.addColumn(family, Bytes.toBytes("SRC_PORT"),
                    Bytes.toBytes(email.getSRC_PORT()+""));

        if(email.getDST_PORT() != 0)
            put.addColumn(family, Bytes.toBytes("DST_PORT"),
                    Bytes.toBytes(email.getDST_PORT()+""));

        if(email.getFROMENV() != null)
            put.addColumn(family, Bytes.toBytes("FROMENV"),
                    Bytes.toBytes(email.getFROMENV()));

        if(email.getTOENV() != null)
            put.addColumn(family, Bytes.toBytes("TOENV"), Bytes.toBytes(email.getTOENV()));

        if(email.getMAIL_FROM() != null)
            put.addColumn(family, Bytes.toBytes("MAIL_FROM"),
                    Bytes.toBytes(email.getMAIL_FROM()));

        if(email.getRCPT_TO() != null)
            put.addColumn(family, Bytes.toBytes("RCPT_TO"),
                    Bytes.toBytes(email.getRCPT_TO()));

        if(email.getCC() != null)
            put.addColumn(family, Bytes.toBytes("CC"),
                    Bytes.toBytes(email.getCC()));

        if(email.getBCC() != null)
            put.addColumn(family, Bytes.toBytes("BCC"),
                    Bytes.toBytes(email.getBCC()));

        if(email.getURL() != null)
            put.addColumn(family, Bytes.toBytes("URL"),
                    Bytes.toBytes(email.getURL()));

        if(email.getSUBJECT() != null)
            put.addColumn(family, Bytes.toBytes("SUBJECT"),
                    Bytes.toBytes(email.getSUBJECT()));

        if(email.getSENT_TIME() != null)
            put.addColumn(family, Bytes.toBytes("SENT_TIME"),
                    Bytes.toBytes(format.format(email.getSENT_TIME())));

        if(email.getAFFIXTEXT() != null)
            put.addColumn(family, Bytes.toBytes("AFFIXTEXT"),
                    Bytes.toBytes(email.getAFFIXTEXT()));

        if(email.getCONTENT() != null)
            put.addColumn(family, Bytes.toBytes("CONTENT"),
                    Bytes.toBytes(email.getCONTENT()));

        if(email.getREMARK() != null)
            put.addColumn(family, Bytes.toBytes("REMARK"),
                    Bytes.toBytes(email.getREMARK()));

        if(email.getAFFIXCOUNT() >=0)
            put.addColumn(family, Bytes.toBytes("AFFIXCOUNT"),
                    Bytes.toBytes(email.getAFFIXCOUNT()+""));

        if(email.getCAP_IP() != null)
            put.addColumn(family, Bytes.toBytes("CAP_IP"),
                    Bytes.toBytes(email.getCAP_IP()));

        if(email.getDATA_ID() >=0 )
            put.addColumn(family, Bytes.toBytes("DATA_ID"),
                    Bytes.toBytes(email.getDATA_ID()+""));

        if(email.getPROTO_TYPE() != null)
            put.addColumn(family, Bytes.toBytes("PROTO_TYPE"),
                    Bytes.toBytes(email.getPROTO_TYPE()));

        if(email.getAA() != null)
            put.addColumn(family, Bytes.toBytes("AA"),
                    Bytes.toBytes(email.getAA()));

        if(email.getXX() != null)
            put.addColumn(family, Bytes.toBytes("XX"),
                    Bytes.toBytes(email.getXX()));

        if(email.getWW() != null)
            put.addColumn(family, Bytes.toBytes("WW"),
                    Bytes.toBytes(email.getWW()));

        if(email.getYY() != null)
            put.addColumn(family, Bytes.toBytes("YY"),
                    Bytes.toBytes(email.getYY()));

        if(email.getISP() != null)
            put.addColumn(family, Bytes.toBytes("ISP"),
                    Bytes.toBytes(email.getISP()));

        if(email.getAREA() != null)
            put.addColumn(family, Bytes.toBytes("AREA"),
                    Bytes.toBytes(email.getAREA()));

        if(email.getORIGINAL() != null)
            put.addColumn(family, Bytes.toBytes("ORIGINAL"),
                    Bytes.toBytes(email.getORIGINAL()));

        return put;
    }

}