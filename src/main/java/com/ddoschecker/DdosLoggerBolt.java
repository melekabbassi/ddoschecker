package com.ddoschecker;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

public class DdosLoggerBolt extends BaseRichBolt {

    private OutputCollector collector;

    // This method is called when the bolt is activated
    @Override
    public void prepare(Map<String, Object> topoConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    // This method is called when a tuple is received
    @Override
    public void execute(Tuple tuple) {
        String ip = tuple.getStringByField("ip");
        String timeStamp = tuple.getStringByField("timeStamp");
        
        // Write the log message to the log file
        String logFilePath = "/tmp/ddos-checker.log";
        String logMessage = "DDOS attack detected from " + ip + " at " + timeStamp;
        writeLog(logFilePath, logMessage);

        // Acknowledge the tuple
        collector.ack(tuple);
    }

    // Declare any resources that this bolt needs
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // No output fields
    }

    public void writeLog(String logFilePath, String logMessage) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath, true));
            writer.write(logMessage);
            writer.newLine();
            writer.close();
        } catch (Exception e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }   
}
