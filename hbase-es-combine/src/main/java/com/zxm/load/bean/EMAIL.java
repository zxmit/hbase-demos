package com.zxm.load.bean;

import java.util.Date;

/**
 * Created by zxm on 7/26/16.
 */
public class EMAIL {
    //    data_uuid long,
    private long DATA_UUID;
    //    data_type  varchar(30),
    private String DATA_TYPE;
    //    protocol_type varchar(30),
    private String PROTOCOL_TYPE;
    //    capture_time datetime,
    private Date CAPTURE_TIME;
    //    username text(2048) scianalyzer,
    private String USERNAME;
    //    passwd varchar(2048),
    private String PASSWD;
    //    email_type varchar(30),
    private String EMAIL_TYPE;
    //    src_ip ipv4_addr,
    private String SRC_IP;
    //    dst_ip ipv4_addr,
    private String DST_IP;
    //    src_port int,
    private int SRC_PORT;
    //    dst_port int,
    private int DST_PORT;
    //    fromenv varchar(2048),
    private String FROMENV;
    //    toenv text(2048) classicanalyzer,
    private String TOENV;
    //    mail_from varchar(2048),
    private String MAIL_FROM;
    //    rcpt_to text(2048) classicanalyzer,
    private String RCPT_TO;
    //    cc text(2048) classicanalyzer,
    private String CC;
    //    bcc varchar(2048),
    private String BCC;
    //    url varchar(2048),
    private String URL;
    //    subject text(524288000) scianalyzer,
    private String SUBJECT;
    //    sent_time datetime,
    private Date SENT_TIME;
    //    affixtext text(524288000) scianalyzer,
    private String AFFIXTEXT;
    //    content text(524288000) scianalyzer,
    private String CONTENT;
    //    remark varchar(32000),
    private String REMARK;
    //    affixcount int,
    private int AFFIXCOUNT;
    //    cap_ip ipv4_addr,
    private String CAP_IP;
    //    data_id long,
    private long DATA_ID;
    //    proto_type varchar(30),
    private String PROTO_TYPE;
    //    aa varchar(32000),
    private String AA;
    //    xx varchar(32000),
    private String XX;
    //    ww varchar(32000),
    private String WW;
    //    yy varchar(32000),
    private String YY;
    //    isp varchar(300) ,
    private String ISP;
    //    area varchar(3),
    private String AREA;
    //    original varchar(1024)
    private String ORIGINAL;

    public long getDATA_UUID() {
        return DATA_UUID;
    }

    public void setDATA_UUID(long DATA_UUID) {
        this.DATA_UUID = DATA_UUID;
    }

    public String getDATA_TYPE() {
        return DATA_TYPE;
    }

    public void setDATA_TYPE(String DATA_TYPE) {
        this.DATA_TYPE = DATA_TYPE;
    }

    public String getPROTOCOL_TYPE() {
        return PROTOCOL_TYPE;
    }

    public void setPROTOCOL_TYPE(String PROTOCOL_TYPE) {
        this.PROTOCOL_TYPE = PROTOCOL_TYPE;
    }

    public Date getCAPTURE_TIME() {
        return CAPTURE_TIME;
    }

    public void setCAPTURE_TIME(Date CAPTURE_TIME) {
        this.CAPTURE_TIME = CAPTURE_TIME;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getPASSWD() {
        return PASSWD;
    }

    public void setPASSWD(String PASSWD) {
        this.PASSWD = PASSWD;
    }

    public String getEMAIL_TYPE() {
        return EMAIL_TYPE;
    }

    public void setEMAIL_TYPE(String EMAIL_TYPE) {
        this.EMAIL_TYPE = EMAIL_TYPE;
    }

    public String getSRC_IP() {
        return SRC_IP;
    }

    public void setSRC_IP(String SRC_IP) {
        this.SRC_IP = SRC_IP;
    }

    public String getDST_IP() {
        return DST_IP;
    }

    public void setDST_IP(String DST_IP) {
        this.DST_IP = DST_IP;
    }

    public int getSRC_PORT() {
        return SRC_PORT;
    }

    public void setSRC_PORT(int SRC_PORT) {
        this.SRC_PORT = SRC_PORT;
    }

    public int getDST_PORT() {
        return DST_PORT;
    }

    public void setDST_PORT(int DST_PORT) {
        this.DST_PORT = DST_PORT;
    }

    public String getFROMENV() {
        return FROMENV;
    }

    public void setFROMENV(String FROMENV) {
        this.FROMENV = FROMENV;
    }

    public String getTOENV() {
        return TOENV;
    }

    public void setTOENV(String TOENV) {
        this.TOENV = TOENV;
    }

    public String getMAIL_FROM() {
        return MAIL_FROM;
    }

    public void setMAIL_FROM(String MAIL_FROM) {
        this.MAIL_FROM = MAIL_FROM;
    }

    public String getRCPT_TO() {
        return RCPT_TO;
    }

    public void setRCPT_TO(String RCPT_TO) {
        this.RCPT_TO = RCPT_TO;
    }

    public String getCC() {
        return CC;
    }

    public void setCC(String CC) {
        this.CC = CC;
    }

    public String getBCC() {
        return BCC;
    }

    public void setBCC(String BCC) {
        this.BCC = BCC;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getSUBJECT() {
        return SUBJECT;
    }

    public void setSUBJECT(String SUBJECT) {
        this.SUBJECT = SUBJECT;
    }

    public Date getSENT_TIME() {
        return SENT_TIME;
    }

    public void setSENT_TIME(Date SENT_TIME) {
        this.SENT_TIME = SENT_TIME;
    }

    public String getAFFIXTEXT() {
        return AFFIXTEXT;
    }

    public void setAFFIXTEXT(String AFFIXTEXT) {
        this.AFFIXTEXT = AFFIXTEXT;
    }

    public String getCONTENT() {
        return CONTENT;
    }

    public void setCONTENT(String CONTENT) {
        this.CONTENT = CONTENT;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public int getAFFIXCOUNT() {
        return AFFIXCOUNT;
    }

    public void setAFFIXCOUNT(int AFFIXCOUNT) {
        this.AFFIXCOUNT = AFFIXCOUNT;
    }

    public String getCAP_IP() {
        return CAP_IP;
    }

    public void setCAP_IP(String CAP_IP) {
        this.CAP_IP = CAP_IP;
    }

    public long getDATA_ID() {
        return DATA_ID;
    }

    public void setDATA_ID(long DATA_ID) {
        this.DATA_ID = DATA_ID;
    }

    public String getPROTO_TYPE() {
        return PROTO_TYPE;
    }

    public void setPROTO_TYPE(String PROTO_TYPE) {
        this.PROTO_TYPE = PROTO_TYPE;
    }

    public String getAA() {
        return AA;
    }

    public void setAA(String AA) {
        this.AA = AA;
    }

    public String getXX() {
        return XX;
    }

    public void setXX(String XX) {
        this.XX = XX;
    }

    public String getWW() {
        return WW;
    }

    public void setWW(String WW) {
        this.WW = WW;
    }

    public String getYY() {
        return YY;
    }

    public void setYY(String YY) {
        this.YY = YY;
    }

    public String getISP() {
        return ISP;
    }

    public void setISP(String ISP) {
        this.ISP = ISP;
    }

    public String getAREA() {
        return AREA;
    }

    public void setAREA(String AREA) {
        this.AREA = AREA;
    }

    public String getORIGINAL() {
        return ORIGINAL;
    }

    public void setORIGINAL(String ORIGINAL) {
        this.ORIGINAL = ORIGINAL;
    }
}
