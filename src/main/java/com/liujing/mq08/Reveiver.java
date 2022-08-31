package com.liujing.mq08;

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
        Destination queue = session.createTopic("new-queue?consumer.retroactive=true");
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                System.out.println("s:"+message.toString());
            }
        });
    }


}
