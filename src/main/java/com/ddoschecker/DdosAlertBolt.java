// package com.ddoschecker;

// import java.util.Map;

// import org.apache.storm.task.OutputCollector;
// import org.apache.storm.task.TopologyContext;
// import org.apache.storm.topology.OutputFieldsDeclarer;
// import org.apache.storm.topology.base.BaseRichBolt;
// import org.apache.storm.tuple.Tuple;
// import org.apache.commons.mail.DefaultAuthenticator;
// import org.apache.commons.mail.Email;
// import org.apache.commons.mail.EmailException;
// import org.apache.commons.mail.SimpleEmail;

// public class DdosAlertBolt extends BaseRichBolt {

//     private OutputCollector collector;
//     private String emailAddress;
//     private String emailPassword;

//     public DdosAlertBolt() {
//     }

//     public DdosAlertBolt(String emailAddress, String emailPassword) {
//         this.emailAddress = emailAddress;
//         this.emailPassword = emailPassword;
//     }

//     public DdosAlertBolt(OutputCollector collector, String emailAddress, String emailPassword) {
//         this.collector = collector;
//         this.emailAddress = emailAddress;
//         this.emailPassword = emailPassword;
//     }

//     public OutputCollector getCollector() {
//         return collector;
//     }

//     public void setCollector(OutputCollector collector) {
//         this.collector = collector;
//     }

//     public String getEmailAddress() {
//         return emailAddress;
//     }

//     public void setEmailAddress(String emailAddress) {
//         this.emailAddress = emailAddress;
//     }

//     public String getEmailPassword() {
//         return emailPassword;
//     }

//     public void setEmailPassword(String emailPassword) {
//         this.emailPassword = emailPassword;
//     }

//     @Override
//     public void prepare(Map<String, Object> topoConf, TopologyContext context, OutputCollector collector) {
//         this.collector = collector;
//     }

//     @Override
//     public void execute(Tuple input) {
//         // Extract the IP address and the timeStamp from the alert tuple
//         String ip = input.getStringByField("ip");
//         long timeStamp = input.getLongByField("timeStamp");
        
//         //Send an email alert
//         sendEmail(ip, timeStamp);

//         // Acknowledge the tuple
//         collector.ack(input);
//     }

//     @Override
//     public void declareOutputFields(OutputFieldsDeclarer declarer) {
//         // No output fields
//     }

//     public void sendEmail(String ip, long timeStamp) {
//         try {
//             Email email = new SimpleEmail();
//             email.setHostName("smtp.gmail.com");
//             email.setSmtpPort(465);
//             email.setAuthenticator(new DefaultAuthenticator(emailAddress, emailPassword));
//             email.setStartTLSEnabled(true);
//             email.setFrom(emailAddress);
//             email.setSubject("DDOS attack detected");
//             email.setMsg("DDOS attack detected from " + ip + " at " + timeStamp);
//             email.addTo("abbassimelek@gmail.com");
//             email.send();
//         } catch (EmailException e) {
//             System.err.println("Error sending email: " + e.getMessage());
//         }
//     }
// }
/****************************************************************************************************** */
// package com.ddoschecker;

// import java.util.Map;

// import org.apache.storm.task.OutputCollector;
// import org.apache.storm.task.TopologyContext;
// import org.apache.storm.topology.OutputFieldsDeclarer;
// import org.apache.storm.topology.base.BaseRichBolt;
// import org.apache.storm.tuple.Tuple;
// import org.apache.commons.mail.DefaultAuthenticator;
// import org.apache.commons.mail.Email;
// import org.apache.commons.mail.EmailException;
// import org.apache.commons.mail.SimpleEmail;

// public class DdosAlertBolt extends BaseRichBolt {

//     private OutputCollector collector;
//     private String emailAddress;
//     private String emailPassword;

//     public DdosAlertBolt() {
//     }

//     public DdosAlertBolt(String emailAddress, String emailPassword) {
//         this.emailAddress = emailAddress;
//         this.emailPassword = emailPassword;
//     }

//     public DdosAlertBolt(OutputCollector collector, String emailAddress, String emailPassword) {
//         this.collector = collector;
//         this.emailAddress = emailAddress;
//         this.emailPassword = emailPassword;
//     }

//     public OutputCollector getCollector() {
//         return collector;
//     }

//     public void setCollector(OutputCollector collector) {
//         this.collector = collector;
//     }

//     public String getEmailAddress() {
//         return emailAddress;
//     }

//     public void setEmailAddress(String emailAddress) {
//         this.emailAddress = emailAddress;
//     }

//     public String getEmailPassword() {
//         return emailPassword;
//     }

//     public void setEmailPassword(String emailPassword) {
//         this.emailPassword = emailPassword;
//     }

//     @Override
//     public void prepare(Map<String, Object> topoConf, TopologyContext context, OutputCollector collector) {
//         this.collector = collector;
//     }

//     @Override
//     public void execute(Tuple input) {
//         // Extract the IP address and the timeStamp from the alert tuple
//         String ip = input.getStringByField("ip");
//         long timeStamp = input.getLongByField("timeStamp");
//         int port = input.getIntegerByField("port");
        
//         // Check if port 8083 is being ddosed and send an email alert
//         if (port == 8083) {
//             sendEmail(ip, timeStamp);
//         }

//         // Acknowledge the tuple
//         collector.ack(input);
//     }

//     @Override
//     public void declareOutputFields(OutputFieldsDeclarer declarer) {
//         // No output fields
//     }

//     public void sendEmail(String ip, long timeStamp) {
//         try {
//             Email email = new SimpleEmail();
//             email.setHostName("smtp.gmail.com");
//             email.setSmtpPort(465);
//             email.setAuthenticator(new DefaultAuthenticator(emailAddress, emailPassword));
//             email.setStartTLSEnabled(true);
//             email.setFrom(emailAddress);
//             email.setSubject("DDOS attack detected");
//             email.setMsg("DDOS attack detected from " + ip + " on port 8083 at " + timeStamp);
//             email.addTo("abbassimelek@gmail.com");
//             email.send();
//         } catch (EmailException e) {
//             System.err.println("Error sending email: " + e.getMessage());
//         }
//     }
// }

package com.ddoschecker;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class DdosAlertBolt extends BaseRichBolt {

    private OutputCollector collector;
    private String emailAddress;
    private String emailPassword;

    public DdosAlertBolt() {
    }

    public DdosAlertBolt(String emailAddress, String emailPassword) {
        this.emailAddress = emailAddress;
        this.emailPassword = emailPassword;
    }

    public DdosAlertBolt(OutputCollector collector, String emailAddress, String emailPassword) {
        this.collector = collector;
        this.emailAddress = emailAddress;
        this.emailPassword = emailPassword;
    }

    public OutputCollector getCollector() {
        return collector;
    }

    public void setCollector(OutputCollector collector) {
        this.collector = collector;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    @Override
    public void prepare(Map<String, Object> topoConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        // Extract the IP address and the timeStamp from the alert tuple
        String ip = input.getStringByField("ip");
        long timeStamp = input.getLongByField("timeStamp");
        int port = input.getIntegerByField("port");
        
        // Check if port 8083 is being ddosed and send an email alert
        if (port == 8083) {
            sendEmail(ip, timeStamp);
        }

        // Acknowledge the tuple
        collector.ack(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // No output fields
    }

    public void sendEmail(String ip, long timeStamp) {
        try {
            Email email = new SimpleEmail();
            email.setHostName("smtp.gmail.com");
            email.setSmtpPort(465);
            email.setAuthenticator(new DefaultAuthenticator(emailAddress, emailPassword));
            email.setSSLOnConnect(true);
            email.setFrom(emailAddress);
            email.setSubject("DDOS attack detected");
            email.setMsg("DDOS attack detected from " + ip + " on port 8083 at " + timeStamp);
            email.addTo("abbassimelek@gmail.com");
            email.send();
        } catch (EmailException e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
    }
}
