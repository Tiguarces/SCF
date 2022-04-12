package pl.scf.model.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.api.model.dto.UserRoleDTO;
import pl.scf.api.model.exception.IdentificationException;
import pl.scf.api.model.exception.NotFoundException;
import pl.scf.api.model.request.UserRoleUpdateRequest;
import pl.scf.api.model.response.ExtendedUserRoleDTO;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.api.model.response.UserRoleResponse;
import pl.scf.model.UserRole;
import pl.scf.model.repositories.IUserRoleRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static pl.scf.api.model.utils.ApiConstants.*;
import static pl.scf.api.model.utils.DTOMapper.toRoles;
import static pl.scf.api.model.utils.ResponseUtil.throwExceptionWhenIdZero;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRoleService {
    private final IUserRoleRepository roleRepository;
    private final String toMessageUserRoleWord = "UserRole";

    private UniversalResponse saveResponse;
    public final UniversalResponse save(final String roleName) {
        if(roleName == null) {
            saveResponse = UniversalResponse.builder()
                    .success(false)
                    .date(Instant.now())
                    .message("RoleName is null")
                    .build();
        } else {
            Optional.ofNullable(roleRepository.findByName(roleName)).ifPresentOrElse(
                    (foundRole) -> {
                        log.warn("Role exists, skipping adding");
                        saveResponse = UniversalResponse.builder()
                                .success(false)
                                .date(Instant.now())
                                .message(FAIL_SAVING + " | Role exists")
                                .build();
                    },() -> {
                        final UserRole role = UserRole.builder()
                                .name(roleName)
                                .build();

                        log.info(SAVING, toMessageUserRoleWord);
                        roleRepository.save(role);

                        saveResponse = UniversalResponse.builder()
                                .success(true)
                                .date(Instant.now())
                                .message(SUCCESS_SAVING)
                                .build();
                    }
            );
        } return saveResponse;
    }

    private UserRoleResponse roleByIdResponse;
    public final UserRoleResponse getById(final Long id) throws NotFoundException, IdentificationException {
        throwExceptionWhenIdZero(id);

        roleRepository.findById(id).ifPresentOrElse(
                (foundRole) -> {
                    log.info(FETCH_BY_ID, toMessageUserRoleWord, id);
                    final UserRoleDTO roleDTO = UserRoleDTO.builder()
                            .name(foundRole.getName())
                            .build();

                    roleByIdResponse = UserRoleResponse.builder()
                            .success(true)
                            .date(Instant.now())
                            .message(SUCCESS_FETCHING)
                            .userRole(roleDTO)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found UserRole with specified id");
                }
        ); return roleByIdResponse;
    }

    private UserRoleResponse roleByNameResponse;
    public final UserRoleResponse getByName(final String name) throws NotFoundException{
        Optional.ofNullable(roleRepository.findByName(name)).ifPresentOrElse(
                (foundRole) -> {
                    log.info(FETCHING_BY_STH_MESSAGE, toMessageUserRoleWord, "name", name);
                    final UserRoleDTO roleDTO = UserRoleDTO.builder()
                            .name(foundRole.getName())
                            .build();

                    roleByNameResponse = UserRoleResponse.builder()
                            .success(true)
                            .date(Instant.now())
                            .message(SUCCESS_FETCHING)
                            .userRole(roleDTO)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found UserRole with specified name");
                }
        ); return roleByNameResponse;
    }

    private UniversalResponse updateResponse;
    public final UniversalResponse update(final UserRoleUpdateRequest request) throws NotFoundException, IdentificationException{
        throwExceptionWhenIdZero(request.getRoleId());

        roleRepository.findById(request.getRoleId()).ifPresentOrElse(
                (foundRole) -> {
                    if(foundRole.getName().equalsIgnoreCase(request.getName())) {
                        log.warn("{} equal data", toMessageUserRoleWord);
                        updateResponse = UniversalResponse.builder()
                                .success(false)
                                .date(Instant.now())
                                .message("Role exists and names are equals. Skipping updating")
                                .build();

                    } else {
                        log.info(UPDATE_MESSAGE, toMessageUserRoleWord, foundRole.getId());
                        foundRole.setName(request.getName());
                        roleRepository.save(foundRole);

                        updateResponse = UniversalResponse.builder()
                                .success(true)
                                .date(Instant.now())
                                .message(SUCCESS_UPDATE)
                                .build();
                    }
                },
                () -> {
                    throw new NotFoundException("Not found UserRole with specified id");
                }
        ); return updateResponse;
    }

    private UniversalResponse deleteResponse;
    public final UniversalResponse delete(final Long id) throws NotFoundException, IdentificationException {
        throwExceptionWhenIdZero(id);

        roleRepository.findById(id).ifPresentOrElse(
                (foundRole) -> {
                    log.info(DELETING_MESSAGE, toMessageUserRoleWord, id);
                    roleRepository.deleteById(id);

                    deleteResponse = UniversalResponse.builder()
                            .success(true)
                            .date(Instant.now())
                            .message(SUCCESS_DELETE)
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found UserRole with specified id");
                }
        ); return deleteResponse;
    }

    private ExtendedUserRoleDTO getUserRoleResponse;
    public final ExtendedUserRoleDTO getUserRole() throws NotFoundException {
        Optional.ofNullable(roleRepository.findByName("USER")).ifPresentOrElse(
                (foundRole) -> {
                    log.info(NOT_FOUND_BY_STH, toMessageUserRoleWord, "name", "USER");
                    getUserRoleResponse = ExtendedUserRoleDTO.builder()
                            .id(foundRole.getId())
                            .name(foundRole.getName())
                            .build();
                },
                () -> {
                    throw new NotFoundException("Not found User Role with specified role name");
                }
        ); return getUserRoleResponse;
    }

    public final List<UserRoleDTO> getAll() {
        log.info(FETCHING_ALL_MESSAGE, toMessageUserRoleWord);
        return toRoles(roleRepository.findAll());
    }
}
