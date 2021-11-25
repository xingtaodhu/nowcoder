package com.xingtao.newcoder.controller;

import com.google.code.kaptcha.Producer;
import com.xingtao.newcoder.entity.Comment;
import com.xingtao.newcoder.entity.DiscussPost;
import com.xingtao.newcoder.entity.Event;
import com.xingtao.newcoder.event.EventProducer;
import com.xingtao.newcoder.service.CommentService;
import com.xingtao.newcoder.service.DiscussPostService;
import com.xingtao.newcoder.utils.CommunityConstant;
import com.xingtao.newcoder.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * @author coolsen
 * @version 1.0.0
 * @ClassName CommentController.java
 * @Description Comment
 * @createTime 5/7/2020 5:47 PM
 */

@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private DiscussPostService discussPostService;

    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment) {
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        //触发评论事件
        Event event = new Event()
                .setTopic(TOPIC_COMMENT)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setData("postId",discussPostId);

          if(comment.getEntityType() == ENTITY_TYPE_POST){
              DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
              event.setEntityUserId(target.getUserId());
          }else if(comment.getEntityType() == ENTITY_TYPE_COMMENT){
              Comment target = commentService.findCommentById(comment.getEntityId());
              event.setEntityUserId(target.getUserId());
          }
          eventProducer.fireEvent(event);

//        // 触发评论事件
//        Event event = new Event()
//                .setTopic(TOPIC_COMMENT)
//                .setUserId(hostHolder.getUser().getId())
//                .setEntityType(comment.getEntityType())
//                .setEntityUserId(comment.getEntityId())
//                .setData("postId", discussPostId);

//        if (comment.getEntityType() == ENTITY_TYPE_COMMENT) {
//            DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
//            event.setEntityUserId(target.getUserId());
//        } else if (comment.getEntityType() == ENTITY_TYPE_COMMENT) {
//            Comment target = commentService.findCommentById(comment.getEntityId());
//            event.setEntityUserId(target.getUserId());
//        }
//        eventProducer.fireEvent(event);
//
//        // 触发发帖事件，因为评论帖子时，帖子的评论数量就更改了，需要更新elasticsearch中的数据
//        if(comment.getEntityType()==ENTITY_TYPE_POST){
//            event=new Event()
//                    .setTopic(TOPIC_PUBLISH)
//                    .setUserId(hostHolder.getUser().getId())
//                    .setEntityType(ENTITY_TYPE_POST)
//                    .setEntityId(discussPostId);
//            eventProducer.fireEvent(event);
//        }

        return "redirect:/discuss/detail/" + discussPostId;
    }
}
