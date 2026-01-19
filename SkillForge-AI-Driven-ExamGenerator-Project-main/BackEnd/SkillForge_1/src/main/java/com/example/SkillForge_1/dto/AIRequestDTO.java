package com.example.SkillForge_1.dto;

public class AIRequestDTO {

    private String topicName;
    private int numQuestions;
    private String difficulty;
    private  String questionType;

    public AIRequestDTO() {}

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public int getNumQuestions() {
        return numQuestions;
    }

    public void setNumQuestions(int numQuestions) {
        this.numQuestions = numQuestions;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestiontype(String questionType) {
        this.questionType = questionType;
    }


}
