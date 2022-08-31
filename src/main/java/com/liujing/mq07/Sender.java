package com.liujing.mq07;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMessage;

import javax.jms.*;
import java.text.SimpleDateFormat;
import java.util.Date;

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
                ActiveMQMessage activeMQMessage = (ActiveMQMessage) message;
                long timestamp = activeMQMessage.getTimestamp();
                long brokerInTime = activeMQMessage.getBrokerInTime();
                long brokerOutTime = activeMQMessage.getBrokerOutTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss SSSS");
                System.out.println("timestamp:"+simpleDateFormat.format(new Date(timestamp)));
                System.out.println("brokerInTime:"+simpleDateFormat.format(new Date(brokerInTime)));
                System.out.println("brokerOutTime:"+simpleDateFormat.format(new Date(brokerOutTime)));
                try {
                    message.acknowledge();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
