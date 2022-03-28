package pl.scf.model.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.api.model.response.TopicCategoryResponse;
import pl.scf.api.model.request.TopicCategorySaveRequest;
import pl.scf.api.model.request.TopicSubCategoryUpdateRequest;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.TopicCategory;
import pl.scf.model.repositories.ITopicCategoryRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static pl.scf.api.model.utils.ApiConstants.*;
import static pl.scf.api.model.utils.ResponseUtil.messageByIdError;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicCategoryService {
    private final ITopicCategoryRepository categoryRepository;
    private final String toMessageTopicCategoryWord = "TopicCategory";

    public final UniversalResponse save(final TopicCategorySaveRequest request) {
        if (request.getName() == null) {
            log.warn(NULLABLE_MESSAGE, "New category name");
            return UniversalResponse.builder()
                    .success(false)
                    .date(new Date(System.currentTimeMillis()))
                    .message(String.format(NOT_VALID_ELEMENT_MESSAGE, "New category name"))
                    .build();
        } else {
            final TopicCategory topicCategory = TopicCategory.builder()
                    .name(request.getName())
                    .build();

            log.info(SAVING, toMessageTopicCategoryWord);
            categoryRepository.save(topicCategory);

            return UniversalResponse.builder()
                    .success(true)
                    .date(new Date(System.currentTimeMillis()))
                    .message(SUCCESS_SAVING)
                    .build();
        }
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

    public final List<TopicCategory> getAll() {
        log.info(FETCHING_ALL_MESSAGE, toMessageTopicCategoryWord);
        return categoryRepository.findAll();
    }

    public final List<String> getAllNames() {
        log.info(FETCHING_ALL_MESSAGE, toMessageTopicCategoryWord + " names");
        return categoryRepository.findAll()
                .stream()
                .map(TopicCategory::getName)
                .collect(Collectors.toList());
    }

    private TopicCategoryResponse getByIdTopic;
    public final TopicCategoryResponse getById(final Long id) {
        categoryRepository.findById(id).ifPresentOrElse(
                (foundCategory) -> {
                    log.info(FETCH_BY_ID, toMessageTopicCategoryWord, id);
                    getByIdTopic = TopicCategoryResponse.builder()
                            .category(foundCategory)
                            .message("Found category")
                            .date(new Date(System.currentTimeMillis()))
                            .success(true)
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
}
