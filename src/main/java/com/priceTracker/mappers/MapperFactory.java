package com.priceTracker.mappers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapperFactory {

    private final ModelMapper modelMapper;

    public <A, B> GenericMapper<A, B> create(Class<A> entityClass, Class<B> dtoClass) {
        return new GenericMapper<>(modelMapper, entityClass, dtoClass);
    }
}
