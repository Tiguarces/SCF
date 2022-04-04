package pl.scf.model.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.api.model.dto.TopicDetailsDTO;
import pl.scf.api.model.request.TopicDetailsUpdateRequest;
import pl.scf.api.model.response.TopicDetailsResponse;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.model.repositories.ITopicDetailsRepository;

import java.util.Date;
import java.util.List;

import static pl.scf.api.model.utils.ApiConstants.*;
import static pl.scf.api.model.utils.DTOMapper.toTopicDetails;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicDetailsService {
    private final ITopicDetailsRepository detailsRepository;
    private final String toMessageTopicDetailsWord = "TopicDetails";

    private TopicDetailsResponse getByIdResponse;
    public final TopicDetailsResponse getById(final Long topicId) {
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
                            .date(new Date(System.currentTimeMillis()))
                            .detailsDTO(details)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageTopicDetailsWord, topicId);
                    getByIdResponse = TopicDetailsResponse.builder()
                            .success(false)
                            .date(new Date(System.currentTimeMillis()))
                            .detailsDTO(null)
                            .build();
                }
        ); return getByIdResponse;
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

    private UniversalResponse updateResponse;
    public final UniversalResponse update(final TopicDetailsUpdateRequest request) {
        if(request.getDescription() == null) {
            log.warn(NULLABLE_MESSAGE, "Request description");
            updateResponse = UniversalResponse.builder()
                    .success(false)
                    .date(new Date(System.currentTimeMillis()))
                    .message("Description is null")
                    .build();
        } else {
            detailsRepository.findById(request.getDetailsId()).ifPresentOrElse(
                    (foundDetails) -> {
                        log.info(UPDATE_MESSAGE, toMessageTopicDetailsWord, request.getDetailsId());
                        foundDetails.setDescription(request.getDescription());
                        detailsRepository.save(foundDetails);

                        updateResponse = UniversalResponse.builder()
                                .success(true)
                                .date(new Date(System.currentTimeMillis()))
                                .message(SUCCESS_SAVING)
                                .build();
                    },
                    () -> {
                        log.info(NOT_FOUND_BY_ID, toMessageTopicDetailsWord, request.getDetailsId());
                        updateResponse = UniversalResponse.builder()
                                .success(false)
                                .date(new Date(System.currentTimeMillis()))
                                .message(String.format(NOT_FOUND_MESSAGE, toMessageTopicDetailsWord, "id", request.getDetailsId()))
                                .build();
                    }
            );
        } return updateResponse;
    }
}
