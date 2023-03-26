package com.ddoschecker;

import org.apache.storm.topology.ConfigurableTopology;
import org.apache.storm.topology.TopologyBuilder;

public class DdosCheckerTopology extends ConfigurableTopology {

    public static void main(String[] args) throws Exception {
        ConfigurableTopology.start(new DdosCheckerTopology(), args);
    }

    @Override
    protected int run(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("network-traffic-spout", new NetworkTrafficSpout(), 1);

        builder.setBolt("ddos-checker-bolt", new DdosCheckerBolt(100, 10), 1).shuffleGrouping("network-traffic-spout");

        builder.setBolt("ddos-alert-bolt", new DdosAlertBolt(), 1).shuffleGrouping("ddos-checker-bolt");

        builder.setBolt("ddos-logger-bolt", new DdosLoggerBolt(), 1).shuffleGrouping("ddos-checker-bolt");

        conf.setDebug(true);
        String topologyName = "ddos-checker-topology";
        conf.setNumWorkers(3);
        if(args != null && args.length > 0) {
            topologyName = args[0];
        }

        return submit(topologyName, conf, builder);
    }
    
}
