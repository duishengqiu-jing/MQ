package com.liujing.mq04;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Reveiver {
    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                "admin",
                "admin",
                "nio://172.16.157.132:5671"
        );
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination queue = session.createQueue("new-queue");
        MessageConsumer consumer = session.createConsumer(queue);
        for (int i = 0;; i++) {
            Message message = consumer.receive();
            if (message instanceof TextMessage) {
                if (message.getJMSCorrelationID().equals("3")) {
                    System.out.println("receiver JMSCorrelationID="+3);
                } else {
                    System.out.println("receiver:"+((TextMessage) message).getText());
                }
            }
        }
    }
}
