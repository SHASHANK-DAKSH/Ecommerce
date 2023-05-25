package com.example.MainProject.entities.categories;

import jakarta.persistence.AttributeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Values implements AttributeConverter<List<String>,String> {
    @Override
    public String convertToDatabaseColumn(List<String>li) {
        return String.join(",",li);
    }

    @Override
    public List<String> convertToEntityAttribute(String join) {
        return new ArrayList<>(Arrays.asList(join.split(",")));
    }
}
