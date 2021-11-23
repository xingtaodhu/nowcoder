package com.xingtao.newcoder.controller;

import com.xingtao.newcoder.entity.User;
import com.xingtao.newcoder.service.LikeService;
import com.xingtao.newcoder.utils.CommunityUtil;
import com.xingtao.newcoder.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(value = "/like",method = RequestMethod.POST)
    @ResponseBody
    public String like(int entityType,int entityId,int entityUserId){

        User user = hostHolder.getUser();
        likeService.like(user.getId(),entityType,entityId,entityUserId);

        long entityLikeCount = likeService.findEntityLikeCount(entityType, entityId);
        int entityLikeStatus = likeService.findEntityLikeStatus(entityType, entityId, user.getId());

        //返回的结果
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", entityLikeCount);
        map.put("likeStatus", entityLikeStatus);

        return CommunityUtil.getJSONString(0, null, map);
    }
}
