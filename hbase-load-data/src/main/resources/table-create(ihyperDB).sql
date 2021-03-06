create table t_mail (
           data_uuid long,
           data_type  varchar(30),
           protocol_type varchar(30),
           capture_time datetime,
           username text(2048) scianalyzer,
           passwd varchar(2048),
           email_type varchar(30),
           src_ip ipv4_addr,
           dst_ip ipv4_addr,
           src_port int,
           dst_port int,
           fromenv varchar(2048),
           toenv text(2048) classicanalyzer,
           mail_from varchar(2048),
           rcpt_to text(2048) classicanalyzer,
           cc text(2048) classicanalyzer,
           bcc varchar(2048),
           url varchar(2048),
           subject text(524288000) scianalyzer,
           sent_time datetime,
           affixtext text(524288000) scianalyzer,
           content text(524288000) scianalyzer,
           remark varchar(32000),
           affixcount int,
           cap_ip ipv4_addr,
           data_id long,
           proto_type varchar(30),
           aa varchar(32000),
           xx varchar(32000),
           ww varchar(32000),
           yy varchar(32000),
           isp varchar(300),
           area varchar(3),
           original varchar(1024)
       ) partition by capture_time(range 86400), fromenv(hash 6);