package pl.scf.model.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.api.model.dto.TopicSubCategoryDTO;
import pl.scf.api.model.request.TopicSubCategorySaveRequest;
import pl.scf.api.model.request.TopicSubCategoryUpdateRequest;
import pl.scf.api.model.response.TopicSubCategoryResponse;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.TopicSubCategory;
import pl.scf.model.repositories.ITopicCategoryRepository;
import pl.scf.model.repositories.ITopicSubCategoryRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static pl.scf.api.model.utils.ApiConstants.*;
import static pl.scf.api.model.utils.DTOMapper.toSubCategory;
import static pl.scf.api.model.utils.DTOMapper.toSubCategoryNames;
import static pl.scf.api.model.utils.ResponseUtil.messageByIdError;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicSubCategoryService {
    private final ITopicSubCategoryRepository subCategoryRepository;
    private final ITopicCategoryRepository categoryRepository;
    private final String toMessageTopicSubCategoryWord = "TopicSubCategory";


    private UniversalResponse saveResponse;
    public final UniversalResponse save(final TopicSubCategorySaveRequest request) {
        if (request.getName() == null) {
            log.warn(NULLABLE_MESSAGE, "New SubCategory name");
            saveResponse = UniversalResponse.builder()
                    .success(false)
                    .date(new Date(System.currentTimeMillis()))
                    .message(String.format(NOT_VALID_ELEMENT_MESSAGE, "New SubCategory name"))
                    .build();
        } else {
            categoryRepository.findByName(request.getCategoryName()).ifPresentOrElse(
                    (foundCategory) -> {
                        final TopicSubCategory subCategory = TopicSubCategory.builder()
                                .name(request.getName())
                                .category(foundCategory)
                                .build();

                        log.info(SAVING, toMessageTopicSubCategoryWord);
                        subCategoryRepository.save(subCategory);

                        saveResponse = UniversalResponse.builder()
                                .success(true)
                                .date(new Date(System.currentTimeMillis()))
                                .message(SUCCESS_SAVING)
                                .build();
                    },
                    () -> {
                        log.warn(NOT_FOUND_BY_STH, toMessageTopicSubCategoryWord, "category", request.getCategoryName());
                        saveResponse = UniversalResponse.builder()
                                .success(false)
                                .date(new Date(System.currentTimeMillis()))
                                .message(FAIL_SAVING)
                                .build();
                    }
            );
        } return saveResponse;
    }

    private UniversalResponse updateResponse;
    public final UniversalResponse update(final TopicSubCategoryUpdateRequest request) {
        if (request.getName() == null) {
            log.warn(NULLABLE_MESSAGE, "New SubCategory name");
            updateResponse = UniversalResponse.builder()
                    .success(false)
                    .date(new Date(System.currentTimeMillis()))
                    .message(String.format(NOT_VALID_ELEMENT_MESSAGE, "New SubCategory name"))
                    .build();
        } else {
            subCategoryRepository.findById(request.getTopicCategoryId()).ifPresentOrElse(
                    (foundCategory) -> {
                        log.info(UPDATE_MESSAGE, toMessageTopicSubCategoryWord, request.getTopicCategoryId());
                        foundCategory.setName(request.getName());

                        subCategoryRepository.save(foundCategory);
                        updateResponse = UniversalResponse.builder()
                                .success(true)
                                .date(new Date(System.currentTimeMillis()))
                                .message(SUCCESS_UPDATE)
                                .build();
                    },
                    () -> {
                        log.warn(NOT_FOUND_BY_ID, toMessageTopicSubCategoryWord, request.getTopicCategoryId());
                        updateResponse = UniversalResponse.builder()
                                .success(false)
                                .date(new Date(System.currentTimeMillis()))
                                .message(messageByIdError(request.getTopicCategoryId(), toMessageTopicSubCategoryWord))
                                .build();
                    }
            );
        } return updateResponse;
    }

    private UniversalResponse deleteResponse;
    public final UniversalResponse delete(final Long id) {
        subCategoryRepository.findById(id).ifPresentOrElse(
                (foundCategory) -> {
                    log.info(DELETING_MESSAGE, toMessageTopicSubCategoryWord, id);
                    subCategoryRepository.deleteById(id);

                    deleteResponse = UniversalResponse.builder()
                            .success(true)
                            .date(new Date(System.currentTimeMillis()))
                            .message(SUCCESS_UPDATE)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageTopicSubCategoryWord, id);
                    deleteResponse = UniversalResponse.builder()
                            .success(false)
                            .date(new Date(System.currentTimeMillis()))
                            .message(messageByIdError(id, toMessageTopicSubCategoryWord))
                            .build();
                }
        ); return deleteResponse;
    }

    public final List<TopicSubCategoryDTO> getAll() {
        log.info(FETCHING_ALL_MESSAGE, toMessageTopicSubCategoryWord);
        return toSubCategory(subCategoryRepository.findAll());
    }

    public final List<String> getAllNames() {
        log.info(FETCHING_ALL_MESSAGE, toMessageTopicSubCategoryWord + " names");
        return toSubCategoryNames(subCategoryRepository.findAll());
    }

    private TopicSubCategoryResponse getByIdTopic;
    public final TopicSubCategoryResponse getById(final Long id) {
        subCategoryRepository.findById(id).ifPresentOrElse(
                (foundCategory) -> {
                    log.info(FETCH_BY_ID, toMessageTopicSubCategoryWord, id);
                    final TopicSubCategoryDTO subCategoryDTO = TopicSubCategoryDTO.builder()
                            .name(foundCategory.getName())
                            .topicNameCategory(foundCategory.getCategory().getName())
                            .build();

                    getByIdTopic = TopicSubCategoryResponse.builder()
                            .success(true)
                            .date(new Date(System.currentTimeMillis()))
                            .message("Found category")
                            .subCategory(subCategoryDTO)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageTopicSubCategoryWord, id);
                    getByIdTopic = TopicSubCategoryResponse.builder()
                            .success(false)
                            .date(new Date(System.currentTimeMillis()))
                            .message(messageByIdError(id, toMessageTopicSubCategoryWord))
                            .subCategory(null)
                            .build();
                }
        ); return getByIdTopic;
    }
}
