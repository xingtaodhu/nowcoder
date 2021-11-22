package com.xingtao.newcoder.service;

import com.xingtao.newcoder.dao.MessageMapper;
import com.xingtao.newcoder.entity.Message;
import com.xingtao.newcoder.utils.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<Message> findConversations(int id, int offset, int limit) {
        return messageMapper.selectConversations(id,offset,limit);

    }

    public int findConversationCount(int userId) {
        return messageMapper.selectConversationCount(userId);
    }

    public int findLetterCount(String conversationId) {
        return messageMapper.selectLetterCount(conversationId);
    }

    public int findLetterUnreadCount(int id, String conversationId) {
        return messageMapper.selectLetterUnreadCount(id,conversationId);
    }

    public List<Message> findLetters(String conversationId, int offset, int limit) {
        return messageMapper.selectLetters(conversationId,offset,limit);
    }

    public int addMessage(Message message) {
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));
        return messageMapper.insertMessage(message);
    }



    public int  readMessage(List<Integer> ids) {
        return messageMapper.updateStatus(ids,1);
    }
}
