package com.budgetin.web.dto;

public class UpdatePreferencesDto {
    public static final UpdatePreferencesDto UpdatePreferencesDto = null;
    private String currency;
    private String language;
    private boolean darkMode;

    // Constructor
    public UpdatePreferencesDto() {}

    public UpdatePreferencesDto(String currency, String language, boolean darkMode) {
        this.currency = currency;
        this.language = language;
        this.darkMode = darkMode;
    }

    // Getters
    public String getCurrency() {
        return currency;
    }

    public String getLanguage() {
        return language;
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    // Setters (optional, if needed)
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }
}
