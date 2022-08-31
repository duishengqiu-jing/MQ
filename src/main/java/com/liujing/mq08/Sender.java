package com.liujing.mq08;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

public class Sender {
    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                "admin",
                "admin",
                "nio://172.16.157.132:5671"
        );
        ActiveMQConnection connection = (ActiveMQConnection) factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination queue = session.createTopic("new-queue");

        MessageProducer producer = session.createProducer(queue);
        for (int i = 0; i < 10; i++) {
            TextMessage textMessage = session.createTextMessage("testddddmessage");
            textMessage.setJMSCorrelationID("jmscid1");
            textMessage.setStringProperty("type", "P");
            producer.send(textMessage);
        }
    }
}
