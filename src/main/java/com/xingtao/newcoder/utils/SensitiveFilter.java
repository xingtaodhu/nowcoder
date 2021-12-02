package com.xingtao.newcoder.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j(topic = "c.SenstiveFilter")
public class SensitiveFilter {

    //前缀树根结点
    private TrieNode root= new TrieNode();
    // 替换符
    private static final String REPLACEMENT = "***";

    @PostConstruct
    //启动构造内部前缀树
    public void init(){
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("senstive-words.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String keyword;
        try {
            while((keyword = br.readLine())!=null) {
                addKeyword(keyword);
            }

        } catch (IOException e) {
            e.printStackTrace();
            log.error("加载敏感词文件失败: " + e.getMessage());
        }

    }


    //添加一个关键词到前缀树中
    private void addKeyword(String keyword) {
        TrieNode temp = root;
        for (int i = 0; i < keyword.length(); i++) {
            Character c = keyword.charAt(i);
            TrieNode subNode = temp.getSubNode(c);
            if(subNode == null){
                subNode = new TrieNode();
                temp.addSubNode(c,subNode);
            }
            temp = subNode;

        }
        temp.setKeywordEnd(true);
    }

    /**
     * 过滤敏感词
     *
     * @param text 待过滤的文本
     * @return 过滤后的文本
     */
    public String filter(String text) {
        if(StringUtils.isBlank(text)){
            return null;
        }
        TrieNode temp = root;
        int start = 0;
        int end = 0;
        StringBuilder sb = new StringBuilder();
        while(end!=text.length()){
            Character c = text.charAt(end);
            if(isSymbol(c)){
                if(temp == root){
                    sb.append(c);
                    start++;
                }
                end++;
                continue;
            }
            TrieNode subNode = temp.getSubNode(c);
            if(subNode == null){
                sb.append(text.charAt(start));
                temp = root;
                end = ++start;
            }else if(subNode.isKeywordEnd()){
                sb.append(REPLACEMENT);
                start=++end;
            }else{
                temp = subNode;
                end++;
            }
        }
        return sb.toString();

    }

    // 判断是否为符号
    private boolean isSymbol(Character c) {
        // 0x2E80~0x9FFF 是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }


    // 前缀树
    private class TrieNode {

        // 关键词结束标识
        private boolean isKeywordEnd = false;

        // 子节点(key是下级字符,value是下级节点)
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        // 添加子节点
        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        // 获取子节点
        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }

    }
}
