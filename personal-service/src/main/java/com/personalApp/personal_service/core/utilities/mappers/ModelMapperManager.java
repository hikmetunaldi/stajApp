package com.personalApp.personal_service.core.utilities.mappers;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.modelmapper.spi.MatchingStrategy;

@Service
@AllArgsConstructor
public class ModelMapperManager implements ModelMapperService {
    private ModelMapper modelMapper;

    public ModelMapper forResponse(){
        this.modelMapper.getConfiguration().setAmbiguityIgnored(true).
                setMatchingStrategy(MatchingStrategies.LOOSE);

        return this.modelMapper;
    }

    public ModelMapper forRequest(){
        this.modelMapper.getConfiguration().setAmbiguityIgnored(true).
                setMatchingStrategy(MatchingStrategies.STANDARD);

         return this.modelMapper;
    }
}
