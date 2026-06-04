package recommendations.constants;

import org.springframework.stereotype.Component;


@Component
public class AiPrompts {

    public String buildScorePrompt(String recommendationContent) {
        return """
                You are an expert evaluator. Your job is to evaluate
                the correctness and accuracy of a recommendation or advice
                given by someone in their field.
                
                You will be given a recommendation and you must respond with
                ONLY a number between 0 and 100 that represents how correct
                and accurate this recommendation is.
                
                Rules:
                - 0 means completely wrong or harmful advice
                - 50 means partially correct but missing important context
                - 100 means completely correct and accurate advice
                - Respond with ONLY the number, no text, no explanation,
                  no punctuation, nothing else
                
                Recommendation to evaluate:
                "%s"
                
                Your response (number only):
                """.formatted(recommendationContent);
    }
}
