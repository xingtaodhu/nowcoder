package com.xingtao.newcoder.controller.interceptor;

import com.xingtao.newcoder.entity.User;
import com.xingtao.newcoder.service.MessageService;
import com.xingtao.newcoder.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author coolsen
 * @version 1.0.0
 * @ClassName MessageInterceptor.java
 * @Description 消息拦截器，为了在首页显示未读消息数量
 * @createTime 2020/5/15 15:28
 */

@Component
public class MessageInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private MessageService messageService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            int noticeUnreadCount = messageService.findNoticeUnreadCount(user.getId(), null);
            int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
            modelAndView.addObject("allUnreadCount", noticeUnreadCount + letterUnreadCount);
        }
    }
}