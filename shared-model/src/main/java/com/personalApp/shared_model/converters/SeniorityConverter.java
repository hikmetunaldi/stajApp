package com.personalApp.shared_model.converters;

import com.personalApp.shared_model.enums.Seniority;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

@Converter(autoApply = true)
public class SeniorityConverter implements AttributeConverter<Seniority, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Seniority attribute) {
        return Objects.nonNull(attribute) ? attribute.getValue() : null;
    }

    @Override
    public Seniority convertToEntityAttribute(Integer dbData) {
        return Objects.nonNull(dbData) ? Seniority.fromValue(dbData) : null;
    }
}
