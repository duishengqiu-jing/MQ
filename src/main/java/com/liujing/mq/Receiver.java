package com.liujing.mq;

import com.liujing.mq.queue.Person;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Receiver {
    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                "admin",
                "admin",
                "tcp://172.16.157.132:61616"
        );
        ArrayList<String> trustPackages = new ArrayList<>();
        trustPackages.add(Person.class.getPackage().getName());
        factory.setTrustedPackages(trustPackages);
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Destination dlq = session.createQueue("dlq.new-queue");
        MessageConsumer dlqConsumer = session.createConsumer(dlq);
        dlqConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (message instanceof TextMessage) {
                    try {
                        System.out.println("ActiveMQ.DLQ:"+((TextMessage) message).getText());

                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    message.acknowledge();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        Destination queue = session.createQueue("new-queue");
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    if (message instanceof TextMessage) {
                        System.out.println("consumer textmessage:"+((TextMessage) message).getText());
                    } else if (message instanceof ObjectMessage) {
                        Person person = (Person) ((ObjectMessage) message).getObject();
                        System.out.println("consumer ObjectMessage:"+person.getName()+"\n"+person.getAge()+"\n"+person.getIdCard());
                    } else if (message instanceof BytesMessage) {
                        //顺序不正确就会读取报错，只有按照推送消息的顺序来进行
                        BytesMessage bytesMessage = (BytesMessage) message;
                        FileOutputStream outputStream = new FileOutputStream("/Users/liuchang/Downloads/byte.txt");
                        byte[] bytes = new byte[2048];
                        int len = 0;
                        while ((len = bytesMessage.readBytes(bytes)) != -1) {
                            try {
                                outputStream.write(bytes, 0, len);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
//                        System.out.println("ByteMessage-readUTF:"+((BytesMessage) message).readUTF());
//                        System.out.println("ByteMessage-readBoolean:"+((BytesMessage) message).readBoolean());
//                        System.out.println("ByteMessage-readFloat:"+((BytesMessage) message).readFloat());
//                        System.out.println("ByteMessage-readDouble:"+((BytesMessage) message).readDouble());
//                        System.out.println("ByteMessage-readInt:"+((BytesMessage) message).readInt());
//                        System.out.println("ByteMessage-readLong:"+((BytesMessage) message).readLong());
//                        System.out.println("ByteMessage-readShort:"+((BytesMessage) message).readShort());
                    } else if (message instanceof MapMessage) {
                        boolean aBoolean = ((MapMessage) message).getBoolean("boolean");
                        String string = ((MapMessage) message).getString("string");
                        int anInt = ((MapMessage) message).getInt("int");
                        float aFloat = ((MapMessage) message).getFloat("float");
                        long aLong = ((MapMessage) message).getLong("long");
                        System.out.println("aBoolean:"+aBoolean+",string:"+string+",anInt:"+anInt+",aFloat:"+aFloat+",aLong:"+aLong);
                    }
                    message.acknowledge();
                } catch (JMSException | FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
//        for (int i = 0;i < 100; i++) {
//            Message message = consumer.receive();
//            if (message instanceof TextMessage) {
//                System.out.println("receive:"+((TextMessage) message).getText());
//            }
////            message.acknowledge();
//        }
        Thread.sleep(30000);
        consumer.close();
        session.close();
        connection.close();
    }
}
