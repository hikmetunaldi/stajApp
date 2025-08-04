package com.personalApp.shared_model.converters;

import com.personalApp.shared_model.enums.Gender;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

@Converter(autoApply = true)
public class GenderConverter implements AttributeConverter<Gender, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Gender attribute) {
        return Objects.nonNull(attribute) ? attribute.getValue() : null;
    }

    @Override
    public Gender convertToEntityAttribute(Integer dbData) {
        return Objects.nonNull(dbData) ? Gender.fromValue(dbData) : null;
    }
}