package com.zxm.hbase.demo01.coprocessor;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

/**
 * Created by zxm on 7/21/16.
 */
public class PutObserverCoprocessor extends BaseRegionObserver{

    @Override
    public void prePut(ObserverContext<RegionCoprocessorEnvironment> e,
                       Put put, WALEdit edit, Durability durability) throws IOException {
        super.prePut(e, put, edit, durability);
        System.out.println("***==== before insert values ====***");

        NavigableMap<byte [], List<Cell>> maps = put.getFamilyCellMap();
        String row2 = Bytes.toString(put.getRow());
        for(Map.Entry<byte[], List<Cell>> entry : maps.entrySet()) {
            List<Cell> cells2 = entry.getValue();
            for(Cell cell : cells2) {
                String cf2 = Bytes.toString(CellUtil.cloneFamily(cell));
                String q2 = Bytes.toString(CellUtil.cloneQualifier(cell));
                String value2 = Bytes.toString(CellUtil.cloneValue(cell));
                System.out.println("row: " + row2 + " => " + cf2 + " + " + q2 + " = " + value2);
            }
        }

    }

    @Override
    public void postPut(ObserverContext<RegionCoprocessorEnvironment> e,
                        Put put, WALEdit edit, Durability durability) throws IOException {
        super.postPut(e, put, edit, durability);
        System.out.println("***==== after insert values ====***");
        List<Cell> cells = edit.getCells();
        for(Cell cell : cells) {
            String row = Bytes.toString(CellUtil.cloneRow(cell));
            String cf = Bytes.toString(CellUtil.cloneFamily(cell));
            String q = Bytes.toString(CellUtil.cloneQualifier(cell));
            String value = Bytes.toString(CellUtil.cloneValue(cell));
            System.out.println("row: " + row + " => " + cf + " + " + q + " = " + value);
        }
    }
}
