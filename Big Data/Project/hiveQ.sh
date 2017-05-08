hive -e "add jar hadoop-pcap-serde-1.1-SNAPSHOT-jar-with-dependencies.jar; select protocol,count(*) from pcaps1 group by protocol order by protocol" > /home/mustafa/Fall2015/Research/CyberSecurityDataSets/Jun_outputs/protocol.csv

hive -e "add jar hadoop-pcap-serde-1.1-SNAPSHOT-jar-with-dependencies.jar; select src,count(*) from pcaps1 group by src order by src" > /home/mustafa/Fall2015/Research/CyberSecurityDataSets/Jun_outputs/sources.csv
 
hive -e "add jar hadoop-pcap-serde-1.1-SNAPSHOT-jar-with-dependencies.jar; select dst,count(*) from pcaps1 group by dst order by dst" > /home/mustafa/Fall2015/Research/CyberSecurityDataSets/Jun_outputs/destination.csv


hive -e "add jar hadoop-pcap-serde-1.1-SNAPSHOT-jar-with-dependencies.jar; select src_port,count(*) from pcaps1 group by src_port order by src_port" > /home/mustafa/Fall2015/Research/CyberSecurityDataSets/Jun_outputs/source_ports.csv


hive -e "add jar hadoop-pcap-serde-1.1-SNAPSHOT-jar-with-dependencies.jar; select dst_port,count(*) from pcaps1 group by dst_port order by dst_port" > /home/mustafa/Fall2015/Research/CyberSecurityDataSets/Jun_outputs/dest_ports.csv

hive -e "add jar hadoop-pcap-serde-1.1-SNAPSHOT-jar-with-dependencies.jar; select src,src_port,count(*) from pcaps1 group by src,src_port order by src desc,src_port" > /home/mustafa/Fall2015/Research/CyberSecurityDataSets/Jun_outputs/sourceports.csv

hive -e "add jar hadoop-pcap-serde-1.1-SNAPSHOT-jar-with-dependencies.jar; select dst,dst_port,count(*) from pcaps1 group by dst,dst_port order by dst,dst_port" > /home/mustafa/Fall2015/Research/CyberSecurityDataSets/Jun_outputs/destports.csv


hive -e "add jar hadoop-pcap-serde-1.1-SNAPSHOT-jar-with-dependencies.jar; select ts,count(*) from pcaps1 group by ts order by ts" > /home/mustafa/Fall2015/Research/CyberSecurityDataSets/Jun_outputs/timeCount.csv

hive -e "add jar hadoop-pcap-serde-1.1-SNAPSHOT-jar-with-dependencies.jar; select ts,src,count(*) from pcaps1 group by ts,src order by ts,src" > /home/mustafa/Fall2015/Research/CyberSecurityDataSets/Jun_outputs/timeSIPs.csv

hive -e "add jar hadoop-pcap-serde-1.1-SNAPSHOT-jar-with-dependencies.jar; select ts,len,count(*) from pcaps1 group by ts,len order by ts,len" > /home/mustafa/Fall2015/Research/CyberSecurityDataSets/Jun_outputs/timeBytes.csv

hive -e "add jar hadoop-pcap-serde-1.1-SNAPSHOT-jar-with-dependencies.jar; select src,dst,count(*) from pcaps1 group by src,dst order by src,dst" > /home/mustafa/Fall2015/Research/CyberSecurityDataSets/Jun_outputs/sourcesDestsC.csv



