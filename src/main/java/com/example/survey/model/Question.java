package com.example.survey.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String questionText;
    
    @ManyToOne
    @JoinColumn(name = "survey_id")
    @JsonBackReference
    private Survey survey;
    
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Option> options;
        
    public Question() {
    }
    
    public Question(String questionText, Survey survey) {
        this.questionText = questionText;
        this.survey = survey;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getQuestionText() {
        return questionText;
    }
    
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
    
    public Survey getSurvey() {
        return survey;
    }
    
    public void setSurvey(Survey survey) {
        this.survey = survey;
    }
    
    public List<Option> getOptions() {
        return options;
    }
    
    public void setOptions(List<Option> options) {
        this.options = options;
    }
}