package com.xingtao.newcoder.service;

import com.xingtao.newcoder.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    private RedisTemplate redisTemplate;


    //点赞
    public void like(int userId,int entityType,int entityId,int entityUserId){
//        String key = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
//        Boolean member = redisTemplate.opsForSet().isMember(key, userId);
//        if(member){
//            redisTemplate.opsForSet().remove(key,userId);
//        }else{
//            redisTemplate.opsForSet().add(key,userId);
//        }
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);
                Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
                operations.multi();

                if(isMember){
                    redisTemplate.opsForSet().remove(entityLikeKey,userId);
                    redisTemplate.opsForValue().decrement(userLikeKey);
                }else{
                    redisTemplate.opsForSet().add(entityLikeKey,userId);
                    redisTemplate.opsForValue().increment(userLikeKey);
                }

                return operations.exec();
            }
        });
    }

    /**
     * @Description: 查询某个用户获得赞，用于在个人主页查看收获了多少赞
     * @param userId
     * @return: int
     * @Date 2020/5/12
     **/
    public int findUserLikeCount(int userId){
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count==null?0:count.intValue();// count.intValue()数据的整数形式;
    }

    //查询某实体点赞的数量
    public long findEntityLikeCount(int entityType,int entityId){
        String key = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * @Description:查询某人对某实体的点赞状态
     * @param entityType
     * @param entityId
     * @param userId
     * @return: int
     * @Date 5/9/2020
     **/
    public int findEntityLikeStatus(int entityType,int entityId,int userId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        //此处返回int，是为了进行扩展。比如扩展踩，为止2.等等情况
        return redisTemplate.opsForSet().isMember(entityLikeKey,userId)?1:0;
    }



}
