package vn.tonish.hozo.rest.responseRes;

import java.util.List;

import vn.tonish.hozo.model.Comment;

/**
 * Created by LongBui on 5/11/2017.
 */

public class TaskResponse {

    private Integer id;
    private Integer categoryId;
    private String title;
    private String description;
    private String startTime;
    private String endTime;
    private String status;
    private Integer commentsCount;
    private String gender;
    private Integer minAge;
    private Integer maxAge;
    private Double latitude;
    private Double longitude;
    private String city;
    private String district;
    private String address;
    private Integer workerRate;
    private Integer workerCount;
    private List<String> attachments = null;
    private String currency;
    private Poster poster;
    private List<Bidder> bidders = null;
    private List<Comment> comments = null;




}
