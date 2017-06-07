package vn.tonish.hozo.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;
import vn.tonish.hozo.database.entity.CategoryEntity;
import vn.tonish.hozo.database.entity.SettingEntiny;
import vn.tonish.hozo.database.entity.TaskEntity;
import vn.tonish.hozo.database.manager.CategoryManager;
import vn.tonish.hozo.database.manager.SettingManager;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.model.Comment;
import vn.tonish.hozo.rest.responseRes.Assigner;
import vn.tonish.hozo.rest.responseRes.Bidder;
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
        taskResponse.setPoster(taskEntity.getPoster());
        taskResponse.setRole(taskEntity.getRole());
        taskResponse.setCreatedAt(DateTimeUtils.fromDateIso(taskEntity.getCreatedAt()));

        List<String> listAttachments = new ArrayList<>();

        if (taskEntity.getAttachments() != null) {
            String[] arrAtachments = taskEntity.getAttachments().split(",");
            for (String arrAtachment : arrAtachments)
                if (!arrAtachment.equals(""))
                    listAttachments.add(arrAtachment);
        }

//        List<String> listAttachments = new ArrayList<String>(Arrays.asList(arrAtachments));
        taskResponse.setAttachments(listAttachments);

        List<Bidder> bidders = new ArrayList<>();
        for (int i = 0; i < taskEntity.getBidders().size(); i++)
            bidders.add(taskEntity.getBidders().get(i));
        taskResponse.setBidders(bidders);

        List<Assigner> assigners = new ArrayList<>();
        for (int i = 0; i < taskEntity.getAssignees().size(); i++)
            assigners.add(taskEntity.getAssignees().get(i));
        taskResponse.setAssignees(assigners);

        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < taskEntity.getComments().size(); i++)
            comments.add(taskEntity.getComments().get(i));
        taskResponse.setComments(comments);

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
        taskEntity.setPoster(taskResponse.getPoster());
        taskEntity.setRole(taskResponse.getRole());
        taskEntity.setCreatedAt(DateTimeUtils.getDateFromStringIso(taskResponse.getCreatedAt()));

        List<String> atachments = taskResponse.getAttachments();
        String strAtachments = "";
        for (int i = 0; i < atachments.size(); i++) {
            strAtachments = strAtachments + "," + atachments.get(i);
        }
        taskEntity.setAttachments(strAtachments);

        RealmList<Bidder> bidders = new RealmList<>();
        if (taskResponse.getBidders() != null)
            for (int i = 0; i < taskResponse.getBidders().size(); i++)
                bidders.add(taskResponse.getBidders().get(i));
        taskEntity.setBidders(bidders);


        RealmList<Assigner> assigners = new RealmList<>();
        if (taskResponse.getAssignees() != null)
            for (int i = 0; i < taskResponse.getAssignees().size(); i++)
                assigners.add(taskResponse.getAssignees().get(i));
        taskEntity.setAssignees(assigners);

        RealmList<Comment> comments = new RealmList<>();
        if (taskResponse.getComments() != null)
            for (int i = 0; i < taskResponse.getComments().size(); i++)
                comments.add(taskResponse.getComments().get(i));
        taskEntity.setComments(comments);

        return taskEntity;
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
        option.put("category_ids[]", getIds());
        option.put("min_worker_rate", String.valueOf(settingEntiny.getMinWorkerRate()));
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

    private static String getIds() {
        List<CategoryEntity> entityRealmList = CategoryManager.getAllCategories();
        String ids = "";
        for (CategoryEntity categoryEntity : entityRealmList) {
            if (categoryEntity.isSelected())
                ids = ids + categoryEntity.getId() + ",";
        }
        LogUtils.d(TAG, "getIds" + ids.substring(0, ids.length() - 1));

        return ids.substring(0, ids.length() - 1);

    }


}
