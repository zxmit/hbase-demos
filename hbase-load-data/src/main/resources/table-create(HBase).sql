create table t_mail (
           DATA_UUID long,
           DATA_TYPE  varchar(30),
           PROTOCOL_TYPE varchar(30),
           CAPTURE_TIME datetime,
           USERNAME text(2048) scianalyzer,
           PASSWD varchar(2048),
           EMAIL_TYPE varchar(30),
           SRC_IP ipv4_addr,
           DST_IP ipv4_addr,
           SRC_PORT int,
           DST_PORT int,
           FROMENV varchar(2048),
           TOENV text(2048) classicanalyzer,
           MAIL_FROM varchar(2048),
           RCPT_TO text(2048) classicanalyzer,
           CC text(2048) classicanalyzer,
           BCC varchar(2048),
           URL varchar(2048),
           SUBJECT text(524288000) scianalyzer,
           SENT_TIME datetime,
           AFFIXTEXT text(524288000) scianalyzer,
           CONTENT text(524288000) scianalyzer,
           REMARK varchar(32000),
           AFFIXCOUNT int,
           CAP_IP ipv4_addr,
           DATA_ID long,
           PROTO_TYPE varchar(30),
           AA   varchar(32000),
           XX   varchar(32000),
           WW   varchar(32000),
           YY   varchar(32000),
           ISP  varchar(300),
           AREA varchar(3),
           ORIGINAL varchar(1024)
       ) partition by capture_time(range 86400), fromenv(hash 6);