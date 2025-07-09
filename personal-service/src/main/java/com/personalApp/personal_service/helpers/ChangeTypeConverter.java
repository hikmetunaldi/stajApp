//package com.personalApp.personal_service.helpers;
//
//import com.personalApp.personal_service.constants.ChangeType;
//import jakarta.persistence.AttributeConverter;
//import jakarta.persistence.Converter;
//
//import java.util.Objects;
//
//@Converter(autoApply = true)
//public class ChangeTypeConverter implements AttributeConverter<ChangeType, Integer> {
//
//    @Override
//    public Integer convertToDatabaseColumn(ChangeType attribute){
//        return Objects.nonNull(attribute) ? attribute.getValue() : null;
//    }
//
//    @Override
//    public ChangeType convertToEntityAttribute(Integer dbData){
//        return Objects.nonNull(dbData) ? ChangeType.fromValue(dbData) : null;
//    }
//
//}
