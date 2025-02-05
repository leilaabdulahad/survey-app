package com.example.survey.service;

import com.example.survey.model.Option;
import com.example.survey.repository.OptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OptionsService {

    private final OptionRepository optionRepository;

    public OptionsService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    public Option createOption(Option option) {
        return optionRepository.save(option);
    }

    public List<Option> getAllOptions() {
        return optionRepository.findAll();
    }

    public Option getOptionById(Long id) {
        Optional<Option> option = optionRepository.findById(id);
        return option.orElse(null);
    }

    public Option updateOption(Long id, Option optionDetails) {
        Optional<Option> existingOption = optionRepository.findById(id);
        if (existingOption.isPresent()) {
            Option updatedOption = existingOption.get();
            updatedOption.setText(optionDetails.getText());
            return optionRepository.save(updatedOption);
        }
        return null;
    }

    public void deleteOption(Long id) {
        optionRepository.deleteById(id);
    }
}
