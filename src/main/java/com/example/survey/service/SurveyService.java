package com.example.survey.service;

import com.example.survey.model.Survey;
import com.example.survey.repository.SurveyRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SurveyService {
    private final SurveyRepository surveyRepository;

    public SurveyService(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    public Survey createSurvey(Survey survey) {
        validateSurveyDates(survey);
        survey.setActive(true); // Set default active status
        return surveyRepository.save(survey);
    }

    public List<Survey> getAllSurveys() {
        return surveyRepository.findAll();
    }

    public Survey getSurveyById(Long id) {
        Optional<Survey> survey = surveyRepository.findById(id);
        return survey.orElse(null);
    }

    public Survey updateSurvey(Long id, Survey surveyDetails) {
        Optional<Survey> existingSurvey = surveyRepository.findById(id);
        if (existingSurvey.isPresent()) {
            Survey updatedSurvey = existingSurvey.get();
            
            // Update basic information
            updatedSurvey.setTitle(surveyDetails.getTitle());
            updatedSurvey.setDescription(surveyDetails.getDescription());
            
            // Update metadata
            updatedSurvey.setActive(surveyDetails.isActive());
            updatedSurvey.setCategory(surveyDetails.getCategory());
            updatedSurvey.setTargetResponses(surveyDetails.getTargetResponses());
            
            // Update dates
            if (surveyDetails.getStartDate() != null) {
                updatedSurvey.setStartDate(surveyDetails.getStartDate());
            }
            if (surveyDetails.getEndDate() != null) {
                updatedSurvey.setEndDate(surveyDetails.getEndDate());
            }
            
            validateSurveyDates(updatedSurvey);
            
            return surveyRepository.save(updatedSurvey);
        }
        return null;
    }

    public void deleteSurvey(Long id) {
        surveyRepository.deleteById(id);
    }

    // Helper method to validate survey dates
    private void validateSurveyDates(Survey survey) {
        LocalDateTime startDate = survey.getStartDate();
        LocalDateTime endDate = survey.getEndDate();
        
        if (startDate != null && endDate != null) {
            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("Start date must be before end date");
            }
            if (endDate.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("End date cannot be in the past");
            }
        }
    }

    // Additional helper methods you might want to add:
    
    public List<Survey> getActiveSurveys() {
        return surveyRepository.findAll().stream()
                .filter(Survey::isActive)
                .toList();
    }

    public void deactivateSurvey(Long id) {
        Optional<Survey> survey = surveyRepository.findById(id);
        survey.ifPresent(s -> {
            s.setActive(false);
            surveyRepository.save(s);
        });
    }
}