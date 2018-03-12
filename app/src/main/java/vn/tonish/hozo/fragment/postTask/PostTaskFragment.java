package vn.tonish.hozo.fragment.postTask;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TimePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.PostTaskActivity;
import vn.tonish.hozo.activity.PrePayInfoActivity;
import vn.tonish.hozo.adapter.HozoSpinnerAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.PostTaskEntity;
import vn.tonish.hozo.database.manager.PostTaskManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AgeDialog;
import vn.tonish.hozo.dialog.AlertDialogOk;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.dialog.HozoNumberDialog;
import vn.tonish.hozo.fragment.BaseFragment;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.rest.responseRes.ImageResponse;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.FileUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.NumberTextWatcher;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.CheckBoxHozo;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.ExpandableLayout;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.utils.Utils.formatMoney;
import static vn.tonish.hozo.utils.Utils.hideSoftKeyboard;

/**
 * Created by CanTran on 12/6/17.
 */

public class PostTaskFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = PostTaskFragment.class.getSimpleName();
    private Calendar calendar = GregorianCalendar.getInstance();
    private TextViewHozo tvDate, tvWorkingHour, tvNumberWorker, tvTime, tvMoreShow, tvAge;
    private AutoCompleteTextView edtBudget;
    private EdittextHozo edtPromotion;
    private int ageFrom = 18;
    private int ageTo = 60;
    private static final int MAX_BUGDET = 1000000;
    private static final int MIN_BUGDET = 10000;
    private static final int MAX_WORKER = 30;
    private static final int MIN_WORKER = 1;
    private static final int MIN_HOUR = 1;
    private static final int MAX_HOUR = 12;
    private final ArrayList<String> vnds = new ArrayList<>();
    private ExpandableLayout advanceExpandableLayout;
    private Spinner spGender;
    private String strGender = "";
    private TaskResponse taskResponse = new TaskResponse();
    private ImageView imgMenu, imgSaveDraf;
    private PopupMenu popup;
    private ButtonHozo btnNext;
    private CheckBoxHozo cbAuto;
    private int imageAttachCount;
    private int[] imagesArr;


    @Override
    protected int getLayout() {
        return R.layout.fragment_post_task;
    }

    @Override
    protected void initView() {
        tvWorkingHour = (TextViewHozo) findViewById(R.id.tv_working_hour);
        tvNumberWorker = (TextViewHozo) findViewById(R.id.tv_number_worker);
        tvDate = (TextViewHozo) findViewById(R.id.tv_date);
        tvTime = (TextViewHozo) findViewById(R.id.tv_time);
        tvAge = (TextViewHozo) findViewById(R.id.tv_age);
        edtBudget = (AutoCompleteTextView) findViewById(R.id.edt_budget);
        edtPromotion = (EdittextHozo) findViewById(R.id.edt_promotion);
        tvMoreShow = (TextViewHozo) findViewById(R.id.tv_more_show);
        advanceExpandableLayout = (ExpandableLayout) findViewById(R.id.advance_expandable_layout);
        TextViewHozo tvMoreHide = (TextViewHozo) findViewById(R.id.tv_more_hide);
        tvMoreHide.setOnClickListener(this);
        ImageView imgPrepay = (ImageView) findViewById(R.id.img_prepay);
        imgPrepay.setOnClickListener(this);
        ImageView imgClose = (ImageView) findViewById(R.id.img_close);
        imgClose.setOnClickListener(this);
        spGender = (Spinner) findViewById(R.id.sp_gender);
        btnNext = (ButtonHozo) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);
        imgMenu = (ImageView) findViewById(R.id.img_menu);
        imgMenu.setOnClickListener(this);
        imgSaveDraf = (ImageView) findViewById(R.id.tv_save);
        cbAuto = (CheckBoxHozo) findViewById(R.id.cb_auto_pick);
        imgSaveDraf.setOnClickListener(this);
        tvAge.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        tvWorkingHour.setOnClickListener(this);
        tvMoreShow.setOnClickListener(this);
        tvNumberWorker.setOnClickListener(this);

    }


    @Override
    protected void initData() {
        taskResponse = ((PostTaskActivity) getActivity()).getTaskResponse();
        LogUtils.d(TAG, "values taskResponse" + taskResponse.toString() + "check: " + isCheckExpandable());
        edtBudget.addTextChangedListener(new NumberTextWatcher(edtBudget));
        edtBudget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                vnds.clear();
                if (!edtBudget.getText().toString().equals("") && Long.valueOf(getLongAutoCompleteTextView(edtBudget)) <= MAX_BUGDET)
                    edtBudget.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    edtBudget.setThreshold(edtBudget.getText().length());
                    formatMoney(getContext(), vnds, Long.parseLong(edtBudget.getText().toString().trim().replace(",", "").replace(".", "")), edtBudget);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!edtBudget.getText().toString().equals(""))
                    if (Long.valueOf(getLongAutoCompleteTextView(edtBudget)) > MAX_BUGDET) {
                        edtBudget.setText(edtBudget.getText().toString().substring(0, edtBudget.length() - 1));
                        edtBudget.setError(getString(R.string.max_budget_error, Utils.formatNumber(MAX_BUGDET)));
                        edtBudget.setSelection(edtBudget.getText().toString().length());
                    }
            }
        });
        edtBudget.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                hideSoftKeyboard(getContext(), edtBudget);
            }

        });

        try {
            if (taskResponse.getStartTime() == null || DateTimeUtils.minutesBetween(Calendar.getInstance(), DateTimeUtils.toCalendar(taskResponse.getStartTime())) < 30)
                calendar.add(Calendar.MINUTE, 40);
            else {
                calendar = DateTimeUtils.toCalendar(taskResponse.getStartTime());
                tvWorkingHour.setText(String.valueOf(DateTimeUtils.hoursBetween(DateTimeUtils.toCalendar(taskResponse.getStartTime()), DateTimeUtils.toCalendar(taskResponse.getEndTime()))));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvDate.setText(DateTimeUtils.fromCalendarToDate(calendar));
        tvTime.setText(DateTimeUtils.fromCalendarToTime(calendar));
        if (taskResponse.getWorkerRate() > 0)
            edtBudget.setText(Utils.formatNumber(taskResponse.getWorkerRate()));
        if (taskResponse.getWorkerCount() > 0)
            tvNumberWorker.setText(String.valueOf(taskResponse.getWorkerCount()));
        if (taskResponse.getMinAge() != 0)
            ageFrom = taskResponse.getMinAge();
        if (taskResponse.getMaxAge() != 0)
            ageTo = taskResponse.getMaxAge();
        tvAge.setText(getString(R.string.post_a_task_age, ageFrom, ageTo));
        if (taskResponse.getGender() != null)
            strGender = taskResponse.getGender();
        cbAuto.setChecked(taskResponse.isAutoAssign());
        createGenderSpinner();
        if (strGender.equalsIgnoreCase(Constants.GENDER_MALE))
            spGender.setSelection(1);
        else if (strGender.equalsIgnoreCase(Constants.GENDER_FEMALE))
            spGender.setSelection(2);
        else spGender.setSelection(0);
        if (((PostTaskActivity) getActivity()).isExtraTask) {
            imgSaveDraf.setVisibility(View.GONE);
            imgMenu.setVisibility(View.VISIBLE);
            showPopup();
            if (((PostTaskActivity) getActivity()).isEdit) {
                String mtasktype = ((PostTaskActivity) getActivity()).getTaskType();
                switch (mtasktype) {
                    case Constants.TASK_EDIT:
                        imgSaveDraf.setVisibility(View.GONE);
                        imgMenu.setVisibility(View.GONE);
                        btnNext.setText(getString(R.string.btn_edit_task));
                        popup.getMenu().findItem(R.id.delete_task).setVisible(false);
                        popup.getMenu().findItem(R.id.save_task).setVisible(true);
                        break;
                    case Constants.TASK_COPY:
                        imgSaveDraf.setVisibility(View.VISIBLE);
                        imgMenu.setVisibility(View.GONE);
                        break;
                    case Constants.TASK_DRAFT:
                        imgSaveDraf.setVisibility(View.GONE);
                        imgMenu.setVisibility(View.VISIBLE);
                        popup.getMenu().findItem(R.id.delete_task).setVisible(true);
                        popup.getMenu().findItem(R.id.save_task).setVisible(true);
                        break;
                }

            }
        } else {
            imgSaveDraf.setVisibility(View.VISIBLE);
            imgMenu.setVisibility(View.GONE);
        }
        if (isCheckExpandable()) {
            tvMoreShow.setVisibility(View.GONE);
            advanceExpandableLayout.setExpanded(true);
        } else {
            tvMoreShow.setVisibility(View.VISIBLE);
            advanceExpandableLayout.setExpanded(false);
        }


    }


    @Override
    protected void resumeData() {

    }

    private void showPopup() {
        //Creating the instance of PopupMenu
        popup = new PopupMenu(getContext(), imgMenu);
        popup.getMenuInflater().inflate(R.menu.menu_create_task, popup.getMenu());
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.save_task:
                        taskResponse.setStatus(Constants.CREATE_TASK_STATUS_DRAFT);
                        doPostTask();
                        break;

                    case R.id.delete_task:
                        DialogUtils.showOkAndCancelDialog(
                                getContext(), getString(R.string.title_delete_task), getString(R.string.content_detete_task), getString(R.string.cancel_task_ok),
                                getString(R.string.cancel_task_cancel), new AlertDialogOkAndCancel.AlertDialogListener() {
                                    @Override
                                    public void onSubmit() {
                                        doDeleteTask();
                                    }

                                    @Override
                                    public void onCancel() {

                                    }
                                });
                        break;
                }
                return true;
            }
        });
    }


    private boolean isCheckExpandable() {
        return !((taskResponse.getMinAge() == 18 || taskResponse.getMinAge() == 0) && (taskResponse.getMaxAge() == 60 || taskResponse.getMaxAge() == 0) && !taskResponse.isAutoAssign() && (taskResponse.getGender() == null || taskResponse.getGender().isEmpty() || taskResponse.getGender().equalsIgnoreCase(Constants.GENDER_ANY)));
    }

    private void doDeleteTask() {
        ProgressDialogUtils.showProgressDialog(getContext());
        ApiClient.getApiService().deleteTask(UserManager.getUserToken(), taskResponse.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                LogUtils.d(TAG, "doDeleteTask , code : " + response.code());
                LogUtils.d(TAG, "doDeleteTask , body : " + response.body());
                if (response.code() == Constants.HTTP_CODE_NO_CONTENT) {
                    Utils.showLongToast(getContext(), getString(R.string.delete_task_success_msg), false, false);
                    Intent intent = new Intent();
                    intent.putExtra(Constants.EXTRA_TASK, taskResponse);
                    getActivity().setResult(Constants.RESULT_CODE_TASK_DELETE, intent);
                    getActivity().finish();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(getContext(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doDeleteTask();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(getContext());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.d(TAG, "errorBody" + error.toString());
                    Utils.showLongToast(getContext(), error.message(), false, true);
                }
                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                DialogUtils.showRetryDialog(getContext(), new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        doDeleteTask();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                ProgressDialogUtils.dismissProgressDialog();
            }
        });
    }

    private JSONObject validata() {
        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("category_id", taskResponse.getCategoryId());
            jsonRequest.put("title", taskResponse.getTitle());
            jsonRequest.put("description", taskResponse.getDescription());
            jsonRequest.put("start_time", DateTimeUtils.fromCalendarIso(calendar));
            jsonRequest.put("end_time", DateTimeUtils.fromCalendarIso(getEndTime()));
            jsonRequest.put("latitude", taskResponse.getLatitude());
            jsonRequest.put("longitude", taskResponse.getLongitude());
            String address = taskResponse.getAddress();
            if (!taskResponse.isOnline()) {
                String[] arrAddress = address.split(",");
                jsonRequest.put("city", arrAddress.length >= 2 ? arrAddress[arrAddress.length - 2].trim() : arrAddress[arrAddress.length - 1].trim());
                jsonRequest.put("district", arrAddress.length >= 3 ? arrAddress[arrAddress.length - 3].trim() : arrAddress[arrAddress.length - 1].trim());
                jsonRequest.put("address", address);
                PostTaskEntity postTaskEntity = new PostTaskEntity();
                postTaskEntity.setId(0);
                postTaskEntity.setLat(taskResponse.getLatitude());
                postTaskEntity.setLon(taskResponse.getLongitude());
                postTaskEntity.setAddress(address);
                PostTaskManager.insertPostTask(postTaskEntity);
            }
            if (!getLongAutoCompleteTextView(edtBudget).trim().equals(""))
                jsonRequest.put("worker_rate", Integer.valueOf(getLongAutoCompleteTextView(edtBudget)));
            jsonRequest.put("worker_count", Integer.valueOf(tvNumberWorker.getText().toString()));
            jsonRequest.put("status", taskResponse.getStatus());
            if (taskResponse.getAttachmentsId() != null && (taskResponse.getAttachmentsId().length > 0)) {
                JSONArray jsonArray = new JSONArray();
                for (int anImagesArr : (((PostTaskActivity) getActivity()).getTaskResponse().getAttachmentsId()))
                    jsonArray.put(anImagesArr);
                jsonRequest.put("attachments", jsonArray);
            }
            jsonRequest.put("min_age", ageFrom);
            jsonRequest.put("max_age", ageTo);
            jsonRequest.put("gender", strGender);
            jsonRequest.put("online", taskResponse.isOnline());
            jsonRequest.put("auto_assign", cbAuto.isChecked());
            if (validatepromotion())
                jsonRequest.put("promo_code", edtPromotion.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonRequest;


    }

    private boolean validatepromotion() {
        String strPromotion = edtPromotion.getText().toString().trim().replace(" ", "").replace(",", "").replace(".", "");
        return edtPromotion.length() == 6 && strPromotion.length() == 6;
    }

    private void doAttachFiles() {
        ProgressDialogUtils.showProgressDialog(getContext());
        imageAttachCount = ((PostTaskActivity) getActivity()).images.size();
        imagesArr = new int[((PostTaskActivity) getActivity()).images.size()];
        for (int i = 0; i < ((PostTaskActivity) getActivity()).images.size(); i++) {
            LogUtils.d(TAG, " attachAllFile image " + i + " : " + ((PostTaskActivity) getActivity()).images.get(i).getPath());
            File file = new File(((PostTaskActivity) getActivity()).images.get(i).getPath());
            attachFile(file, i);
        }
    }

    private void attachFile(final File file, final int position) {
        File fileUp = Utils.compressFile(file);
        final RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), fileUp);
        MultipartBody.Part itemPart = MultipartBody.Part.createFormData("image", fileUp.getName(), requestBody);

        ApiClient.getApiService().uploadImage(UserManager.getUserToken(), itemPart).enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                LogUtils.d(TAG, "uploadImage onResponse : " + response.body());
                if (response.code() == Constants.HTTP_CODE_CREATED) {
                    ImageResponse imageResponse = response.body();
                    imageAttachCount--;
                    if (imageResponse != null)
                        imagesArr[position] = imageResponse.getIdTemp();
                    ((PostTaskActivity) getActivity()).getTaskResponse().setAttachmentsId(imagesArr);
                    if (imageAttachCount == 0)
                        doPostTask();
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(getContext());
                }

            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                ProgressDialogUtils.dismissProgressDialog();
                LogUtils.e(TAG, "uploadImage onFailure : " + t.getMessage());
                imageAttachCount--;
                if (imageAttachCount == 0)
                    doPostTask();
            }
        });
    }


    private void doPostTask() {
        LogUtils.d(TAG, "createNewTask status : " + (taskResponse.getStatus()));
        ProgressDialogUtils.showProgressDialog(getContext());
        LogUtils.d(TAG, "createNewTask data request : " + validata().toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), validata().toString());
        LogUtils.d(TAG, "request body create task : " + body.toString());
        if (taskResponse.getId() != 0 && !((PostTaskActivity) getActivity()).getTaskType().equals(Constants.TASK_COPY)) {
            LogUtils.d(TAG, "createNewTask status task -------> : " + taskResponse.getId());
            ApiClient.getApiService().editTask(UserManager.getUserToken(), taskResponse.getId(), body).enqueue(new Callback<TaskResponse>() {
                @Override
                public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                    LogUtils.d(TAG, "createNewTask onResponse : " + response.body());
                    LogUtils.d(TAG, "createNewTask code : " + response.code());
                    APIError error = ErrorUtils.parseError(response);
                    if (response.code() == Constants.HTTP_CODE_CREATED) {
                        if (taskResponse.getStatus().equals(Constants.CREATE_TASK_STATUS_DRAFT))
                            Utils.showLongToast(getContext(), getString(R.string.draft_a_task_complete), false, false);
                        else
                            Utils.showLongToast(getContext(), getString(R.string.post_a_task_complete), false, false);
                        getActivity().setResult(Constants.POST_A_TASK_RESPONSE_CODE);
                        getActivity().finish();
                        FileUtils.deleteDirectory(new File(FileUtils.OUTPUT_DIR));
                    } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                        NetworkUtils.refreshToken(getContext(), new NetworkUtils.RefreshListener() {
                            @Override
                            public void onRefreshFinish() {
                                doPostTask();
                            }
                        });
                    } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                        Utils.blockUser(getContext());
                    } else {
                        if (error.status().equals(Constants.POST_TASK_DUPLICATE)) {
                            DialogUtils.showOkDialog(getContext(), getString(R.string.error), getString(R.string.duplicate_task_error), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                @Override
                                public void onSubmit() {

                                }
                            });
                        } else if (error.status().equals(Constants.INVALID_CATEGORY)) {
                            DialogUtils.showOkDialog(getContext(), getString(R.string.error), getString(R.string.invalid_category), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                @Override
                                public void onSubmit() {

                                }
                            });
                        } else if (error.status().equals(Constants.INVALID_TITLE)) {
                            DialogUtils.showOkDialog(getContext(), getString(R.string.error), getString(R.string.invalid_title), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                @Override
                                public void onSubmit() {

                                }
                            });
                        } else if (error.status().equals(Constants.INVALID_DESCRIPTION)) {
                            DialogUtils.showOkDialog(getContext(), getString(R.string.error), getString(R.string.invalid_description), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                @Override
                                public void onSubmit() {

                                }
                            });
                        } else if (error.status().equals(Constants.INVALID_TIME)) {
                            DialogUtils.showOkDialog(getContext(), getString(R.string.error), getString(R.string.invalid_time), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                @Override
                                public void onSubmit() {

                                }
                            });
                        } else if (error.status().equals(Constants.INVALID_ADRESS)) {
                            Utils.showLongToast(getContext(), getString(R.string.invalid_address), true, false);
                            DialogUtils.showOkDialog(getContext(), getString(R.string.error), getString(R.string.no_task), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                @Override
                                public void onSubmit() {

                                }
                            });
                        } else if (error.status().equals(Constants.INVALID_WORKER_RATE)) {
                            DialogUtils.showOkDialog(getContext(), getString(R.string.error), getString(R.string.invalid_worker_rate), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                @Override
                                public void onSubmit() {

                                }
                            });
                        } else if (error.status().equals(Constants.INVALID_WORKER_COUNT)) {
                            DialogUtils.showOkDialog(getContext(), getString(R.string.error), getString(R.string.invalid_worker_count), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                @Override
                                public void onSubmit() {

                                }
                            });
                        } else if (error.status().equals(Constants.INVALID_GENDER)) {
                            DialogUtils.showOkDialog(getContext(), getString(R.string.error), getString(R.string.invalid_gender), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                @Override
                                public void onSubmit() {

                                }
                            });
                        } else if (error.status().equals(Constants.INVALID_AGE)) {
                            DialogUtils.showOkDialog(getContext(), getString(R.string.error), getString(R.string.invalid_age_between), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                @Override
                                public void onSubmit() {

                                }
                            });
                        } else if (error.status().equals(Constants.INVALID_UPDATE_TASK_FAILED)) {
                            Utils.showLongToast(getContext(), getString(R.string.update_task_failed), true, false);
                            DialogUtils.showOkDialog(getContext(), getString(R.string.error), getString(R.string.no_task), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                @Override
                                public void onSubmit() {

                                }
                            });
                        } else if (error.status().equals(Constants.NO_PERMISSION)) {
                            DialogUtils.showOkDialog(getContext(), getString(R.string.error), getString(R.string.no_permission), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                @Override
                                public void onSubmit() {

                                }
                            });
                        } else if (error.status().equals(Constants.NO_TASK)) {
                            DialogUtils.showOkDialog(getContext(), getString(R.string.error), getString(R.string.no_task), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                @Override
                                public void onSubmit() {

                                }
                            });
                        } else if (error.status().equals(Constants.EMPTY_PARAMETERS)) {
                            DialogUtils.showOkDialog(getContext(), getString(R.string.error), getString(R.string.empty_parameters), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                @Override
                                public void onSubmit() {

                                }
                            });
                        } else if (error.status().equals(Constants.UPDATE_NOT_ALLOWED)) {
                            DialogUtils.showOkDialog(getContext(), getString(R.string.error), getString(R.string.update_not_allowed), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                @Override
                                public void onSubmit() {

                                }
                            });
                        } else {

                            DialogUtils.showOkDialog(getContext(), getString(R.string.invalid_error), error.message(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                @Override
                                public void onSubmit() {

                                }
                            });

                        }
                    }
                    ProgressDialogUtils.dismissProgressDialog();
                }

                @Override
                public void onFailure(Call<TaskResponse> call, Throwable t) {
                    LogUtils.e(TAG, "createNewTask onFailure : " + t.getMessage());
                    DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            doPostTask();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                    ProgressDialogUtils.dismissProgressDialog();
                }
            });
        } else {
            ApiClient.getApiService().createNewTask(UserManager.getUserToken(), body).enqueue(new Callback<TaskResponse>() {
                @Override
                public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                    LogUtils.d(TAG, "createNewTask onResponse : " + response.body());
                    LogUtils.d(TAG, "createNewTask code : " + response.code());
                    LogUtils.d(TAG, "createNewTask task type : " + ((PostTaskActivity) getActivity()).getTaskType());
                    APIError error = ErrorUtils.parseError(response);
                    if (response.code() == Constants.HTTP_CODE_CREATED) {
                        if (taskResponse.getStatus().equals(Constants.CREATE_TASK_STATUS_DRAFT))
                            Utils.showLongToast(getContext(), getString(R.string.draft_a_task_complete), false, false);
                        else
                            Utils.showLongToast(getContext(), getString(R.string.post_a_task_complete), false, false);

                        getActivity().setResult(Constants.POST_A_TASK_RESPONSE_CODE);
                        getActivity().finish();
                        FileUtils.deleteDirectory(new File(FileUtils.OUTPUT_DIR));
                    } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                        NetworkUtils.refreshToken(getContext(), new NetworkUtils.RefreshListener() {
                            @Override
                            public void onRefreshFinish() {
                                doPostTask();
                            }
                        });
                    } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                        Utils.blockUser(getContext());
                    } else {
                        if (error.status().equals(Constants.POST_TASK_DUPLICATE)) {
                            DialogUtils.showOkDialog(getContext(), getString(R.string.error), getString(R.string.duplicate_task_error), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                @Override
                                public void onSubmit() {

                                }
                            });
                        } else if (error.status().equals(Constants.INVALID_TITLE)) {

                            DialogUtils.showOkDialog(getContext(), getString(R.string.error), getString(R.string.invalid_title), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                @Override
                                public void onSubmit() {

                                }
                            });
                        } else if (error.status().equals(Constants.INVALID_TIME)) {
                            DialogUtils.showOkDialog(getContext(), getString(R.string.error), getString(R.string.invalid_time), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                @Override
                                public void onSubmit() {

                                }
                            });
                        } else if (error.status().equals(Constants.PROMOTION_ERROR_NO)) {
                            edtPromotion.setError(getActivity().getString(R.string.promotion_error_no));
                            DialogUtils.showOkDialog(getContext(), getString(R.string.error), getString(R.string.promotion_error_no), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                @Override
                                public void onSubmit() {

                                }
                            });
                        } else if (error.status().equals(Constants.PROMOTION_ERROR_USED)) {
                            edtPromotion.setError(getActivity().getString(R.string.promotion_error_used));
                            DialogUtils.showOkDialog(getContext(), getString(R.string.error), getString(R.string.promotion_error_used), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                @Override
                                public void onSubmit() {

                                }
                            });
                        } else if (error.status().equals(Constants.PROMOTION_ERROR_EXPRIED)) {
                            edtPromotion.setError(getActivity().getString(R.string.promotion_error_expried));
                            DialogUtils.showOkDialog(getContext(), getString(R.string.error), getString(R.string.promotion_error_expried), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                @Override
                                public void onSubmit() {

                                }
                            });

                        } else if (error.status().equals(Constants.PROMOTION_ERROR_LIMITED)) {
                            edtPromotion.setError(getActivity().getString(R.string.promotion_error_limmited));
                            DialogUtils.showOkDialog(getContext(), getString(R.string.error), getString(R.string.promotion_error_limmited), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                @Override
                                public void onSubmit() {

                                }
                            });

                        } else {
                            DialogUtils.showOkDialog(getContext(), getString(R.string.invalid_error), error.message(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                @Override
                                public void onSubmit() {

                                }
                            });

                        }
                    }
                    ProgressDialogUtils.dismissProgressDialog();
                }

                @Override
                public void onFailure(Call<TaskResponse> call, Throwable t) {
                    LogUtils.e(TAG, "createNewTask onFailure : " + t.getMessage());
                    DialogUtils.showRetryDialog(getContext(), new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            doPostTask();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                    ProgressDialogUtils.dismissProgressDialog();
                }
            });
        }
    }

    private void doNext() {
        if (tvDate.getText().toString().equals("")) {
            tvDate.requestFocus();
            tvDate.setError(getString(R.string.post_a_task_date_error));
            Utils.showLongToast(getContext(), getString(R.string.post_a_task_date_error), true, false);
        } else if (DateTimeUtils.minutesBetween(Calendar.getInstance(), calendar) < 30) {
            tvTime.requestFocus();
            tvTime.setError(getString(R.string.post_task_time_start_error));
            Utils.showLongToast(getContext(), getString(R.string.post_task_time_start_error), true, false);
        } else if (edtBudget.getText().toString().equals("0") || edtBudget.getText().toString().equals("")) {
            edtBudget.requestFocus();
            edtBudget.setError(getString(R.string.post_a_task_budget_error));
        } else if (Long.valueOf(getLongAutoCompleteTextView(edtBudget)) > MAX_BUGDET) {
            edtBudget.requestFocus();
            edtBudget.setError(getString(R.string.max_budget_error, Utils.formatNumber(MAX_BUGDET)));
        } else if (Long.valueOf(getLongAutoCompleteTextView(edtBudget)) < MIN_BUGDET) {
            edtBudget.requestFocus();
            edtBudget.setError(getString(R.string.min_budget_error, Utils.formatNumber(MIN_BUGDET)));
        } else if (ageFrom >= ageTo) {
            Utils.showLongToast(getContext(), getString(R.string.select_age_error), true, false);
        } else if (edtPromotion.length() > 0 && !validatepromotion()) {
            edtPromotion.setError(getActivity().getString(R.string.promotion_error_no));
        } else {
            if (((PostTaskActivity) getActivity()).images.size() > 0) doAttachFiles();
            else doPostTask();
        }

    }


    private Calendar getEndTime() {
        Calendar result = Calendar.getInstance();
        result.setTime(calendar.getTime());
        result.add(Calendar.HOUR_OF_DAY, Integer.valueOf(tvWorkingHour.getText().toString()));
        return result;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                inserTask(((PostTaskActivity) getActivity()).getTaskResponse());
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.tv_date:
                openDatePicker();
                break;
            case R.id.tv_time:
                openTimeLayout();
                break;
            case R.id.tv_age:
                AgeDialog ageDialog = new AgeDialog(getContext());
                ageDialog.setAgeFrom(ageFrom);
                ageDialog.setAgeTo(ageTo);
                ageDialog.setAgeDialogListener(new AgeDialog.AgeDialogListener() {
                    @Override
                    public void onAgeDialogLister(int from, int to) {
                        tvAge.setText(getString(R.string.post_a_task_age, from, to));
                        ageFrom = from;
                        ageTo = to;
                        tvAge.setError(null);
                    }
                });
                ageDialog.showView();
                break;
            case R.id.tv_more_hide:
                advanceExpandableLayout.toggle();
                tvMoreShow.setVisibility(View.VISIBLE);

                break;
            case R.id.tv_more_show:
                tvMoreShow.setVisibility(View.GONE);
                advanceExpandableLayout.toggle();
                break;
            case R.id.tv_working_hour:
                HozoNumberDialog hozoHourDialog = new HozoNumberDialog(getContext(), getString(R.string.hour_title), MIN_HOUR, MAX_HOUR);
                hozoHourDialog.setValue(Integer.valueOf(tvWorkingHour.getText().toString()));
                hozoHourDialog.setHourDialogListener(new HozoNumberDialog.NumberDialogListener() {
                    @Override
                    public void onNumberDialogLister(int number) {
                        tvWorkingHour.setText(String.valueOf(number));
                    }
//
                });
                hozoHourDialog.showView();
                break;

            case R.id.tv_number_worker:
                HozoNumberDialog hozoNumberDialog = new HozoNumberDialog(getContext(), getString(R.string.number_title), MIN_WORKER, MAX_WORKER);
                hozoNumberDialog.setValue(Integer.valueOf(tvNumberWorker.getText().toString()));
                hozoNumberDialog.setHourDialogListener(new HozoNumberDialog.NumberDialogListener() {
                    @Override
                    public void onNumberDialogLister(int number) {
                        tvNumberWorker.setText(String.valueOf(number));
                    }
//
                });
                hozoNumberDialog.showView();
                break;
            case R.id.btn_next:
                taskResponse.setStatus(Constants.CREATE_TASK_STATUS_PUBLISH);
                doNext();
                break;
            case R.id.img_menu:
                popup.show();//showing popup menu
                break;
            case R.id.tv_save:
                taskResponse.setStatus(Constants.CREATE_TASK_STATUS_DRAFT);
                if (((PostTaskActivity) getActivity()).images.size() > 1) doAttachFiles();
                else doPostTask();
                break;
            case R.id.img_prepay:
                Intent intent = new Intent(getActivity(), PrePayInfoActivity.class);
                startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
                break;

        }
    }

    private void createGenderSpinner() {
        List<String> state = new ArrayList<>();
        state.add(getString(R.string.gender_vn_any));
        state.add(getString(R.string.gender_vn_male));
        state.add(getString(R.string.gender_vn_mafele));
        HozoSpinnerAdapter dataAdapter = new HozoSpinnerAdapter(getContext(), state);
        spGender.setAdapter(dataAdapter);
        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        strGender = "";
                        break;
                    case 1:
                        strGender = Constants.GENDER_MALE;
                        break;
                    case 2:
                        strGender = Constants.GENDER_FEMALE;
                        break;
                    default:
                        strGender = "";
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private String getLongAutoCompleteTextView(AutoCompleteTextView autoCompleteTextView) {
        return autoCompleteTextView.getText().toString().replace(",", "").replace(".", "").replace(" ₫", "");
    }

    private void openDatePicker() {
        @SuppressWarnings("deprecation") DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year,
                                          final int monthOfYear, final int dayOfMonth) {
                        if (view.isShown()) {
                            calendar.set(year, monthOfYear, dayOfMonth);
                            String strDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            tvDate.setText(strDate);
                            tvDate.setError(null);
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        Calendar calendarMax = Calendar.getInstance();
        // Set calendar to 1 day next from today
        calendarMax.add(Calendar.DAY_OF_MONTH, 1);
        // Set calendar to 1 month next
        calendarMax.add(Calendar.MONTH, 1);
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime() - 10000);
        datePickerDialog.getDatePicker().setMaxDate(calendarMax.getTimeInMillis());
        datePickerDialog.setTitle(getString(R.string.post_task_date_picker_title));
        datePickerDialog.show();
    }

    private void openTimeLayout() {
        //noinspection deprecation
        TimePickerDialog timeEndPickerDialog = new TimePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        if (view.isShown()) {
                            Calendar c2 = Calendar.getInstance();
                            if (calendar.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                                    && calendar.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                                    && calendar.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)
                                    && (hourOfDay < c2.get(Calendar.HOUR_OF_DAY) || (hourOfDay == c2.get(Calendar.HOUR_OF_DAY) && minute <= (c2.get(Calendar.MINUTE) + 30)))) {
                                Utils.showLongToast(getContext(), getString(R.string.post_task_time_start_error), true, false);
                            } else {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                tvTime.setText(DateTimeUtils.fromCalendarToTime(calendar));
                                tvTime.setError(null);
                            }
                        }
                    }
                }, calendar.get((Calendar.HOUR_OF_DAY)), calendar.get(Calendar.MINUTE), true);
        timeEndPickerDialog.setTitle(getString(R.string.post_task_time_picker_title));
        timeEndPickerDialog.show();
    }

    private void inserTask(TaskResponse response) {
        response.setStartTime(DateTimeUtils.fromCalendarIso(calendar));
        response.setEndTime(DateTimeUtils.fromCalendarIso(getEndTime()));
        if (!edtBudget.getText().toString().trim().isEmpty() && Integer.valueOf(getLongAutoCompleteTextView(edtBudget)) > 0)
            response.setWorkerRate(Integer.valueOf(getLongAutoCompleteTextView(edtBudget)));
        response.setWorkerCount(Integer.valueOf(tvNumberWorker.getText().toString()));
        response.setMinAge(ageFrom);
        response.setMaxAge(ageTo);
        response.setGender(strGender);
        response.setAutoAssign(cbAuto.isChecked());
    }
}
