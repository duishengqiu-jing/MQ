package com.liujing.mq07;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Sender {
    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                "admin",
                "admin",
                "nio://172.16.157.132:5671"
        );
        ActiveMQConnection connection = (ActiveMQConnection) factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Destination queue = session.createQueue("new-queue");

        MessageProducer producer = session.createProducer(queue);
        TextMessage textMessage = session.createTextMessage("testddddmessage");
        textMessage.setJMSCorrelationID("jmscid1");
        textMessage.setStringProperty("type", "P");
        producer.send(textMessage);

        String selector = "type = 'S'";
        MessageConsumer consumer = session.createConsumer(queue, selector);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                System.out.println("ps:"+message.toString());
                try {
                    MessageProducer producer1 = session.createProducer(queue);
                    TextMessage textMessage1 = session.createTextMessage("ps发送");
                    textMessage1.setJMSCorrelationID(message.getJMSCorrelationID());
//                    textMessage1.setStringProperty("type", "P");
                    producer1.send(textMessage1);
                    message.acknowledge();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
