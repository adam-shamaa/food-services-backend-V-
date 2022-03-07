package com.foodservicesapi.services;

import org.springframework.stereotype.Service;

@Service
public class RestaurantUtils {

    public String normalizeName(String text) {
        String normalizedText = text;

        normalizedText = convertWhitespaceToSingleSpace(normalizedText);
        normalizedText = removeApostrophes(normalizedText);
        normalizedText = removeTextInParenthesis(normalizedText);
        normalizedText = removeHashtagIdentifiers(normalizedText);

        return normalizedText;
    }

    public String convertWhitespaceToSingleSpace(String text) {
        return text.replaceAll("\\s+", " ").trim();
    }

    public String removeApostrophes(String text) {
        return removeCharacter(text, '\'', "").trim();
    }

    public String removeTextInParenthesis(String text) {
        return text.replaceAll("\\([^)]*\\)", "").trim();
    }

    public String removeHashtagIdentifiers(String text) {
        return text.replaceAll("#[^ ]*", "").trim();
    }

    private String removeCharacter(String text, Character targetCharacter, String replacement) {
        return text.replaceAll(String.format("[ ]*%c", targetCharacter), replacement).trim();
    }
}
