package com.example.survey.service;

import com.example.survey.model.Survey;
import com.example.survey.repository.SurveyRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SurveyService {
    private final SurveyRepository surveyRepository;

    // Logger for debugging information
    private static final Logger logger = LoggerFactory.getLogger(SurveyService.class);

    public SurveyService(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    public Survey createSurvey(Survey survey) {
        validateSurveyDates(survey);
        survey.setActive(true); 
        logger.debug("Creating survey: {}", survey);
        return surveyRepository.save(survey);
    }

    public List<Survey> getAllSurveys() {
        logger.debug("Fetching all surveys");
        return surveyRepository.findAll();
    }

    public Survey getSurveyById(Long id) {
        logger.debug("Fetching survey by id: {}", id);
        Optional<Survey> survey = surveyRepository.findById(id);
        return survey.orElse(null);
    }

    public Survey updateSurvey(Long id, Survey surveyDetails) {
        logger.debug("Updating survey with id: {}", id);
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
        logger.debug("Deleting survey with id: {}", id);
        surveyRepository.deleteById(id);
    }

    private void validateSurveyDates(Survey survey) {
        LocalDateTime startDate = survey.getStartDate();
        LocalDateTime endDate = survey.getEndDate();

        // Check if start date is before end date
        if (startDate != null && endDate != null) {
            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("Start date must be before end date");
            }
        }

        // Ensure end date is not in the past
        if (endDate != null && endDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("End date must be in the future");
        }

        // Ensure survey duration is at least one day
        if (startDate != null && endDate != null && startDate.plusDays(1).isAfter(endDate)) {
            throw new IllegalArgumentException("Survey duration must be at least one day");
        }
        
        // Automatically close survey if end date has passed
        if (endDate != null && endDate.isBefore(LocalDateTime.now())) {
            survey.setActive(false);
        }
    }
    
    public List<Survey> getActiveSurveys() {
        logger.debug("Fetching active surveys");
        return surveyRepository.findAll().stream()
                .filter(Survey::isActive)
                .toList();
    }

    public void deactivateSurvey(Long id) {
        logger.debug("Deactivating survey with id: {}", id);
        Optional<Survey> survey = surveyRepository.findById(id);
        survey.ifPresent(s -> {
            s.setActive(false);
            surveyRepository.save(s);
        });
    }
}