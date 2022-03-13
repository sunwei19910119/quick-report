//package com.xhtt.config;
//
//
//import org.springframework.amqp.rabbit.annotation.EnableRabbit;
//import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@EnableRabbit
//public class RabbitProducerConfig {
//    @Value("${spring.rabbitmq.host}")
//    private String host;
//
//    @Value("${spring.rabbitmq.port}")
//    private int port;
//
//    @Value("${spring.rabbitmq.username}")
//    private String username;
//
//    @Value("${spring.rabbitmq.password}")
//    private String password;
//
//    @Value("${spring.rabbitmq.virtual-host}")
//    private String virtualHosthost;
//
//    @Bean
//    public CachingConnectionFactory connectionFactory() {
//
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
//        connectionFactory.setUsername(username);
//        connectionFactory.setPassword(password);
//        connectionFactory.setVirtualHost(virtualHosthost);
//
//        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);//开启 消息发送到queue模式，发送到了就会回调 setConfirmCallback
////        connectionFactory.setPublisherReturns(true);//确认被消费后回调
//        return connectionFactory;
//    }
//
//    @Bean
//    public RabbitTemplate rabbitTemplate() {
//        RabbitTemplate template = new RabbitTemplate(connectionFactory());
//        //template.setDefaultReceiveQueue(queue);//设置默认接收队列
//        template.setConfirmCallback((data, ack, cause) -> {
//            if (!ack) {
//                System.out.println("消息发送失败!" + cause + data.toString());
//            } else {
//                System.out.println("消息发送成功,消息ID：" + (data != null ? data.getId() : null));
//            }
//        });
//        return template;
//    }
//}
