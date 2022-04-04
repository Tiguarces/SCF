package pl.scf.model.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.scf.api.model.dto.UserRoleDTO;
import pl.scf.api.model.request.UserRoleUpdateRequest;
import pl.scf.api.model.response.UniversalResponse;
import pl.scf.api.model.response.UserRoleResponse;
import pl.scf.model.UserRole;
import pl.scf.model.repositories.IUserRoleRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static pl.scf.api.model.utils.ApiConstants.*;
import static pl.scf.api.model.utils.DTOMapper.toRoles;

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
                    .date(new Date(System.currentTimeMillis()))
                    .message("RoleName is null")
                    .build();
        } else {
            Optional.ofNullable(roleRepository.findByName(roleName)).ifPresentOrElse(
                    (foundRole) -> {
                        log.warn("Role exists, skipping adding");
                        saveResponse = UniversalResponse.builder()
                                .success(false)
                                .date(new Date(System.currentTimeMillis()))
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
                                .date(new Date(System.currentTimeMillis()))
                                .message(SUCCESS_SAVING)
                                .build();
                    }
            );
        } return saveResponse;
    }

    private UserRoleResponse roleByIdResponse;
    public final UserRoleResponse getById(final Long id) {
        roleRepository.findById(id).ifPresentOrElse(
                (foundRole) -> {
                    log.info(FETCH_BY_ID, toMessageUserRoleWord, id);
                    final UserRoleDTO roleDTO = UserRoleDTO.builder()
                            .name(foundRole.getName())
                            .build();

                    roleByIdResponse = UserRoleResponse.builder()
                            .success(true)
                            .date(new Date(System.currentTimeMillis()))
                            .message(SUCCESS_FETCHING)
                            .userRole(roleDTO)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageUserRoleWord, id);
                    roleByIdResponse = UserRoleResponse.builder()
                            .success(false)
                            .date(new Date(System.currentTimeMillis()))
                            .message(FAIL_FETCHING)
                            .userRole(null)
                            .build();
                }
        ); return roleByIdResponse;
    }

    private UserRoleResponse roleByNameResponse;
    public final UserRoleResponse getByName(final String name) {
        Optional.ofNullable(roleRepository.findByName(name)).ifPresentOrElse(
                (foundRole) -> {
                    log.info(FETCHING_BY_STH_MESSAGE, toMessageUserRoleWord, "name", name);
                    final UserRoleDTO roleDTO = UserRoleDTO.builder()
                            .name(foundRole.getName())
                            .build();

                    roleByNameResponse = UserRoleResponse.builder()
                            .success(true)
                            .date(new Date(System.currentTimeMillis()))
                            .message(SUCCESS_FETCHING)
                            .userRole(roleDTO)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_STH, toMessageUserRoleWord, "name", name);
                    roleByNameResponse = UserRoleResponse.builder()
                            .success(false)
                            .date(new Date(System.currentTimeMillis()))
                            .message(FAIL_FETCHING)
                            .userRole(null)
                            .build();
                }
        ); return roleByNameResponse;
    }

    private UniversalResponse updateResponse;
    public final UniversalResponse update(final UserRoleUpdateRequest request) {
        roleRepository.findById(request.getRoleId()).ifPresentOrElse(
                (foundRole) -> {
                    if(foundRole.getName().equalsIgnoreCase(request.getName())) {
                        log.warn("{} equal data", toMessageUserRoleWord);
                        updateResponse = UniversalResponse.builder()
                                .success(false)
                                .date(new Date(System.currentTimeMillis()))
                                .message("Role exists and names are equal | Skipping updating")
                                .build();

                    } else {
                        log.info(UPDATE_MESSAGE, toMessageUserRoleWord, foundRole.getId());
                        foundRole.setName(request.getName());
                        roleRepository.save(foundRole);

                        updateResponse = UniversalResponse.builder()
                                .success(true)
                                .date(new Date(System.currentTimeMillis()))
                                .message(SUCCESS_UPDATE)
                                .build();
                    }
                },
                () -> {
                    log.warn(NULLABLE_MESSAGE, "Request");
                    updateResponse = UniversalResponse.builder()
                            .success(false)
                            .date(new Date(System.currentTimeMillis()))
                            .message(FAIL_UPDATE)
                            .build();
                }
        ); return updateResponse;
    }

    private UniversalResponse deleteResponse;
    public final UniversalResponse delete(final Long id) {
        roleRepository.findById(id).ifPresentOrElse(
                (foundRole) -> {
                    log.info(DELETING_MESSAGE, toMessageUserRoleWord, id);
                    roleRepository.deleteById(id);

                    deleteResponse = UniversalResponse.builder()
                            .success(true)
                            .date(new Date(System.currentTimeMillis()))
                            .message(SUCCESS_DELETE)
                            .build();
                },
                () -> {
                    log.warn(NOT_FOUND_BY_ID, toMessageUserRoleWord, id);
                    deleteResponse = UniversalResponse.builder()
                            .success(false)
                            .date(new Date(System.currentTimeMillis()))
                            .message(FAIL_DELETE)
                            .build();
                }
        ); return deleteResponse;
    }

    public final List<UserRoleDTO> getAll() {
        log.info(FETCHING_ALL_MESSAGE, toMessageUserRoleWord);
        return toRoles(roleRepository.findAll());
    }
}
