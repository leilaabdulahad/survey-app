package com.example.survey.controller;

import com.example.survey.model.Survey;
import com.example.survey.service.SurveyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    private final SurveyService surveyService;

    // Constructor injection automatically handles dependency injection
    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    // Create a new survey
    @PostMapping
    public ResponseEntity<Survey> createSurvey(@RequestBody Survey survey) {
        Survey createdSurvey = surveyService.createSurvey(survey);
        return ResponseEntity.ok(createdSurvey);
    }

    // Get all surveys
    @GetMapping
    public ResponseEntity<List<Survey>> getAllSurveys() {
        List<Survey> surveys = surveyService.getAllSurveys();
        return ResponseEntity.ok(surveys);
    }

    // Get a survey by ID
    @GetMapping("/{id}")
    public ResponseEntity<Survey> getSurveyById(@PathVariable Long id) {
        Survey survey = surveyService.getSurveyById(id);
        return ResponseEntity.ok(survey);
    }

    // Update an existing survey
    @PutMapping("/{id}")
    public ResponseEntity<Survey> updateSurvey(@PathVariable Long id, @RequestBody Survey survey) {
        Survey updatedSurvey = surveyService.updateSurvey(id, survey);
        return ResponseEntity.ok(updatedSurvey);
    }

    // Delete a survey by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable Long id) {
        surveyService.deleteSurvey(id);
        return ResponseEntity.noContent().build();
    }
}
