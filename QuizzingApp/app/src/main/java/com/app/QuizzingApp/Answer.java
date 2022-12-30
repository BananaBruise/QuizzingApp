package com.app.QuizzingApp;

public class Answer {
    private String prompt;
    private Boolean isCorrect;

    public Answer(String prompt, Boolean isCorrect){
        this.prompt = prompt;
        this.isCorrect = isCorrect;
    }

    public Answer(){
        this("Example prompt; please change me.", false);
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
