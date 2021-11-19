package com.xingtao.newcoder.service;

import com.xingtao.newcoder.dao.UserMapper;
import com.xingtao.newcoder.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

}