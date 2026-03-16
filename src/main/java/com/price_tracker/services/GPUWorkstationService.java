package com.price_tracker.services;

import com.price_tracker.domain.dto.GPUWorkstationDTO;
import java.util.List;
import java.util.Optional;

public interface GPUWorkstationService {

    GPUWorkstationDTO save(GPUWorkstationDTO gpuWorkstation);

    List<GPUWorkstationDTO> saveAll(List<GPUWorkstationDTO> GPUWorkstationDTOList);

    List<GPUWorkstationDTO> findAll();

    Optional<GPUWorkstationDTO> findOne(String id);

    Boolean exists(String id);

    Optional<GPUWorkstationDTO> fullUpdate(String id, GPUWorkstationDTO gpuWorkstationEntity);

    Optional<GPUWorkstationDTO> partialUpdate(String id, GPUWorkstationDTO gpuWorkstation);

    void delete(String id);
}
