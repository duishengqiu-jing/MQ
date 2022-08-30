package com.liujing.mq02;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class BrowserSender {
    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                "admin",
                "admin",
//                "tcp://172.16.157.132:61616"
                "tcp://172.16.157.132:5671"
        );
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination queue = session.createQueue("new-queue");
        MessageProducer producer = session.createProducer(queue);
        TextMessage textMessage = session.createTextMessage("hello");
        producer.send(textMessage);


    }
}
