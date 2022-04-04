package pl.scf.model.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.api.model.dto.TopicCategoryDTO;
import pl.scf.api.model.request.TopicCategorySaveRequest;
import pl.scf.api.model.request.TopicSubCategoryUpdateRequest;
import pl.scf.api.model.response.TopicCategoryResponse;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.TopicCategory;
import pl.scf.model.TopicSubCategory;
import pl.scf.model.repositories.ITopicCategoryRepository;
import pl.scf.model.repositories.ITopicSubCategoryRepository;

import java.util.Date;
import java.util.List;

import static pl.scf.api.model.utils.ApiConstants.*;
import static pl.scf.api.model.utils.DTOMapper.*;
import static pl.scf.api.model.utils.ResponseUtil.messageByIdError;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicCategoryService {
    private final ITopicCategoryRepository categoryRepository;
    private final ITopicSubCategoryRepository subCategoryRepository;
    private final String toMessageTopicCategoryWord = "TopicCategory";

    private UniversalResponse saveResponse;
    public final UniversalResponse save(final TopicCategorySaveRequest request) {
        if (request.getName() == null) {
            log.warn(NULLABLE_MESSAGE, "New category name");
            saveResponse =  UniversalResponse.builder()
                    .success(false)
                    .date(new Date(System.currentTimeMillis()))
                    .message(String.format(NOT_VALID_ELEMENT_MESSAGE, "New category name"))
                    .build();

        } else if(subCategoryRepository.existsByName(request.getSubCategoryName())) {
            log.warn(String.format(ELEMENT_EXISTS, "SubCategory"));
            saveResponse =  UniversalResponse.builder()
                    .success(false)
                    .date(new Date(System.currentTimeMillis()))
                    .message(String.format(ELEMENT_EXISTS, "SubCategory"))
                    .build();

        } else {
            categoryRepository.findByName(request.getName()).ifPresentOrElse(
                    (foundCategory) -> {
                        log.info("Category exists");
                        saveResponse =  UniversalResponse.builder()
                                .success(false)
                                .date(new Date(System.currentTimeMillis()))
                                .message("Category name exists")
                                .build();
                    },
                    () -> {
                        final TopicCategory topicCategory = TopicCategory.builder()
                                .name(request.getName())
                                .build();

                        final TopicSubCategory subCategory = TopicSubCategory.builder()
                                .name(request.getSubCategoryName())
                                .category(topicCategory).build();

                        log.info(SAVING, "TopicSubCategory");
                        subCategoryRepository.save(subCategory);

                        log.info(SAVING, toMessageTopicCategoryWord);
                        categoryRepository.save(topicCategory);

                        saveResponse = UniversalResponse.builder()
                                .success(true)
                                .date(new Date(System.currentTimeMillis()))
                                .message(SUCCESS_SAVING)
                                .build();
                    }
            );
        } return saveResponse;
    }

    private UniversalResponse updateResponse;
    public final UniversalResponse update(final TopicSubCategoryUpdateRequest request) {
        if (request.getName() == null) {
            log.warn(NULLABLE_MESSAGE, "New category name");
            updateResponse = UniversalResponse.builder()
                    .success(false)
                    .date(new Date(System.currentTimeMillis()))
                    .message(String.format(NOT_VALID_ELEMENT_MESSAGE, "New category name"))
                    .build();
        } else {
            categoryRepository.findById(request.getTopicCategoryId()).ifPresentOrElse(
                    (foundCategory) -> {
                        log.info(UPDATE_MESSAGE, toMessageTopicCategoryWord, request.getTopicCategoryId());
                        foundCategory.setName(request.getName());

                        categoryRepository.save(foundCategory);
                        updateResponse = UniversalResponse.builder()
                                .success(true)
                                .date(new Date(System.currentTimeMillis()))
                                .message(SUCCESS_UPDATE)
                                .build();
                    },
                    () -> {
                        log.warn(NOT_FOUND_BY_ID, toMessageTopicCategoryWord, request.getTopicCategoryId());
                        updateResponse = UniversalResponse.builder()
                                .success(false)
                                .date(new Date(System.currentTimeMillis()))
                                .message(messageByIdError(request.getTopicCategoryId(), toMessageTopicCategoryWord))
                                .build();
                    }
            );
        } return updateResponse;
    }

    private UniversalResponse deleteResponse;
    public final UniversalResponse delete(final Long id) {
        categoryRepository.findById(id).ifPresentOrElse(
                (foundCategory) -> {
                    log.info(DELETING_MESSAGE, toMessageTopicCategoryWord, id);
                    categoryRepository.deleteById(id);

                    deleteResponse = UniversalResponse.builder()
                            .success(true)
                            .date(new Date(System.currentTimeMillis()))
                            .message(SUCCESS_UPDATE)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageTopicCategoryWord, id);
                    deleteResponse = UniversalResponse.builder()
                            .success(false)
                            .date(new Date(System.currentTimeMillis()))
                            .message(messageByIdError(id, toMessageTopicCategoryWord))
                            .build();
                }
        ); return deleteResponse;
    }

    private TopicCategoryResponse getByIdTopic;
    public final TopicCategoryResponse getById(final Long id) {
        categoryRepository.findById(id).ifPresentOrElse(
                (foundCategory) -> {
                    log.info(FETCH_BY_ID, toMessageTopicCategoryWord, id);
                    final TopicCategoryDTO categoryDTO = TopicCategoryDTO.builder()
                            .categoryName(foundCategory.getName())
                            .subCategoryNames(toSubCategoryNamesToArray(foundCategory.getSubCategory()))
                            .build();

                    getByIdTopic = TopicCategoryResponse.builder()
                            .success(true)
                            .date(new Date(System.currentTimeMillis()))
                            .message("Found category")
                            .category(categoryDTO)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageTopicCategoryWord, id);
                    getByIdTopic = TopicCategoryResponse.builder()
                            .success(false)
                            .date(new Date(System.currentTimeMillis()))
                            .message(messageByIdError(id, toMessageTopicCategoryWord))
                            .category(null)
                            .build();
                }
        ); return getByIdTopic;
    }

    public final List<TopicCategoryDTO> getAll() {
        log.info(FETCHING_ALL_MESSAGE, toMessageTopicCategoryWord);
        return toTopicCategory(categoryRepository.findAll());
    }

    public final List<String> getAllNames() {
        log.info(FETCHING_ALL_MESSAGE, toMessageTopicCategoryWord + " names");
        return toTopicCategoryAllNames(categoryRepository.findAll());
    }
}
