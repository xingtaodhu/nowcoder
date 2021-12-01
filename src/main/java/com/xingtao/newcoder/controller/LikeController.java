package com.xingtao.newcoder.controller;

import com.xingtao.newcoder.entity.Event;
import com.xingtao.newcoder.entity.User;
import com.xingtao.newcoder.event.EventProducer;
import com.xingtao.newcoder.service.LikeService;
import com.xingtao.newcoder.utils.CommunityConstant;
import com.xingtao.newcoder.utils.CommunityUtil;
import com.xingtao.newcoder.utils.HostHolder;
import com.xingtao.newcoder.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController implements CommunityConstant {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/like",method = RequestMethod.POST)
    @ResponseBody
    public String like(int entityType,int entityId,int entityUserId,int postId){

        User user = hostHolder.getUser();
        likeService.like(user.getId(),entityType,entityId,entityUserId);

        long entityLikeCount = likeService.findEntityLikeCount(entityType, entityId);
        int entityLikeStatus = likeService.findEntityLikeStatus(entityType, entityId, user.getId());

        if(entityLikeStatus==1){
            Event event = new Event()
                    .setTopic(TOPIC_LIKE)
                    .setUserId(user.getId())
                    .setEntityType(entityType)
                    .setEntityId(entityId)
                    .setEntityUserId(entityUserId)
                    .setData("postId",postId);
            eventProducer.fireEvent(event);
        }

        if (entityType == ENTITY_TYPE_POST) {
            // 计算帖子分数
            String redisKey = RedisKeyUtil.getPostScoreKey();
            redisTemplate.opsForSet().add(redisKey, postId);
        }
        //返回的结果
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", entityLikeCount);
        map.put("likeStatus", entityLikeStatus);

        return CommunityUtil.getJSONString(0, null, map);
    }
}
