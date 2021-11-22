package com.xingtao.newcoder;

import com.xingtao.newcoder.utils.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = NewcoderApplication.class)
public class SenstiveTests {
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void test1(){
        String filter = sensitiveFilter.filter("ǒòò我喜欢à吸ǎ毒ā，我喜欢ㄆ嫖+娼ō");
        System.out.println(filter);
    }
}
