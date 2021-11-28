package com.xingtao.newcoder.controller;

import com.xingtao.newcoder.entity.DiscussPost;
import com.xingtao.newcoder.entity.Page;
import com.xingtao.newcoder.service.ElasticsearchService;
import com.xingtao.newcoder.service.LikeService;
import com.xingtao.newcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author coolsen
 * @version 1.0.0
 * @ClassName SearchController.java
 * @Description Search Controller
 * @createTime 2020/5/19 16:41
 */

@Controller
public class SearchController {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    // search?keyword=xxx
    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public String search(String keyword, Page page, Model model) {
        // 搜索帖子
        org.springframework.data.domain.Page<DiscussPost> searchResults = elasticsearchService.searchDiscussPost(keyword, page.getCurrent() - 1, page.getLimit());

        // 聚合数据
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (searchResults != null) {
            for (DiscussPost post : searchResults) {
                Map<String, Object> map = new HashMap<>();

                // 帖子
                map.put("post", post);
                // 作者
                map.put("user", userService.findUserById(post.getUserId()));
                // 点赞数量
                map.put("likeCount", likeService.findUserLikeCount(post.getUserId()));

                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        model.addAttribute("keyword", keyword);

        ///分页信息
        page.setPath("search?keyword=" + keyword);
        page.setRows(searchResults == null ? 0 : (int) searchResults.getTotalElements());

        return "/site/search";
    }
}
