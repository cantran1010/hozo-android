package vn.tonish.hozo.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;
import vn.tonish.hozo.database.entity.AssignerEntity;
import vn.tonish.hozo.database.entity.BidderEntity;
import vn.tonish.hozo.database.entity.CategoryEntity;
import vn.tonish.hozo.database.entity.CommentEntity;
import vn.tonish.hozo.database.entity.PosterEntity;
import vn.tonish.hozo.database.entity.SettingEntiny;
import vn.tonish.hozo.database.entity.TaskEntity;
import vn.tonish.hozo.database.manager.CategoryManager;
import vn.tonish.hozo.database.manager.SettingManager;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.model.Comment;
import vn.tonish.hozo.rest.responseRes.Assigner;
import vn.tonish.hozo.rest.responseRes.Bidder;
import vn.tonish.hozo.rest.responseRes.Poster;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by Can Tran on 24/04/2017.
 */

public class DataParse {
    private final static String TAG = DataParse.class.getSimpleName();


    public static Category convertCatogoryEntityToCategory(CategoryEntity categoryEntity) {
        Category category = new Category();
        category.setId(categoryEntity.getId());
        category.setName(categoryEntity.getName());
        category.setDescription(categoryEntity.getDescription());
        category.setSuggestTitle(categoryEntity.getSuggestTitle());
        category.setSuggestDescription(categoryEntity.getSuggestDescription());
        category.setPresentPath(categoryEntity.getPresentPath());
        category.setSelected(categoryEntity.isSelected());
        category.setStatus(categoryEntity.getStatus());
        return category;
    }

    private static CategoryEntity convertCatogoryToCategoryEntity(Category category) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(category.getId());
        categoryEntity.setName(category.getName());
        categoryEntity.setDescription(category.getDescription());
        categoryEntity.setSuggestTitle(category.getSuggestTitle());
        categoryEntity.setSuggestDescription(category.getSuggestDescription());
        categoryEntity.setPresentPath(category.getPresentPath());
        categoryEntity.setSelected(category.isSelected());
        categoryEntity.setStatus(category.getStatus());
        return categoryEntity;
    }

    public static List<CategoryEntity> convertListCategoryToListCategoryEntity(List<Category> categories) {
        List<CategoryEntity> categoryEntities = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            categoryEntities.add(convertCatogoryToCategoryEntity(categories.get(i)));
        }
        return categoryEntities;
    }

    public static List<Category> convertListCategoryEntityToListCategory(List<CategoryEntity> categories) {
        List<Category> categories1 = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            categories1.add(convertCatogoryEntityToCategory(categories.get(i)));
        }
        return categories1;
    }


    public static TaskResponse converTaskEntityToTaskReponse(TaskEntity taskEntity) {
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(taskEntity.getId());
        taskResponse.setCategoryId(taskEntity.getCategoryId());
        taskResponse.setTitle(taskEntity.getTitle());
        taskResponse.setDescription(taskEntity.getDescription());
        taskResponse.setStartTime(DateTimeUtils.fromDateIso(taskEntity.getStartTime()));
        taskResponse.setEndTime(DateTimeUtils.fromDateIso(taskEntity.getEndTime()));
        taskResponse.setStatus(taskEntity.getStatus());
        taskResponse.setOfferStatus(taskEntity.getOfferStatus());
        taskResponse.setCommentsCount(taskEntity.getCommentsCount());
        taskResponse.setGender(taskEntity.getGender());
        taskResponse.setMinAge(taskEntity.getMinAge());
        taskResponse.setMaxAge(taskEntity.getMaxAge());
        taskResponse.setLatitude(taskEntity.getLatitude());
        taskResponse.setLongitude(taskEntity.getLongitude());
        taskResponse.setCity(taskEntity.getCity());
        taskResponse.setDistrict(taskEntity.getDistrict());
        taskResponse.setAddress(taskEntity.getAddress());
        taskResponse.setWorkerRate(taskEntity.getWorkerRate());
        taskResponse.setWorkerCount(taskEntity.getWorkerCount());
        taskResponse.setAssigneeCount(taskEntity.getAssigneeCount());
        taskResponse.setBidderCount(taskEntity.getBidderCount());
        taskResponse.setCurrency(taskEntity.getCurrency());
        taskResponse.setRatePoster(taskEntity.isRatePoster());
        taskResponse.setPoster(convertPosterEntityToPoster(taskEntity.getPoster()));
        taskResponse.setRole(taskEntity.getRole());
        taskResponse.setCreatedAt(DateTimeUtils.fromDateIso(taskEntity.getCreatedAt()));

        List<String> listAttachments = new ArrayList<>();

        if (taskEntity.getAttachments() != null) {
            String[] arrAtachments = taskEntity.getAttachments().split(",");
            for (String arrAtachment : arrAtachments)
                if (!arrAtachment.equals(""))
                    listAttachments.add(arrAtachment);
        }

        taskResponse.setAttachments(listAttachments);

        taskResponse.setBidders(convertListBidderEntityToListBidder(taskEntity.getBidders()));

        taskResponse.setAssignees(convertListAssignerEntityToListAssigner(taskEntity.getAssignees()));

        taskResponse.setComments(convertListCommentEntityToListComment(taskEntity.getComments()));

        return taskResponse;
    }

    public static TaskEntity converTaskReponseToTaskEntity(TaskResponse taskResponse) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(taskResponse.getId());
        taskEntity.setCategoryId(taskResponse.getCategoryId());
        taskEntity.setTitle(taskResponse.getTitle());
        taskEntity.setDescription(taskResponse.getDescription());
        taskEntity.setStartTime(DateTimeUtils.getDateFromStringIso(taskResponse.getStartTime()));
        taskEntity.setEndTime(DateTimeUtils.getDateFromStringIso(taskResponse.getEndTime()));
        taskEntity.setStatus(taskResponse.getStatus());
        taskEntity.setOfferStatus(taskResponse.getOfferStatus());
        taskEntity.setCommentsCount(taskResponse.getCommentsCount());
        taskEntity.setGender(taskResponse.getGender());
        taskEntity.setMinAge(taskResponse.getMinAge());
        taskEntity.setMaxAge(taskResponse.getMaxAge());
        taskEntity.setLatitude(taskResponse.getLatitude());
        taskEntity.setLongitude(taskResponse.getLongitude());
        taskEntity.setCity(taskResponse.getCity());
        taskEntity.setDistrict(taskResponse.getDistrict());
        taskEntity.setAddress(taskResponse.getAddress());
        taskEntity.setWorkerRate(taskResponse.getWorkerRate());
        taskEntity.setWorkerCount(taskResponse.getWorkerCount());
        taskEntity.setAssigneeCount(taskResponse.getAssigneeCount());
        taskEntity.setBidderCount(taskResponse.getBidderCount());
        taskEntity.setCurrency(taskResponse.getCurrency());
        taskEntity.setRatePoster(taskResponse.isRatePoster());
        taskEntity.setPoster(convertPosterToPosterEntity(taskResponse.getPoster()));
        taskEntity.setRole(taskResponse.getRole());
        taskEntity.setCreatedAt(DateTimeUtils.getDateFromStringIso(taskResponse.getCreatedAt()));

        List<String> atachments = taskResponse.getAttachments();
        String strAtachments = "";
        for (int i = 0; i < atachments.size(); i++) {
            strAtachments = strAtachments + "," + atachments.get(i);
        }
        taskEntity.setAttachments(strAtachments);

        taskEntity.setBidders(convertListBidderToListBidderEntity(taskResponse.getBidders()));

        taskEntity.setAssignees(convertListAssigerToListAssignerEntity(taskResponse.getAssignees()));

        taskEntity.setComments(convertListCommentToListCommentEntity(taskResponse.getComments()));

        return taskEntity;
    }

    private static Poster convertPosterEntityToPoster(PosterEntity posterEntity) {
        Poster poster = new Poster();
        poster.setId(posterEntity.getId());
        poster.setAvatar(posterEntity.getAvatar());
        poster.setFullName(posterEntity.getFullName());
        poster.setPhone(posterEntity.getPhone());
        poster.setPosterAverageRating(posterEntity.getPosterAverageRating());
        poster.setTaskId(posterEntity.getTaskId());
        poster.setVerify(posterEntity.getVerify());
        return poster;
    }

    private static PosterEntity convertPosterToPosterEntity(Poster poster) {
        PosterEntity posterEntity = new PosterEntity();
        posterEntity.setId(poster.getId());
        posterEntity.setAvatar(poster.getAvatar());
        posterEntity.setFullName(poster.getFullName());
        posterEntity.setPhone(poster.getPhone());
        posterEntity.setPosterAverageRating(poster.getPosterAverageRating());
        posterEntity.setTaskId(poster.getTaskId());
        posterEntity.setVerify(poster.getVerify());
        return posterEntity;
    }

    private static Bidder convertBidderEntityToBidder(BidderEntity bidderEntity) {
        Bidder bidder = new Bidder();
        bidder.setId(bidderEntity.getId());
        bidder.setVerify(bidderEntity.getVerify());
        bidder.setPhone(bidderEntity.getPhone());
        bidder.setFullName(bidderEntity.getFullName());
        bidder.setAvatar(bidderEntity.getAvatar());
        bidder.setBidedAt(bidderEntity.getBidedAt());
        bidder.setTaskerAverageRating(bidderEntity.getTaskerAverageRating());
        return bidder;
    }

    public static BidderEntity convertBidderToBidderEntity(Bidder bidder) {
        BidderEntity bidderEntity = new BidderEntity();
        bidderEntity.setId(bidder.getId());
        bidderEntity.setVerify(bidder.getVerify());
        bidderEntity.setPhone(bidder.getPhone());
        bidderEntity.setFullName(bidder.getFullName());
        bidderEntity.setAvatar(bidder.getAvatar());
        bidderEntity.setBidedAt(bidder.getBidedAt());
        bidderEntity.setTaskerAverageRating(bidder.getTaskerAverageRating());
        return bidderEntity;
    }

    private static List<Bidder> convertListBidderEntityToListBidder(List<BidderEntity> bidderEntities) {
        List<Bidder> bidders = new ArrayList<>();
        for (BidderEntity bidderEntity : bidderEntities)
            bidders.add(convertBidderEntityToBidder(bidderEntity));
        return bidders;
    }

    private static RealmList<BidderEntity> convertListBidderToListBidderEntity(List<Bidder> bidders) {
        RealmList<BidderEntity> bidderEntities = new RealmList<>();
        for (Bidder bidder : bidders) bidderEntities.add(convertBidderToBidderEntity(bidder));
        return bidderEntities;
    }

    public static Assigner convertAssignerEntityToAssign(AssignerEntity assignerEntity) {
        Assigner assigner = new Assigner();
        assigner.setId(assignerEntity.getId());
        assigner.setTaskerAverageRating(assignerEntity.getTaskerAverageRating());
        assigner.setAvatar(assignerEntity.getAvatar());
        assigner.setFullName(assignerEntity.getFullName());
        assigner.setBiddedAt(assignerEntity.getBiddedAt());
        assigner.setPhone(assignerEntity.getPhone());
        assigner.setRating(assignerEntity.getRating());
        assigner.setVerify(assignerEntity.getVerify());
        return assigner;
    }

    public static AssignerEntity convertAssignerToAssignEntity(Assigner assigner) {
        AssignerEntity assignerEntity = new AssignerEntity();
        assignerEntity.setId(assigner.getId());
        assignerEntity.setTaskerAverageRating(assigner.getTaskerAverageRating());
        assignerEntity.setAvatar(assigner.getAvatar());
        assignerEntity.setFullName(assigner.getFullName());
        assignerEntity.setBiddedAt(assigner.getBiddedAt());
        assignerEntity.setPhone(assigner.getPhone());
        assignerEntity.setRating(assigner.getRating());
        assignerEntity.setVerify(assigner.getVerify());
        return assignerEntity;
    }

    public static List<Assigner> convertListAssignerEntityToListAssigner(List<AssignerEntity> assignerEntities) {
        List<Assigner> assigners = new ArrayList<>();
        for (AssignerEntity assignerEntity : assignerEntities)
            assigners.add(convertAssignerEntityToAssign(assignerEntity));
        return assigners;
    }

    public static RealmList<AssignerEntity> convertListAssigerToListAssignerEntity(List<Assigner> assigners) {
        RealmList<AssignerEntity> assignerEntities = new RealmList<>();
        for (Assigner assigner : assigners)
            assignerEntities.add(convertAssignerToAssignEntity(assigner));
        return assignerEntities;
    }

    public static CommentEntity convertCommentToCommentEntity(Comment comment) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(comment.getId());
        commentEntity.setFullName(comment.getFullName());
        commentEntity.setAvatar(comment.getAvatar());
        commentEntity.setAuthorId(comment.getAuthorId());
        commentEntity.setBody(comment.getBody());
        commentEntity.setCreatedAt(comment.getCreatedAt());
        commentEntity.setCreatedDateAt(comment.getCreatedDateAt());
        commentEntity.setImgAttach(comment.getImgAttach());
        commentEntity.setTaskId(comment.getTaskId());
        return commentEntity;
    }

    public static Comment convertCommentEntityToComment(CommentEntity commentEntity) {
        Comment comment = new Comment();
        comment.setId(commentEntity.getId());
        comment.setFullName(commentEntity.getFullName());
        comment.setAvatar(commentEntity.getAvatar());
        comment.setAuthorId(commentEntity.getAuthorId());
        comment.setBody(commentEntity.getBody());
        comment.setCreatedAt(commentEntity.getCreatedAt());
        comment.setCreatedDateAt(commentEntity.getCreatedDateAt());
        comment.setImgAttach(commentEntity.getImgAttach());
        comment.setTaskId(commentEntity.getTaskId());
        return comment;
    }

    public static List<Comment> convertListCommentEntityToListComment(List<CommentEntity> commentEntities) {
        List<Comment> comments = new ArrayList<>();
        for (CommentEntity commentEntity : commentEntities)
            comments.add(convertCommentEntityToComment(commentEntity));
        return comments;
    }

    public static RealmList<CommentEntity> convertListCommentToListCommentEntity(List<Comment> comments) {
        RealmList<CommentEntity> commentEntities = new RealmList<>();
        for (Comment comment : comments)
            commentEntities.add(convertCommentToCommentEntity(comment));
        return commentEntities;
    }

    public static List<TaskEntity> convertListTaskResponseToTaskEntity(List<TaskResponse> taskResponses) {
        List<TaskEntity> taskEntities = new ArrayList<>();
        for (int i = 0; i < taskResponses.size(); i++)
            taskEntities.add(converTaskReponseToTaskEntity(taskResponses.get(i)));
        return taskEntities;
    }

    public static Map<String, String> setParameterGetTasks(String sortBy, String limit, String since, String query) {
        SettingEntiny settingEntiny = SettingManager.getSettingEntiny();
        Map<String, String> option = new HashMap<>();
        if (settingEntiny.getMinWorkerRate() > 0)
            option.put("min_worker_rate", String.valueOf(settingEntiny.getMinWorkerRate()));
        if (settingEntiny.getMaxWorkerRate() > 0)
            option.put("max_worker_rate", String.valueOf(settingEntiny.getMaxWorkerRate()));
        option.put("latitude", String.valueOf(settingEntiny.getLatitude()));
        option.put("longitude", String.valueOf(settingEntiny.getLongitude()));
        option.put("distance", String.valueOf(settingEntiny.getRadius()));
        if (sortBy != null) option.put("sort_by", sortBy);
        option.put("limit", limit);
        if (since != null) option.put("since", since);
        if (query != null) option.put("query", query);
        LogUtils.d(TAG, " set option :" + option.toString());
        return option;
    }

    public static List<Long> getIds() {
        List<CategoryEntity> entityRealmList = CategoryManager.getAllCategories();
        List<Long> ids = new ArrayList<>();
        for (CategoryEntity categoryEntity : entityRealmList) {
            if (categoryEntity.isSelected())
                ids.add((long) categoryEntity.getId());
        }
        return ids;

    }


}