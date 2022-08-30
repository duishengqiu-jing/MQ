package com.liujing.mq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Date;

public class QueueReceiver3 {
    public static void main(String[] args) throws JMSException, InterruptedException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                "admin",
                "admin",
                "tcp://172.16.157.132:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

        Destination destination = session.createQueue("testlj");
        String selector = "age > 5";
        MessageConsumer consumer = session.createConsumer(destination, selector);
        for (int i = 0; ; i++) {
            // 阻塞 socket.accept 在这个方法被调用的时候就一直卡着直到有连接进入这个连接才会进入下一步
            // 这个receive也一样，直到有消息过来才会执行下面的，在这里只有一个线程不能支持高并发
            // 或者IO出现瓶颈，大文件等第二条消息就很难被消费到，即阻塞住，同步执行的，非异步的
            // 死循环及其影响开发效率和系统性能，不能单线程，除非处理单个计算非常简单， 不然处理不完，
            // 还有IO如果这个消息比如有500M，接过来第二条没办法处理了，所以这里面改为另外方式
            // 消费过也不一定删除，比如设定了重复投递
            Message message = consumer.receive();
//            Thread.sleep(1000);
            if (message instanceof TextMessage) {
                System.out.println(new Date()+"TextMessage:"+message.toString());
                message.acknowledge();
            } else if(message instanceof MapMessage) {
                System.out.println(message.toString());
                message.acknowledge();
            }
//            if ( i % 3 == 0){
//                session.commit();
//            } else {
//                session.rollback();
//            }
//            session.commit();
        }
//        while (true) {
//            Message message = consumer.receive();
//            System.out.println("--------------------");
//            if (message instanceof TextMessage) {
//                System.out.println("destination:"+destination.toString()+"TextMessage:"+((TextMessage) message).getText());
//            }
//        }
//        consumer.close();
//        session.close();
//        connection.close();
    }
}
