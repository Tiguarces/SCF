package pl.scf.model.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.api.model.dto.TopicCategoryDTO;
import pl.scf.api.model.exception.IdentificationException;
import pl.scf.api.model.exception.NotFoundException;
import pl.scf.api.model.request.TopicCategorySaveRequest;
import pl.scf.api.model.request.TopicCategoryUpdateRequest;
import pl.scf.api.model.response.GetAllTopicCategoryResponse;
import pl.scf.api.model.response.TopicCategoryResponse;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.Topic;
import pl.scf.model.TopicCategory;
import pl.scf.model.TopicSubCategory;
import pl.scf.model.repositories.ITopicCategoryRepository;
import pl.scf.model.repositories.ITopicRepository;
import pl.scf.model.repositories.ITopicSubCategoryRepository;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static pl.scf.api.model.utils.ApiConstants.*;
import static pl.scf.api.model.utils.DTOMapper.*;
import static pl.scf.api.model.utils.ResponseUtil.throwExceptionWhenIdZero;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicCategoryService {
    private final ITopicRepository topicRepository;
    private final ITopicCategoryRepository categoryRepository;
    private final ITopicSubCategoryRepository subCategoryRepository;
    private final String toMessageTopicCategoryWord = "TopicCategory";

    private UniversalResponse saveResponse;
    public final UniversalResponse save(final TopicCategorySaveRequest request) {
        if (request.getName() == null) {
            log.warn(NULLABLE_MESSAGE, "New category name");
            saveResponse =  UniversalResponse.builder()
                    .success(false)
                    .date(Instant.now())
                    .message(String.format(NOT_VALID_ELEMENT_MESSAGE, "New category name"))
                    .build();

        } else if(subCategoryRepository.existsByNameIn(request.getSubCategoryNames())) {
            log.warn(String.format(ELEMENT_EXISTS, "SubCategory"));
            saveResponse =  UniversalResponse.builder()
                    .success(false)
                    .date(Instant.now())
                    .message(String.format(ELEMENT_EXISTS, "SubCategory"))
                    .build();

        } else {
            categoryRepository.findByName(request.getName()).ifPresentOrElse(
                    (foundCategory) -> {
                        log.info("Category exists");
                        saveResponse =  UniversalResponse.builder()
                                .success(false)
                                .date(Instant.now())
                                .message("Category name exists")
                                .build();
                    },
                    () -> {
                        final TopicCategory topicCategory = TopicCategory.builder()
                                .name(request.getName())
                                .imageURL(request.getImageURL())
                                .build();

                        var savedTopicCategory = categoryRepository.saveAndFlush(topicCategory);
                        log.info(SAVING, "TopicCategory");

                        Arrays.asList(request.getSubCategoryNames())
                                .parallelStream()
                                .map(subCatName -> TopicSubCategory.builder()
                                        .name(subCatName)
                                        .category(savedTopicCategory)
                                        .build())
                                .forEachOrdered(subCategoryRepository::saveAndFlush);

                        log.info(SAVING, "TopicSubCategories");
                        saveResponse = UniversalResponse.builder()
                                .success(true)
                                .date(Instant.now())
                                .message(SUCCESS_SAVING)
                                .build();
                    }
            );
        } return saveResponse;
    }

    private UniversalResponse updateResponse;
    public final UniversalResponse update(final TopicCategoryUpdateRequest request) throws NotFoundException, IdentificationException {
        if (request.getName() == null) {
            log.warn(NULLABLE_MESSAGE, "New category name");
            updateResponse = UniversalResponse.builder()
                    .success(false)
                    .date(Instant.now())
                    .message(String.format(NOT_VALID_ELEMENT_MESSAGE, "New category name"))
                    .build();
        } else {
            throwExceptionWhenIdZero(request.getTopicId());

            categoryRepository.findById(request.getTopicId()).ifPresentOrElse(
                    (foundCategory) -> {
                        log.info(UPDATE_MESSAGE, toMessageTopicCategoryWord, request.getTopicId());
                        foundCategory.setName(request.getName());
                        foundCategory.setImageURL(request.getImageURL());

                        categoryRepository.save(foundCategory);
                        updateResponse = UniversalResponse.builder()
                                .success(true)
                                .date(Instant.now())
                                .message(SUCCESS_UPDATE)
                                .build();
                    },
                    () -> {
                        throw new NotFoundException("Not found TopicCategory with specified id");
                    }
            );
        } return updateResponse;
    }

    private UniversalResponse deleteResponse;
    public final UniversalResponse delete(final Long id) throws NotFoundException, IdentificationException {
        throwExceptionWhenIdZero(id);

        categoryRepository.findById(id).ifPresentOrElse(
                (foundCategory) -> {
                    log.info(DELETING_MESSAGE, toMessageTopicCategoryWord, id);
                    categoryRepository.deleteById(id);

                    deleteResponse = UniversalResponse.builder()
                            .success(true)
                            .date(Instant.now())
                            .message(SUCCESS_DELETE)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found TopicCategory with specified id");
                }
        ); return deleteResponse;
    }

    private TopicCategoryResponse getByIdTopic;
    public final TopicCategoryResponse getById(final Long id) throws NotFoundException, IdentificationException{
        throwExceptionWhenIdZero(id);

        categoryRepository.findById(id).ifPresentOrElse(
                (foundCategory) -> {
                    log.info(FETCH_BY_ID, toMessageTopicCategoryWord, id);
                    final TopicCategoryDTO categoryDTO = TopicCategoryDTO.builder()
                            .imageURL(foundCategory.getImageURL())
                            .categoryName(foundCategory.getName())
                            .subCategoryNames(toSubCategoryNamesToArray(foundCategory.getSubCategory()))
                            .build();

                    categoryDTO.setNumberOfTopics(getNumberOfTopics(categoryDTO));
                    categoryDTO.setNumberOfAnswers(getNumberOfAnswers(categoryDTO));

                    getByIdTopic = TopicCategoryResponse.builder()
                            .success(true)
                            .date(Instant.now())
                            .message(SUCCESS_FETCHING)
                            .category(categoryDTO)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found TopicCategory with specified id");
                }
        ); return getByIdTopic;
    }

    public final GetAllTopicCategoryResponse getAll() {
        log.info(FETCHING_ALL_MESSAGE, toMessageTopicCategoryWord);
        final var categories = toTopicCategories(categoryRepository.findAll());

        categories.forEach(category -> {
            category.setNumberOfTopics(getNumberOfTopics(category));
            category.setNumberOfAnswers(getNumberOfAnswers(category));
        });

        return GetAllTopicCategoryResponse.builder()
                .success(true)
                .date(Instant.now())
                .message(SUCCESS_FETCHING)
                .categories(categories)
                .build();
    }

    public final List<String> getAllNames() {
        log.info(FETCHING_ALL_MESSAGE, toMessageTopicCategoryWord + " names");
        return toTopicCategoryAllNames(categoryRepository.findAll());
    }

    /////////////////////////////////////////////////

    private Integer getNumberOfTopics(final TopicCategoryDTO category) {
        final var subCategoriesByCatName =
                subCategoryRepository
                        .findAllByCategoryName(category.getCategoryName())
                        .stream()
                        .map(TopicSubCategory::getName)
                        .toList();

        return topicRepository
               .findAllBySubCategoryNameIn(subCategoriesByCatName)
               .size();
    }

    private Integer getNumberOfAnswers(final TopicCategoryDTO category) {
        final var subCategoriesByCatName =
                subCategoryRepository
                        .findAllByCategoryName(category.getCategoryName())
                        .stream()
                        .map(TopicSubCategory::getName)
                        .toList();

        return topicRepository
                .findAllBySubCategoryNameIn(subCategoriesByCatName)
                .stream()
                .map(Topic::getAnswers)
                .mapToInt(Set::size)
                .sum();
    }
}
