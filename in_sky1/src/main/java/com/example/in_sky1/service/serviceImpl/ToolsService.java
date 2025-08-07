package com.example.in_sky1.service.serviceImpl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.example.in_sky1.pojo.Card;
import com.example.in_sky1.service.CardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ToolsService {
    @Autowired
    private CardService cardService;
    @Tool(description = "当用户要查询帖子和作品时使用这个方法")
    public String questionSearch(String searchText) {
        String result = null;
        try {
            List<Card> cards = cardService.getCardByKey(searchText, null);
            log.info("result = {}", result);
                    int i = 0;
                    String text="";
                    for (Card card : cards) {
                        if (i++ >= 5) {
                            break;
                        }
                        String title = card.getTitle();
                        Integer id = card.getId();
                        String link = String.format("visitor/%s", card.getAuthorId());
                        String mediaUrl=card.getPictureUrl();
                        String fileType=card.getFileType();
                        if(fileType.startsWith("video/"))
                        {
                            text+="<a href ='"+link+"' style='margin-right: 95px;text-decoration: none;'>"+title+"</a>"+"<video src='"+mediaUrl+"' controls style='width:150px;height:150px;object-fit:cover;'></video>";
                        }else{
                            text+="<a href ='"+link+"' style='margin-right: 95px;text-decoration: none;'>"+title+"</a>"+"<img src='"+mediaUrl+"' style='width:150px; height:150px;'>";
                        }
                    }
                    System.out.println(text);
                    return text;
                }catch (Exception e){
                    return "没有找到结果";
        }
        }
}
