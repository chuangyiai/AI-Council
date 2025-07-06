package com.aides.service.lmpl;

import com.aides.service.AIParliamentService;
import org.springframework.stereotype.Service;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

@Service
public class AIParliamentServiceImpl implements AIParliamentService {
    private static final String OLLAMA_BASE_URL = "http://localhost:11434";
    private static final ObjectMapper mapper = new ObjectMapper();
    
    private static final List<String> MODELS = Arrays.asList(
        "deepseek",
        "qwen",
        "doubao"
    );

    public String processQuestion(String question) throws Exception {
        // 第一阶段：生成简报
        List<String> summaries = generateSummaries(question);
        
        // 第二阶段：投票选举最佳简报
        String bestSummary = voteForBestSummary(question, summaries);
        
        // 第三阶段：生成最终答复
        return generateFinalAnswer(question, bestSummary);
    }
    
    private List<String> generateSummaries(String question) throws Exception {
        List<String> summaries = new ArrayList<>();
        for (String model : MODELS) {
            summaries.add(callModel(model, question, "请生成20字以内的简报："));
        }
        return summaries;
    }
    
    private String voteForBestSummary(String question, List<String> summaries) throws Exception {
        String prompt = "请从以下简报中投票选出最合适的：\n" + 
            String.join("\n", summaries) + 
            "\n\n请说明你选择的简报编号(1-3)和简短理由：";
            
        Map<String, Integer> votes = new HashMap<>();
        for (String model : MODELS) {
            String response = callModel(model, question, prompt);
            int votedIndex = parseVoteResponse(response);
            votes.put(summaries.get(votedIndex), votes.getOrDefault(summaries.get(votedIndex), 0) + 1);
        }
        
        return Collections.max(votes.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
    
    private String generateFinalAnswer(String question, String bestSummary) throws Exception {
        String prompt = "基于以下问题和选出的简报，生成详细答复：\n" +
            "问题：" + question + "\n" +
            "简报：" + bestSummary;
            
        return callModel(MODELS.get(0), question, prompt);
    }
    
    private String callModel(String model, String question, String prompt) throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(OLLAMA_BASE_URL + "/api/generate");
            
            Map<String, Object> request = new HashMap<>();
            request.put("model", model);
            request.put("prompt", prompt + "\n" + question);
            request.put("max_tokens", 50);
            
            post.setEntity(new StringEntity(mapper.writeValueAsString(request)));
            post.setHeader("Content-Type", "application/json");
            
            return EntityUtils.toString(client.execute(post).getEntity());
        }
    }
    
    private int parseVoteResponse(String response) {
        return Integer.parseInt(response.substring(3, 4)) - 1;
    }
}