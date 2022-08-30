package com.liujing.mq04;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Sender {
    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                "admin",
                "admin",
                "nio://172.16.157.132:5671"
        );
        Connection connection = factory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination queue = session.createQueue("new-queue");
        MessageProducer producer = session.createProducer(queue);
        for (int i = 0; i < 10; i++) {
            TextMessage textMessage = session.createTextMessage("hello");
            textMessage.setJMSCorrelationID(""+i);
            producer.send(textMessage);
            System.out.println("producer:"+textMessage.getText());
        }
    }
}
