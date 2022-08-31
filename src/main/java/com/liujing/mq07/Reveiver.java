package com.liujing.mq07;

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
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Destination queue = session.createQueue("new-queue");

        String selector = "type='P'";
        MessageConsumer consumer = session.createConsumer(queue, selector);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                System.out.println("s:"+message.toString());
                try {
                    MessageProducer producer = session.createProducer(queue);
                    TextMessage textMessage = session.createTextMessage("s 返回");
                    textMessage.setJMSCorrelationID("jmscid1");
                    textMessage.setStringProperty("type", "S");
                    producer.send(textMessage);
                    message.acknowledge();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

    }


}
