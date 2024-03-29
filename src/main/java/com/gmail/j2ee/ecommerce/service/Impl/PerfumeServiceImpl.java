package com.gmail.j2ee.ecommerce.service.Impl;

import com.gmail.j2ee.ecommerce.domain.Perfume;
import com.gmail.j2ee.ecommerce.dto.perfume.PerfumeSearchRequest;
import com.gmail.j2ee.ecommerce.enums.SearchPerfume;
import com.gmail.j2ee.ecommerce.service.PerfumeService;
import com.gmail.j2ee.ecommerce.exception.ApiRequestException;
import com.gmail.j2ee.ecommerce.repository.PerfumeRepository;
import com.gmail.j2ee.ecommerce.repository.projection.PerfumeProjection;
import graphql.schema.DataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.gmail.j2ee.ecommerce.constants.ErrorMessage.PERFUME_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PerfumeServiceImpl implements PerfumeService {

    private final PerfumeRepository perfumeRepository;
    private final ResourceLoader resourceLoader;

    @Value("${amazon.s3.bucket.name}")
    private String bucketName;

    @Override
    public Perfume getPerfumeById(Long perfumeId) {
        return perfumeRepository.findById(perfumeId)
                .orElseThrow(() -> new ApiRequestException(PERFUME_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @Override
    public Page<PerfumeProjection> getAllPerfumes(Pageable pageable) {
        return perfumeRepository.findAllByOrderByIdAsc(pageable);
    }

    @Override
    public List<PerfumeProjection> getPerfumesByIds(List<Long> perfumesId) {
        return perfumeRepository.getPerfumesByIds(perfumesId);
    }

    @Override
    public Page<PerfumeProjection> findPerfumesByFilterParams(PerfumeSearchRequest filter, Pageable pageable) {
        return perfumeRepository.findPerfumesByFilterParams(
                filter.getPerfumers(),
                filter.getGenders(),
                filter.getPrices().get(0),
                filter.getPrices().get(1),
                filter.getSortByPrice(),
                pageable);
    }

    @Override
    public List<Perfume> findByPerfumer(String perfumer) {
        return perfumeRepository.findByPerfumerOrderByPriceDesc(perfumer);
    }

    @Override
    public List<Perfume> findByPerfumeGender(String perfumeGender) {
        return perfumeRepository.findByPerfumeGenderOrderByPriceDesc(perfumeGender);
    }

    @Override
    public Page<PerfumeProjection> findByInputText(SearchPerfume searchType, String text, Pageable pageable) {
        if (searchType.equals(SearchPerfume.BRAND)) {
            return perfumeRepository.findByPerfumer(text, pageable);
        } else if (searchType.equals(SearchPerfume.PERFUME_TITLE)) {
            return perfumeRepository.findByPerfumeTitle(text, pageable);
        } else {
            return perfumeRepository.findByManufacturerCountry(text, pageable);
        }
    }

    @Override
    @Transactional
    public Perfume savePerfume(Perfume perfume, MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            // Handle the case where no file is provided or the file is empty
            // You may want to throw an exception or handle this case according to your requirements
        } else {
            try {
                // Generate a unique filename for the uploaded file
                String fileName = UUID.randomUUID().toString() + "." + multipartFile.getOriginalFilename();

                // Obtain a reference to the static folder
                File staticFolder = resourceLoader.getResource("classpath:/static/").getFile();

                // Create the file path in the static resources folder
                String filePath = staticFolder.getAbsolutePath() + File.separator + fileName;
                String hostname = "localhost:8080";
                String filePathToCall = "http://"+ hostname + "/static/" + fileName;

                // Save the file to the specified path

                File file = new File(filePath);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(multipartFile.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception appropriately
                    // You might want to throw an exception here or log the error
                }

                // Set the filename in the Perfume entity
                perfume.setFilename(filePathToCall);

            } catch (Exception ex) {
                ex.printStackTrace();
                // Handle the exception appropriately
                // You might want to throw an exception here or log the error
            }
        }
        return perfumeRepository.save(perfume);
    }


    @Override
    @Transactional
    public String deletePerfume(Long perfumeId) {
        Perfume perfume = perfumeRepository.findById(perfumeId)
                .orElseThrow(() -> new ApiRequestException(PERFUME_NOT_FOUND, HttpStatus.NOT_FOUND));
        perfumeRepository.delete(perfume);
        return "Perfume deleted successfully";
    }

    @Override
    public DataFetcher<Perfume> getPerfumeByQuery() {
        return dataFetchingEnvironment -> {
            Long perfumeId = Long.parseLong(dataFetchingEnvironment.getArgument("id"));
            return perfumeRepository.findById(perfumeId).get();
        };
    }

    @Override
    public DataFetcher<List<PerfumeProjection>> getAllPerfumesByQuery() {
        return dataFetchingEnvironment -> perfumeRepository.findAllByOrderByIdAsc();
    }

    @Override
    public DataFetcher<List<Perfume>> getAllPerfumesByIdsQuery() {
        return dataFetchingEnvironment -> {
            List<String> objects = dataFetchingEnvironment.getArgument("ids");
            List<Long> perfumesId = objects.stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            return perfumeRepository.findByIdIn(perfumesId);
        };
    }
}
