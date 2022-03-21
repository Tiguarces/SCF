package pl.scf.model.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.api.model.UniversalResponse;
import pl.scf.model.VerificationToken;
import pl.scf.model.repositories.IVerificationTokenRepository;

import java.util.Date;
import java.util.List;

import static pl.scf.api.ApiConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationTokenService {
    private final IVerificationTokenRepository tokenRepository;
    private final String toMessageTokenWord = "Verification Token";

    private UniversalResponse updateResponse;
    public final UniversalResponse update(final VerificationToken token) {
        tokenRepository.findById(token.getId()).ifPresentOrElse(
                (foundToken) -> {
                    log.info(UPDATE_MESSAGE, toMessageTokenWord, token.getId());
                    tokenRepository.save(foundToken);

                    updateResponse = UniversalResponse.builder()
                            .date(new Date(System.currentTimeMillis()))
                            .success(true)
                            .response(SUCCESS_UPDATE)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, "Token", token.getId());
                    updateResponse = UniversalResponse.builder()
                            .date(new Date(System.currentTimeMillis()))
                            .success(false)
                            .response(FAIL_UPDATE)
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
                            .response(SUCCESS_DELETE)
                            .build();
                }, () -> {
                    log.warn(NOT_FOUND_BY_ID, "Token", id);
                    deleteResponse = UniversalResponse.builder()
                            .date(new Date(System.currentTimeMillis()))
                            .success(false)
                            .response(SUCCESS_DELETE)
                            .build();
                }
        ); return deleteResponse;
    }

    private VerificationToken tokenById;
    public final VerificationToken getById(final Long id) {
        tokenRepository.findById(id).ifPresentOrElse(
                (foundToken) -> {
                    log.info(FETCH_BY_ID, toMessageTokenWord, id);
                    tokenById = foundToken;
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageTokenWord, id);
                    tokenById = new VerificationToken();
                }
        ); return tokenById;
    }

    private VerificationToken tokenByName;
    public final VerificationToken findByTokenName(final String desiredToken) {
        tokenRepository.findByToken(desiredToken).ifPresentOrElse(
                (foundToken) -> {
                    log.info(FETCH_BY_ID, tokenByName, foundToken.getId());
                    tokenByName = foundToken;
                }, () -> {
                    log.warn(NOT_FOUND_BY_STH, toMessageTokenWord, "TokenName", desiredToken);
                    tokenById = new VerificationToken();
                }
        ); return  tokenByName;
    }

    public final List<VerificationToken> getAll() {
        log.info(FETCHING_ALL_MESSAGE, "VerificationToken");
        return tokenRepository.findAll();
    }
}
