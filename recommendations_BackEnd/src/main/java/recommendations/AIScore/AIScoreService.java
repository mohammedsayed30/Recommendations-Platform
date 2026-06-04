package recommendations.AIScore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recommendations.constants.AiPrompts;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIScoreService {

    private final AiPrompts aiPrompts;

    private final ChatClient chatClient;

    public Integer getRecommendationAIScore(String recommendationContent) {
        return this.makeAIPrompt(recommendationContent);
    }

    //build the prompt + call the AI agent (Gemini) and get the response
    private Integer makeAIPrompt(String recommendationContent) {
        String prompt = aiPrompts.buildScorePrompt(recommendationContent);

        //call the AI Agent
        Integer AIScore = this.callAIAgent(prompt);

        //return the AIScore
        return AIScore;
    }

    private Integer callAIAgent(String prompt) {

        try {
            String response = chatClient
                    .prompt()
                    .user(prompt)
                    .call()
                    .content();

            log.info("Response from Gemini: {}", response);
            return Integer.parseInt(response.trim());

        } catch (Exception e) {
            log.error("Gemini error full details: {}", e.getMessage(), e);
            throw e;
        }
    }
}
