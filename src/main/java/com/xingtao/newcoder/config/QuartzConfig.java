package com.xingtao.newcoder.config;

import com.xingtao.newcoder.quartz.AlphaJob;
import com.xingtao.newcoder.quartz.PostScoreRefreshJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import java.util.Date;

@Configuration
public class QuartzConfig {
    //     FactoryBean可简化Bean的实例化过程:
//     1.通过FactoryBean封装Bean的实例化过程.
//     2.将FactoryBean装配到Spring容器里.
//     3.将FactoryBean注入给其他的Bean.
//     4.该Bean得到的是FactoryBean所管理的对象实例.


    // 配置JobDetail
    // 此处为demo，之后不再使用，所以注释了@Bean
//    @Bean
    public JobDetailFactoryBean alphaJobDetail() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(AlphaJob.class);
        factoryBean.setName("alphaJob");
        factoryBean.setGroup("alphaJobGroup");
        factoryBean.setDurability(true);
        factoryBean.setRequestsRecovery(true);//任务出现问题时是否恢复
        return  factoryBean;
    }

    // 配置Trigger
//     @Bean
    public SimpleTriggerFactoryBean alphaTrigger(JobDetail alphaJobDetail){
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(alphaJobDetail);
        factoryBean.setName("alphaTrigger");
        factoryBean.setGroup("alphaTriggerGroup");
        factoryBean.setStartTime(new Date(System.currentTimeMillis()+5000));
        factoryBean.setRepeatInterval(3000);
        factoryBean.setJobDataMap(new JobDataMap());//存储当前Job状态
        return factoryBean;
    }

    // 刷新帖子分数任务
    @Bean
    public JobDetailFactoryBean postScoreRefreshJobDetail() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(PostScoreRefreshJob.class);
        factoryBean.setName("postScoreRefreshJob");
        factoryBean.setGroup("communityJobGroup");
        factoryBean.setDurability(true);
        factoryBean.setRequestsRecovery(true);
        return  factoryBean;
    }

    @Bean
    public SimpleTriggerFactoryBean postScoreRefreshTrigger(JobDetail postScoreRefreshJobDetail){
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(postScoreRefreshJobDetail);
        factoryBean.setName("postScoreRefreshTrigger");
        factoryBean.setGroup("communityJobGroup");
        factoryBean.setRepeatInterval(1000*60*1);
        factoryBean.setJobDataMap(new JobDataMap());
        return factoryBean;
    }
}
