package com.price_tracker.mappers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
public class GenericMapper<A, B> implements Mapper<A, B> {

    private final ModelMapper modelMapper;
    private final Class<A> entityClass;
    private final Class<B> dtoClass;

    @Override
    public B mapTo(A a) {
        return modelMapper.map(a, dtoClass);
    }

    @Override
    public A mapFrom(B b) {
        return modelMapper.map(b, entityClass);
    }
}
