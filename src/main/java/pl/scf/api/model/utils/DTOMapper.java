package pl.scf.api.model.utils;

import org.springframework.security.core.GrantedAuthority;
import pl.scf.api.model.dto.*;
import pl.scf.model.*;

import java.util.Collection;
import java.util.List;

import static pl.scf.api.model.utils.ResponseUtil.formatDate;

public final class DTOMapper {

    public static List<TopicCategoryDTO> toTopicCategories(final List<TopicCategory> rawTopicCategories) {
        return rawTopicCategories.stream()
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
                .imageURL(topicCategory.getImageURL())
                .categoryName(topicCategory.getName())
                .subCategoryNames(subCategories)
                .build();
    }

    public static List<String> toTopicCategoryAllNames(final List<TopicCategory> rawTopicCategories) {
        return rawTopicCategories
                .stream()
                .map(TopicCategory::getName)
                .toList();
    }

    /////////////////////////////////////////////////

    public static List<AnswerDTO> toAnswers(final List<Answer> rawAnswers) {
        return rawAnswers
                .stream()
                .map(DTOMapper::toAnswerDTO)
                .toList();
    }

    private static AnswerDTO toAnswerDTO(final Answer answer) {
        return AnswerDTO.builder()
                .topicId(answer.getTopic().getId())
//                .createdDate(answer.getCreatedDate().toString())
                .forumUserId(answer.getUser().getId())
                .content(answer.getContent())
                .build();
    }

    public static List<LastAnswerDTO> toLastAnswers(final List<Answer> lastRawAnswers) {
        return lastRawAnswers
                .stream()
                .map(DTOMapper::toLastAnswerDTO)
                .toList();
    }

    private static LastAnswerDTO toLastAnswerDTO(final Answer answer) {
        final Topic topic = answer.getTopic();
        final TopicDetails topicDetails = topic.getDetails();
        final TopicSubCategory subCategory = topic.getSubCategory();
        final TopicCategory topicCategory = subCategory.getCategory();
        final ForumUser forumUser = answer.getUser();
        final AppUser appUser = forumUser.getUser();
        final AppUserDetails appUserDetails = appUser.getUser_details();
        final ForumUserImages userImages = forumUser.getImages();

        return LastAnswerDTO.builder()
                .content(answer.getContent())
                .createdDate(answer.getCreatedDate())
                .topicId(topic.getId())
                .topicName(topicDetails.getTopicName())
                .topicCategory(topicCategory.getName())
                .topicSubCategory(subCategory.getName())
                .avatarImageURL(userImages.getAvatarImageURL())
                .userNickname(appUserDetails.getNickname())
                .build();
    }

    public static List<ExtendedAnswerDTO> toExtendedAnswers(final List<Answer> answers) {
        return answers
                .stream()
                .map(DTOMapper::toExtendedAnswer)
                .toList();
    }

    private static ExtendedAnswerDTO toExtendedAnswer(final Answer answer) {
        final Topic answerTopic = answer.getTopic();
        final TopicSubCategory subCategory = answerTopic.getSubCategory();
        final TopicCategory topicCategory = subCategory.getCategory();
        final TopicDetails topicDetails = answerTopic.getDetails();

        final AnswerDTO answerDTO = AnswerDTO.builder()
                .content(answer.getContent())
                .createdDate("Unknown")
                .forumUserId(answer.getUser().getId())
                .topicId(answerTopic.getId())
                .build();

        return ExtendedAnswerDTO.builder()
                .answer(answerDTO)
                .subCategoryName(subCategory.getName())
                .categoryName(topicCategory.getName())
                .topicName(topicDetails.getTopicName())
                .build();
    }

    /////////////////////////////////////////////////

    public static List<AppUserDTO> toAppUsers(final List<AppUser> rawUsers) {
        return rawUsers
                .stream()
                .map(DTOMapper::toAppUserDTO)
                .toList();
    }

    private static AppUserDTO toAppUserDTO(final AppUser appUser) {
        return AppUserDTO.builder()
                .verTokenId(appUser.getToken().getId())
                .detailsId(appUser.getUser_details().getId())
                .roleName(appUser.getRole().getName())
                .build();
    }

    /////////////////////////////////////////////////

    public static List<AppUserDetailsDTO> toAppUserDetails(final List<AppUserDetails> rawDetails) {
        return rawDetails
                .stream()
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

    public static List<ForumUserDescriptionDTO> toForumUserDescriptions(final List<ForumUserDescription> rawDescriptions) {
        return rawDescriptions
                .stream()
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
                .stream()
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

    public static List<ForumUserDTO> toForumUsers(final List<ForumUser> rawForumUsers) {
        return rawForumUsers
                .stream()
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

    public static List<ForumUserTitleDTO> toForumUserTitles(final List<ForumUserTitle> rawTitles) {
        return rawTitles
                .stream()
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

    public static List<TopicSubCategoryDTO> toSubCategories(final List<TopicSubCategory> rawSubCategories) {
        return rawSubCategories
                .stream()
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
                .stream()
                .map(TopicSubCategory::getName)
                .toList();
    }

    public static String[] toSubCategoryNamesToArray(final List<TopicSubCategory> rawSubCategories) {
        return rawSubCategories
                .stream()
                .map(TopicSubCategory::getName)
                .toArray(String[]::new);
    }



    /////////////////////////////////////////////////

    public static List<TopicDetailsDTO> toTopicDetails(final List<TopicDetails> rawTopicDetails) {
        return rawTopicDetails
                .stream()
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

    public static List<ExtendedTopicDetailsDTO> toExtendedTopicDetails(final List<Topic> rawTopicDetails) {
        return rawTopicDetails
                .stream()
                .map(DTOMapper::toExtendedTopicDetailsDTO)
                .toList();
    }

    private static ExtendedTopicDetailsDTO toExtendedTopicDetailsDTO(final Topic topic) {
        final TopicDetails topicDetails = topic.getDetails();
        final TopicSubCategory subCategory = topic.getSubCategory();
        final TopicCategory topicCategory = subCategory.getCategory();

        return ExtendedTopicDetailsDTO.builder()
                .topicId(topic.getId())
                .categoryName(topicCategory.getName())
                .subCategoryName(subCategory.getName())
                .description(topicDetails.getDescription())
                .topicName(topicDetails.getTopicName())
//                .createdDate(formatDate(topic.getCreatedDate()))
                .build();
    }

    /////////////////////////////////////////////////

    public static List<UserRoleDTO> toRoles(final List<UserRole> rawUserRoles) {
        return rawUserRoles
                .stream()
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
                .stream()
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

    /////////////////////////////////////////////////

    public static List<String> toUserRoles(final Collection<GrantedAuthority> rawAuthorities) {
        return rawAuthorities
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }

    /////////////////////////////////////////////////

    public static List<TopicDTO> toTopics(final List<Topic> rawTopics) {
        return rawTopics
                .stream()
                .map(DTOMapper::toTopicDTO)
                .toList();
    }

    private static TopicDTO toTopicDTO(final Topic topic) {
        return TopicDTO.builder()
                .userId(topic.getUser().getId())
                .detailsId(topic.getDetails().getId())
                .subCategoryName(topic.getSubCategory().getName())
                .build();
    }

    public static List<LastTopicDTO> toLastTopics(final List<Topic> lastRawTopics) {
        return lastRawTopics
                .stream()
                .map(DTOMapper::toLastTopicDTO)
                .toList();
    }

    private static LastTopicDTO toLastTopicDTO(final Topic topic) {
        final TopicDetails topicDetails = topic.getDetails();
        final ForumUser forumUser = topic.getUser();
        final AppUser appUser = forumUser.getUser();
        final AppUserDetails appUserDetails = appUser.getUser_details();
        final TopicCategory topicCategory = topic.getSubCategory().getCategory();
        final ForumUserImages userImages = forumUser.getImages();

        return LastTopicDTO.builder()
                .userId(topic.getUser().getId())
                .subCategoryName(topic.getSubCategory().getName())
                .topicName(topicDetails.getTopicName())
//                .createdDate(formatDate(topic.getCreatedDate()))
                .userNickname(appUserDetails.getNickname())
                .topicCategoryName(topicCategory.getName())
                .numberOfAnswers(getNumberOfAnswers(forumUser))
                .avatarImageURL(userImages.getAvatarImageURL())
                .build();
    }

    private static Integer getNumberOfAnswers(final ForumUser forumUser) {
        return forumUser
                .getAnswers()
                .size();
    }

    public static TopicDTO toTopic(final Topic topic) {
        return TopicDTO.builder()
                .detailsId(topic.getDetails().getId())
                .subCategoryName(topic.getSubCategory().getName())
                .userId(topic.getUser().getId())
                .build();
    }

}
