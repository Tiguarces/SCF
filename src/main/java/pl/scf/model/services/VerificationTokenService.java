package pl.scf.model.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.api.model.dto.VerificationTokenDTO;
import pl.scf.api.model.request.VerificationTokenSaveRequest;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.api.model.response.VerificationTokenResponse;
import pl.scf.model.VerificationToken;
import pl.scf.model.repositories.IVerificationTokenRepository;

import java.util.Date;
import java.util.List;

import static pl.scf.api.model.utils.ApiConstants.*;
import static pl.scf.api.model.utils.DTOMapper.toVerificationTokens;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationTokenService {
    private final IVerificationTokenRepository tokenRepository;
    private final String toMessageTokenWord = "Verification Token";

    private UniversalResponse updateResponse;
    public final UniversalResponse update(final VerificationTokenSaveRequest request) {
        tokenRepository.findById(request.getUserId()).ifPresentOrElse(
                (foundToken) -> {
                    foundToken.setToken(request.getToken());
                    foundToken.setActivated(request.getActivated());

                    log.info(UPDATE_MESSAGE, toMessageTokenWord, request.getUserId());
                    tokenRepository.save(foundToken);

                    updateResponse = UniversalResponse.builder()
                            .date(new Date(System.currentTimeMillis()))
                            .success(true)
                            .message(SUCCESS_UPDATE)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, "Token", request.getUserId());
                    updateResponse = UniversalResponse.builder()
                            .date(new Date(System.currentTimeMillis()))
                            .success(false)
                            .message(FAIL_UPDATE)
                            .build();
                }
        ); return  updateResponse;
    }

    private UniversalResponse deleteResponse;
    public final UniversalResponse delete(final Long id) {
        tokenRepository.findById(id).ifPresentOrElse(
                (foundToken) -> {
                    log.info(DELETING_MESSAGE, toMessageTokenWord, id);
                    tokenRepository.deleteById(id);

                    deleteResponse = UniversalResponse.builder()
                            .date(new Date(System.currentTimeMillis()))
                            .success(true)
                            .message(SUCCESS_DELETE)
                            .build();
                }, () -> {
                    log.warn(NOT_FOUND_BY_ID, "Token", id);
                    deleteResponse = UniversalResponse.builder()
                            .date(new Date(System.currentTimeMillis()))
                            .success(false)
                            .message(SUCCESS_DELETE)
                            .build();
                }
        ); return deleteResponse;
    }

    private VerificationTokenResponse tokenByIdResponse;
    public final VerificationTokenResponse getById(final Long id) {
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
                            .date(new Date(System.currentTimeMillis()))
                            .message(SUCCESS_FETCHING)
                            .verificationToken(tokenDTO)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageTokenWord, id);
                    tokenByIdResponse = VerificationTokenResponse.builder()
                            .success(false)
                            .date(new Date(System.currentTimeMillis()))
                            .message(FAIL_FETCHING)
                            .verificationToken(null)
                            .build();
                }
        ); return tokenByIdResponse;
    }

    private VerificationTokenResponse tokenByNameResponse;
    public final VerificationTokenResponse getByTokenName(final String desiredToken) {
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
                            .date(new Date(System.currentTimeMillis()))
                            .message(SUCCESS_FETCHING)
                            .verificationToken(tokenDTO)
                            .build();
                }, () -> {
                    log.warn(NOT_FOUND_BY_STH, toMessageTokenWord, "TokenName", desiredToken);
                    tokenByNameResponse = VerificationTokenResponse.builder()
                            .success(false)
                            .date(new Date(System.currentTimeMillis()))
                            .message(FAIL_FETCHING)
                            .verificationToken(null)
                            .build();
                }
        ); return tokenByNameResponse;
    }

    public final List<VerificationTokenDTO> getAll() {
        log.info(FETCHING_ALL_MESSAGE, "VerificationToken");
        return toVerificationTokens(tokenRepository.findAll());
    }
}
