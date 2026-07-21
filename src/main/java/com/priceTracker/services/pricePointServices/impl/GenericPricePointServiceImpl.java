package com.priceTracker.services.pricePointServices.impl;

import com.priceTracker.domain.dto.hybridInterfaces.GenericDataAndPricePointProjection;
import com.priceTracker.domain.dto.pricePointDTOs.GenericPricePointDTO;
import com.priceTracker.mappers.GenericMapper;
import com.priceTracker.services.pricePointServices.DataAndPricePointFactory;
import com.priceTracker.services.pricePointServices.GenericPricePointService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

@Transactional
public class GenericPricePointServiceImpl<E, P, D, H> implements GenericPricePointService<H> {

    private final JpaRepository<P, Long> repository;
    private final BiFunction<String, Pageable, Page<GenericDataAndPricePointProjection<E, P>>> findByModelNumberQuery;
    private final GenericMapper<P, GenericPricePointDTO> pricePointMapper;
    private final GenericMapper<E, D> productMapper;
    private final DataAndPricePointFactory<D, H> hybridDtoFactory;

    public GenericPricePointServiceImpl(JpaRepository<P, Long> repository,
                                        BiFunction<String, Pageable, Page<GenericDataAndPricePointProjection<E, P>>> findByModelNumberQuery,
                                        GenericMapper<P, GenericPricePointDTO> pricePointMapper,
                                        GenericMapper<E, D> productMapper,
                                        DataAndPricePointFactory<D, H> hybridDtoFactory) {
        this.repository = repository;
        this.findByModelNumberQuery = findByModelNumberQuery;
        this.pricePointMapper = pricePointMapper;
        this.productMapper = productMapper;
        this.hybridDtoFactory = hybridDtoFactory;
    }

    @Override
    public Page<GenericPricePointDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(pricePointMapper::mapTo);
    }

    @Override
    public Optional<H> findByModelNumber(String modelNumber, Pageable pageable) {

        Page<GenericDataAndPricePointProjection<E, P>> resultList = findByModelNumberQuery
                .apply(modelNumber, pageable);

        // throw a 404 if not found
        if (resultList.isEmpty()) {
            return Optional.empty();
        }

        // convert product entity to a DTO so we can expose it in our API
        E product = resultList.stream().toList().getFirst().getEntity();
        D productDTO = productMapper.mapTo(product);

        // convert price points to a list of DTOs
        List<GenericPricePointDTO> pricePointDTOs = resultList.stream()
                .map(result -> pricePointMapper.mapTo(result.getPricePoint()))
                .toList();

        H hybridDTO = hybridDtoFactory.create(productDTO, pricePointDTOs, resultList.getNumber(),
                resultList.getSize(), resultList.getTotalPages(), resultList.getTotalElements());

        return Optional.of(hybridDTO);
    }
}