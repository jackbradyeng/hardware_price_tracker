package com.priceTracker.services.pricePointServices.impl;

import com.priceTracker.domain.dto.hybridInterfaces.GenericDataAndPricePointProjection;
import com.priceTracker.domain.dto.pricePointDTOs.GenericPricePointDTO;
import com.priceTracker.domain.entities.pricePointEntities.GenericPricePoint;
import com.priceTracker.domain.entities.scrapingJobEntities.ScrapingJobResult;
import com.priceTracker.mappers.GenericMapper;
import com.priceTracker.repositories.pricePointRepositories.jdbcTemplates.GenericPricePointJdbcTemplate;
import com.priceTracker.repositories.scrapingJobRepositories.ScrapingJobResultRepository;
import com.priceTracker.services.pricePointServices.DataAndPricePointFactory;
import com.priceTracker.services.pricePointServices.GenericPricePointService;
import com.priceTracker.services.pricePointServices.GenericPricePointValidator;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * A generic price point service for all product types.
 * @param <E> product entity.
 * @param <P> product price point.
 * @param <D> product DTO.
 * @param <H> hybrid product & price point DTO.
 */
@Transactional
public class GenericPricePointServiceImpl<E, P extends GenericPricePoint, D, H>
        implements GenericPricePointService<H>, GenericPricePointValidator {

    private final JpaRepository<P, Long> repository;
    private final ScrapingJobResultRepository scrapingJobResultRepository;
    private final BiFunction<String, Pageable, Page<GenericDataAndPricePointProjection<E, P>>> findByModelNumberQuery;
    private final GenericMapper<P, GenericPricePointDTO> pricePointMapper;
    private final GenericMapper<E, D> productMapper;
    private final GenericPricePointJdbcTemplate<P> pricePointJdbcTemplate;
    private final DataAndPricePointFactory<D, H> hybridDtoFactory;

    public GenericPricePointServiceImpl(JpaRepository<P, Long> repository,
                                        ScrapingJobResultRepository scrapingJobResultRepository,
                                        BiFunction<String, Pageable, Page<GenericDataAndPricePointProjection<E, P>>> findByModelNumberQuery,
                                        GenericMapper<P, GenericPricePointDTO> pricePointMapper,
                                        GenericMapper<E, D> productMapper,
                                        GenericPricePointJdbcTemplate<P> pricePointJdbcTemplate,
                                        DataAndPricePointFactory<D, H> hybridDtoFactory) {
        this.repository = repository;
        this.scrapingJobResultRepository = scrapingJobResultRepository;
        this.findByModelNumberQuery = findByModelNumberQuery;
        this.pricePointMapper = pricePointMapper;
        this.productMapper = productMapper;
        this.pricePointJdbcTemplate = pricePointJdbcTemplate;
        this.hybridDtoFactory = hybridDtoFactory;
    }

    @Override
    public Optional<List<GenericPricePointDTO>> saveAll(List<GenericPricePointDTO> pricePointDTOs) {

        // if empty or invalid return empty
        if (pricePointDTOs.isEmpty() || !validatePricePointDTOs(pricePointDTOs)) {
            return Optional.empty();
        }

        List<P> pricePoints = pricePointDTOs.stream()
                .map(pricePointMapper::mapFrom)
                .toList();

        pricePointJdbcTemplate.batchInsertPricePoints(pricePoints);

        // safe to assume consistent vendor and product type since the validation checks have already been run
        String vendor = pricePointDTOs.getFirst().getVendor();
        String productType = pricePointDTOs.getFirst().getProductType();

        // persist the results and return the list of price point DTOs
        saveResults(vendor, productType, pricePointDTOs.size(), pricePoints.size());
        return Optional.of(pricePointDTOs);
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

        // return empty if not found
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

    public ScrapingJobResult saveResults(String vendor,
                            String productType,
                            Integer recordsReceived,
                            Integer recordsSaved) {

        ScrapingJobResult scrapingJobResult = ScrapingJobResult.builder()
                .vendor(vendor)
                .productType(productType)
                .recordsReceived(recordsReceived)
                .recordsSaved(recordsSaved)
                .timeCompleted(LocalDateTime.now())
                .build();

        return scrapingJobResultRepository.save(scrapingJobResult);
    }
}