package pl.scf.model.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.api.model.dto.TopicDetailsDTO;
import pl.scf.api.model.exception.IdentificationException;
import pl.scf.api.model.exception.NotFoundException;
import pl.scf.api.model.request.TopicDetailsUpdateRequest;
import pl.scf.api.model.response.TopicDetailsResponse;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.repositories.ITopicDetailsRepository;

import java.time.Instant;
import java.util.List;

import static pl.scf.api.model.utils.ApiConstants.*;
import static pl.scf.api.model.utils.DTOMapper.toTopicDetails;
import static pl.scf.api.model.utils.ResponseUtil.throwExceptionWhenIdZero;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicDetailsService {
    private final ITopicDetailsRepository detailsRepository;
    private final String toMessageTopicDetailsWord = "TopicDetails";

    private TopicDetailsResponse getByIdResponse;
    public final TopicDetailsResponse getById(final Long topicId) throws NotFoundException, IdentificationException {
        throwExceptionWhenIdZero(topicId);

        detailsRepository.findById(topicId).ifPresentOrElse(
                (foundDetails) -> {
                    log.info(FETCH_BY_ID, toMessageTopicDetailsWord, topicId);
                    final TopicDetailsDTO details = TopicDetailsDTO.builder()
                            .topicId(topicId)
                            .topicName(foundDetails.getTopicName())
                            .description(foundDetails.getDescription())
                            .build();

                    getByIdResponse = TopicDetailsResponse.builder()
                            .success(true)
                            .date(Instant.now())
                            .detailsDTO(details)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found TopicDetails with specified id");
                }
        ); return getByIdResponse;
    }

    private UniversalResponse updateResponse;
    public final UniversalResponse update(final TopicDetailsUpdateRequest request) throws NotFoundException, IdentificationException{
        if(request.getDescription() == null) {
            log.warn(NULLABLE_MESSAGE, "Request description");
            updateResponse = UniversalResponse.builder()
                    .success(false)
                    .date(Instant.now())
                    .message("Description is null")
                    .build();
        } else {
            throwExceptionWhenIdZero(request.getDetailsId());

            detailsRepository.findById(request.getDetailsId()).ifPresentOrElse(
                    (foundDetails) -> {
                        log.info(UPDATE_MESSAGE, toMessageTopicDetailsWord, request.getDetailsId());
                        foundDetails.setDescription(request.getDescription());
                        detailsRepository.save(foundDetails);

                        updateResponse = UniversalResponse.builder()
                                .success(true)
                                .date(Instant.now())
                                .message(SUCCESS_SAVING)
                                .build();
                    },
                    () -> {
                        throw new NotFoundException("Not found TopicDetails with specified id");
                    }
            );
        } return updateResponse;
    }

    public final List<TopicDetailsDTO> getAllLastTopics(final Long amount) {
        log.info(FETCHING_ALL_MESSAGE, toMessageTopicDetailsWord);
        return detailsRepository.findTopByOrderByIdDesc()
                .stream().limit(amount).toList();
    }

    public final List<TopicDetailsDTO> getAll() {
        log.info(FETCHING_ALL_MESSAGE, toMessageTopicDetailsWord);
        return toTopicDetails(detailsRepository.findAll());
    }

}
