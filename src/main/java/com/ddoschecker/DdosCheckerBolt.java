package com.ddoschecker;

import java.util.HashMap;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

public class DdosCheckerBolt extends BaseRichBolt {
    private OutputCollector collector;
    private Map<String, Integer> ipCountMap;
    private int threshold;
    private int timeWindow;

    public DdosCheckerBolt() {
    }

    public DdosCheckerBolt(int threshold, int timeWindow) {
        this.threshold = threshold;
        this.timeWindow = timeWindow;
    }

    public DdosCheckerBolt(OutputCollector collector, Map<String, Integer> ipCountMap, int threshold, int timeWindow) {
        this.collector = collector;
        this.ipCountMap = ipCountMap;
        this.threshold = threshold;
        this.timeWindow = timeWindow;
    }

    public OutputCollector getCollector() {
        return collector;
    }

    public void setCollector(OutputCollector collector) {
        this.collector = collector;
    }

    public Map<String, Integer> getIpCountMap() {
        return ipCountMap;
    }

    public void setIpCountMap(Map<String, Integer> ipCountMap) {
        this.ipCountMap = ipCountMap;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public int getTimeWindow() {
        return timeWindow;
    }

    public void setTimeWindow(int timeWindow) {
        this.timeWindow = timeWindow;
    }

    @Override
    public void prepare(Map<String, Object> topoConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.ipCountMap = new HashMap<String, Integer>();
    }

    @Override
    public void execute(Tuple tuple) {
        // Get the source IP from the tuple
        String ip = tuple.getStringByField("sourceIP");
        
        // Get the current time
        long timeStamp = System.currentTimeMillis();
        
        // Update the count of requests from this IP
        int count = ipCountMap.getOrDefault(ip, 0);
        ipCountMap.put(ip, count + 1);

        // Emit an alert if the count exceeds the threshold within the time window
        if (count + 1 > threshold) {
            long oldestTimeStamp = timeStamp - (timeWindow * 1000);
            int totalCount = 0;
            for (Integer value : ipCountMap.values()) {
                totalCount += value;
            }
            if (totalCount > threshold && oldestTimeStamp < timeStamp) {
                collector.emit(new Values(ip, timeStamp));
                System.out.println("DDoS attack detected from " + ip + " at " + timeStamp);
            }
        }
        collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("ip", "timeStamp"));
    }
}