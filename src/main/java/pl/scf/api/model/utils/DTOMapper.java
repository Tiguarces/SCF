package pl.scf.api.model.utils;

import pl.scf.api.model.dto.*;
import pl.scf.model.*;

import java.util.List;

public final class DTOMapper {

    public static List<TopicCategoryDTO> toTopicCategory(final List<TopicCategory> rawTopicCategories) {
        return rawTopicCategories.parallelStream()
                .map(DTOMapper::toTopicDTO)
                .toList();
    }

    private static TopicCategoryDTO toTopicDTO(TopicCategory topicCategory) {
        final String[] subCategories = topicCategory
                .getSubCategory()
                .stream()
                .map(TopicSubCategory::getName)
                .toArray(String[]::new);

        return TopicCategoryDTO.builder()
                .categoryName(topicCategory.getName())
                .subCategoryNames(subCategories)
                .build();
    }

    public static List<String> toTopicCategoryAllNames(final List<TopicCategory> rawTopicCategories) {
        return rawTopicCategories.parallelStream()
                .map(TopicCategory::getName)
                .toList();
    }

    /////////////////////////////////////////////////

    public static List<AnswerDTO> toAnswer(final List<Answer> rawAnswers) {
        return rawAnswers.parallelStream()
                .map(DTOMapper::toAnswerDTO)
                .toList();
    }

    private static AnswerDTO toAnswerDTO(final Answer answer) {
        return AnswerDTO.builder()
                .topicId(answer.getTopic().getId())
                .createdDate(answer.getCreatedDate())
                .forumUserId(answer.getUser().getId())
                .content(answer.getContent())
                .build();
    }

    /////////////////////////////////////////////////

    public static List<AppUserDTO> toAppUser(final List<AppUser> rawUsers) {
        return rawUsers
                .parallelStream()
                .map(DTOMapper::toAppUserDTO)
                .toList();
    }

    private static AppUserDTO toAppUserDTO(final AppUser appUser) {
        return AppUserDTO.builder()
                .verTokenId(appUser.getToken().getId())
                .username(appUser.getUsername())
                .detailsId(appUser.getUser_details().getId())
                .roleName(appUser.getRole().getName())
                .build();
    }

    /////////////////////////////////////////////////

    public static List<AppUserDetailsDTO> toDetails(final List<AppUserDetails> rawDetails) {
        return rawDetails
                .parallelStream()
                .map(DTOMapper::toDetailsDTO)
                .toList();
    }

    private static AppUserDetailsDTO toDetailsDTO(final AppUserDetails appUserDetails) {
        return AppUserDetailsDTO.builder()
                .nickname(appUserDetails.getNickname())
                .appUserId(appUserDetails.getUser().getId())
                .forumUserId(appUserDetails.getForumUser().getId())
                .email(appUserDetails.getEmail())
                .createdDate(appUserDetails.getCreatedDate())
                .build();
    }

    /////////////////////////////////////////////////

    public static List<ForumUserDescriptionDTO> toForumUserDescription(final List<ForumUserDescription> rawDescriptions) {
        return rawDescriptions
                .parallelStream()
                .map(DTOMapper::toDescriptionDTO)
                .toList();
    }

    private static ForumUserDescriptionDTO toDescriptionDTO(final ForumUserDescription forumUserDescription) {
        return ForumUserDescriptionDTO.builder()
                .content(forumUserDescription.getContent())
                .forumUserId(forumUserDescription.getId())
                .build();
    }

    /////////////////////////////////////////////////

    public static List<ForumUserImagesDTO> toForumUserImages(final List<ForumUserImages> rawImages) {
        return rawImages
                .parallelStream()
                .map(DTOMapper::toImagesDTO)
                .toList();
    }

    private static ForumUserImagesDTO toImagesDTO(final ForumUserImages forumUserImages) {
        return ForumUserImagesDTO.builder()
                .avatarImageURL(forumUserImages.getAvatarImageURL())
                .backgroundImageURL(forumUserImages.getBackgroundImageURL())
                .forumUserId(forumUserImages.getId())
                .build();
    }

    /////////////////////////////////////////////////

    public static List<ForumUserDTO> toForumUser(final List<ForumUser> rawForumUsers) {
        return rawForumUsers
                .parallelStream()
                .map(DTOMapper::toForumUserDTO)
                .toList();
    }

    private static ForumUserDTO toForumUserDTO(final ForumUser forumUser) {
        return ForumUserDTO.builder()
                .descriptionId(forumUser.getDescription().getId())
                .imagesId(forumUser.getImages().getId())
                .appUserId(forumUser.getUser().getId())
                .topicTitle(forumUser.getTitle().getTitleName())
                .reputation(forumUser.getReputation())
                .visitors(forumUser.getVisitors())
                .build();
    }

    /////////////////////////////////////////////////

    public static List<ForumUserTitleDTO> toForumUserTitle(final List<ForumUserTitle> rawTitles) {
        return rawTitles
                .parallelStream()
                .map(DTOMapper::toTitlesDTO)
                .toList();
    }

    private static ForumUserTitleDTO toTitlesDTO(final ForumUserTitle forumUserTitles) {
        return ForumUserTitleDTO.builder()
                .titleName(forumUserTitles.getTitleName())
                .rangeIntervalPoints(forumUserTitles.getRangeIntervalPoints())
                .build();
    }

    /////////////////////////////////////////////////

    public static List<TopicSubCategoryDTO> toSubCategory(final List<TopicSubCategory> rawSubCategories) {
        return rawSubCategories
                .parallelStream()
                .map(DTOMapper::toSubCategoryDTO)
                .toList();
    }

    private static TopicSubCategoryDTO toSubCategoryDTO(final TopicSubCategory topicSubCategory) {
        return TopicSubCategoryDTO.builder()
                .name(topicSubCategory.getName())
                .topicNameCategory(topicSubCategory.getCategory().getName())
                .build();
    }

    public static List<String> toSubCategoryNames(final List<TopicSubCategory> rawSubCategories) {
        return rawSubCategories
                .parallelStream()
                .map(TopicSubCategory::getName)
                .toList();
    }

    public static String[] toSubCategoryNamesToArray(final List<TopicSubCategory> rawSubCategories) {
        return rawSubCategories
                .parallelStream()
                .map(TopicSubCategory::getName)
                .toArray(String[]::new);
    }

    /////////////////////////////////////////////////

    public static List<TopicDetailsDTO> toTopicDetails(final List<TopicDetails> rawTopicDetails) {
        return rawTopicDetails
                .parallelStream()
                .map(DTOMapper::toTopicDetailsDTO)
                .toList();
    }

    private static TopicDetailsDTO toTopicDetailsDTO(final TopicDetails topicDetails) {
        return TopicDetailsDTO.builder()
                .topicName(topicDetails.getTopicName())
                .description(topicDetails.getDescription())
                .topicId(topicDetails.getTopic().getId())
                .build();
    }

    /////////////////////////////////////////////////

    public static List<UserRoleDTO> toRoles(final List<UserRole> rawUserRoles) {
        return rawUserRoles
                .parallelStream()
                .map(DTOMapper::toRolesDTO)
                .toList();
    }

    private static UserRoleDTO toRolesDTO(final UserRole userRole) {
        return UserRoleDTO.builder()
                .name(userRole.getName())
                .build();
    }

    /////////////////////////////////////////////////

    public static List<VerificationTokenDTO> toVerificationTokens(final List<VerificationToken> rawVerificationTokens) {
        return rawVerificationTokens
                .parallelStream()
                .map(DTOMapper::toVerTokenDTO)
                .toList();
    }

    private static VerificationTokenDTO toVerTokenDTO(final VerificationToken verificationToken) {
        return VerificationTokenDTO.builder()
                .token(verificationToken.getToken())
                .activated(verificationToken.getActivated())
                .userId(verificationToken.getUser().getId())
                .build();
    }

}
