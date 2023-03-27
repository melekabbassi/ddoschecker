package com.ddoschecker;

import java.io.EOFException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.packet.Packet;
import org.pcap4j.core.BpfProgram;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

public class NetworkTrafficSpout extends BaseRichSpout {

    private SpoutOutputCollector collector;
    private PcapHandle handle;
    private int packetCount = 0;
    
    public NetworkTrafficSpout() {
    }

    public NetworkTrafficSpout(SpoutOutputCollector collector, PcapHandle handle, int packetCount) {
        this.collector = collector;
        this.handle = handle;
        this.packetCount = packetCount;
    }    

    public SpoutOutputCollector getCollector() {
        return collector;
    }

    public void setCollector(SpoutOutputCollector collector) {
        this.collector = collector;
    }

    public PcapHandle getHandle() {
        return handle;
    }

    public void setHandle(PcapHandle handle) {
        this.handle = handle;
    }

    public int getPacketCount() {
        return packetCount;
    }

    public void setPacketCount(int packetCount) {
        this.packetCount = packetCount;
    }

    // This method is called when the spout is activated
    @Override
    public void open(Map<String, Object> conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        try {
            // Open the pcap handle for the network interface
            PcapNetworkInterface nif = Pcaps.getDevByName("lo");
            handle = nif.openLive(65536, PromiscuousMode.PROMISCUOUS, 10);
            handle.setFilter("port 8083", BpfProgram.BpfCompileMode.OPTIMIZE);
            //handle.setFilter("portrange 1-65535", BpfProgram.BpfCompileMode.OPTIMIZE);
        } catch (PcapNativeException | NotOpenException e) {
            e.printStackTrace();
        }
    }

    // This method is called when the spout is deactivated
    @Override
    public void nextTuple() {
        try {
            // Read the next packet from the pcap handle
            Packet packet = handle.getNextPacketEx();
            if (packet != null) {
                // Emit the packet as a tuple
                collector.emit(new Values(packet));
                packetCount++;
            }
        } catch (NotOpenException | EOFException | PcapNativeException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    // This method is called when the spout is deactivated
    @Override
    public void close() {
        // Close the pcap handle
        handle.close();
    }

    // Declare the output fields of the spout
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("packet"));
    }
}
