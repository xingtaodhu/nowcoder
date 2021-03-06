package com.xingtao.newcoder;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author coolsen
 * @version 1.0.0
 * @ClassName KafkaTests.java
 * @Description Kafka Test
 * @createTime 2020/5/14 13:14
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = NewcoderApplication.class)
public class KafkaTests {

    @Autowired
    private KafkaProducer  kafkaProducer;


    @Test
    public void testKafka() {

        kafkaProducer.sendMessage("publish", "你好");
        kafkaProducer.sendMessage("test", "在吗");
        kafkaProducer.sendMessage("test1","ooo");

        try {
            Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

@Component
class KafkaProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;


    public void sendMessage(String topic, String content) {
        kafkaTemplate.send(topic, content);
    }

}
@Component
class KafkaConsumer {

    @KafkaListener(topics = {"publish"})
    public void handleMessage(ConsumerRecord record) {
        System.out.println(record.value());
    }

}