package com.yupi.mcp.mcpserver.service;
import com.alibaba.fastjson.JSON;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.yupi.mcp.mcpserver.dto.BaseResponse;
import com.yupi.mcp.mcpserver.pojo.Card;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;


@Slf4j
@Service
public class MianshiyaService {

    @Value("${endpoint.mianshiya.searchQuestion}")
    private String baseUrl;

    @Value("${endpoint.mianshiya.resultLink}")
    private String resultLink;



    @Tool(description = "当用户要查询帖子和作品时使用这个方法")
    public String questionSearch(String searchText) {
        String result = null;
        try {
            HttpResponse response = HttpRequest.get(baseUrl+"?key="+searchText).timeout(5000).execute();
            result = response.body();
            log.info("result = {}", result);
            if (response.getStatus() != 200) {
                log.error("response = {}", result);
                return String.format("搜索服务异常，状态码[%s]", response.getStatus());
            }

            BaseResponse<?> resp = JSONUtil.toBean(result, BaseResponse.class);
            int code = resp.getCode();
            if (code == 1) {
                log.info(" code = {}", code);
                log.info(resp.getData().toString());
                List<Card> cards = JSON.parseArray(resp.getData().toString(), Card.class);
                if (cards.isEmpty()) {
                    return "无搜索结果";
                } else {
                    int i = 0;
                    String text="";
                    for (Card card : cards) {
                        if (i++ >= 5) {
                            break;
                        }
                        String title = card.getTitle();
                        Integer id = card.getId();
                        String link = String.format(resultLink, card.getAuthorId());
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
                }

            } else {
                log.error("call mianshiya failed, code = {}, message = {}", code, resp.getMsg());
                return String.format("搜索服务异常，响应码[%s]", code);
            }
        } catch (Exception e) {
            log.error("call mianshiya failed, e:\n", e);
            return String.format("调用搜索服务失败，异常[%s]", e.getMessage());
        }
    }
}
