package com.example.football_championship.DTO;

import java.util.List;

public class ProcessingResult<T> {
    private List<T> validData;
    private List<String> errors;

    // Getters and setters for serialization
    public List<T> getValidData() {
        return validData;
    }

    public void setValidData(List<T> validData) {
        this.validData = validData;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
