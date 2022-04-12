package pl.scf.model.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.api.model.dto.VerificationTokenDTO;
import pl.scf.api.model.exception.IdentificationException;
import pl.scf.api.model.exception.NotFoundException;
import pl.scf.api.model.request.VerificationTokenSaveRequest;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.api.model.response.VerificationTokenResponse;
import pl.scf.model.repositories.IVerificationTokenRepository;

import java.time.Instant;
import java.util.List;

import static pl.scf.api.model.utils.ApiConstants.*;
import static pl.scf.api.model.utils.DTOMapper.toVerificationTokens;
import static pl.scf.api.model.utils.ResponseUtil.throwExceptionWhenIdZero;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationTokenService {
    private final IVerificationTokenRepository tokenRepository;
    private final String toMessageTokenWord = "Verification Token";

    private UniversalResponse updateResponse;
    public final UniversalResponse update(final VerificationTokenSaveRequest request) throws NotFoundException, IdentificationException {
        throwExceptionWhenIdZero(request.getUserId());

        tokenRepository.findById(request.getUserId()).ifPresentOrElse(
                (foundToken) -> {
                    foundToken.setToken(request.getToken());
                    foundToken.setActivated(request.getActivated());

                    log.info(UPDATE_MESSAGE, toMessageTokenWord, request.getUserId());
                    tokenRepository.save(foundToken);

                    updateResponse = UniversalResponse.builder()
                            .date(Instant.now())
                            .success(true)
                            .message(SUCCESS_UPDATE)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found VerificationToken with specified AppUser id");
                }
        ); return  updateResponse;
    }

    private UniversalResponse deleteResponse;
    public final UniversalResponse delete(final Long id) throws NotFoundException, IdentificationException{
        throwExceptionWhenIdZero(id);

        tokenRepository.findById(id).ifPresentOrElse(
                (foundToken) -> {
                    log.info(DELETING_MESSAGE, toMessageTokenWord, id);
                    tokenRepository.deleteById(id);

                    deleteResponse = UniversalResponse.builder()
                            .date(Instant.now())
                            .success(true)
                            .message(SUCCESS_DELETE)
                            .build();
                }, () -> {
                    throw new NotFoundException("Not found VerificationToken with specified id");
                }
        ); return deleteResponse;
    }

    private VerificationTokenResponse tokenByIdResponse;
    public final VerificationTokenResponse getById(final Long id) throws NotFoundException, IdentificationException{
        throwExceptionWhenIdZero(id);

        tokenRepository.findById(id).ifPresentOrElse(
                (foundToken) -> {
                    log.info(FETCH_BY_ID, toMessageTokenWord, id);
                    final VerificationTokenDTO tokenDTO = VerificationTokenDTO.builder()
                            .token(foundToken.getToken())
                            .activated(foundToken.getActivated())
                            .userId(foundToken.getUser().getId())
                            .build();

                    tokenByIdResponse = VerificationTokenResponse.builder()
                            .success(true)
                            .date(Instant.now())
                            .message(SUCCESS_FETCHING)
                            .verificationToken(tokenDTO)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found VerificationToken with specified id");
                }
        ); return tokenByIdResponse;
    }

    private VerificationTokenResponse tokenByNameResponse;
    public final VerificationTokenResponse getByTokenName(final String desiredToken) throws NotFoundException{
        tokenRepository.findByToken(desiredToken).ifPresentOrElse(
                (foundToken) -> {
                    log.info(FETCH_BY_ID, toMessageTokenWord, foundToken.getId());
                    final VerificationTokenDTO tokenDTO = VerificationTokenDTO.builder()
                            .token(foundToken.getToken())
                            .userId(foundToken.getUser().getId())
                            .activated(foundToken.getActivated())
                            .build();

                    tokenByNameResponse = VerificationTokenResponse.builder()
                            .success(true)
                            .date(Instant.now())
                            .message(SUCCESS_FETCHING)
                            .verificationToken(tokenDTO)
                            .build();
                }, () -> {
                    throw new NotFoundException("Not found VerificationToken with specified token");
                }
        ); return tokenByNameResponse;
    }

    public final List<VerificationTokenDTO> getAll() {
        log.info(FETCHING_ALL_MESSAGE, "VerificationToken");
        return toVerificationTokens(tokenRepository.findAll());
    }
}
