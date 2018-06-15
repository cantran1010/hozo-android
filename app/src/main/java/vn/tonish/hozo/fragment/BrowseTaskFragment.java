package vn.tonish.hozo.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.SearchEvent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.BuildConfig;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BrowserTaskMapActivity;
import vn.tonish.hozo.activity.MainActivity;
import vn.tonish.hozo.activity.SettingActivity;
import vn.tonish.hozo.activity.task_detail.DetailTaskActivity;
import vn.tonish.hozo.adapter.TaskAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.SettingAdvanceEntity;
import vn.tonish.hozo.database.manager.SettingAdvanceManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.dialog.ConfirmGpsDialog;
import vn.tonish.hozo.fragment.mytask.MyTaskFragment;
import vn.tonish.hozo.model.MiniTask;
import vn.tonish.hozo.model.SettingAdvance;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.EndlessRecyclerViewScrollListener;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.PreferUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.SpeedyLinearLayoutManager;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.database.manager.SettingAdvanceManager.converToSettingAdvanceEntity;
import static vn.tonish.hozo.utils.Utils.hideKeyBoard;
import static vn.tonish.hozo.utils.Utils.showSearch;

/**
 * Created by CanTran on 4/11/17.
 */

public class BrowseTaskFragment extends BaseFragment implements View.OnClickListener {
    private final static String TAG = BrowseTaskFragment.class.getSimpleName();
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 100000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private final static int limit = 20;
    private ImageView imgSearch, imgLocation, imgBack, imgClear;
    private RelativeLayout layoutHeader, layoutSearch;
    private EdittextHozo edtSearch;
    private RecyclerView rcvTask;
    private TaskAdapter taskAdapter;
    private ImageView imgFilter;
    private final List<TaskResponse> taskList = new ArrayList<>();
    private String query = null;
    private boolean isLoadingMoreFromServer = true;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private Call<List<TaskResponse>> call;
    private TextViewHozo tvCountNewTask;
    private int currentPage = 1;
    private String orderBy = "";
    private String order = Constants.ORDER_DESC;
    private GoogleApiClient mGoogleApiClient;
    private Location mylocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private LocationSettingsRequest mLocationSettingsRequest;
    private boolean isUpdateGps = true;


    @Override
    protected int getLayout() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView() {
        imgSearch = (ImageView) findViewById(R.id.img_search);
        imgLocation = (ImageView) findViewById(R.id.img_location);
        imgFilter = (ImageView) findViewById(R.id.img_filter);
        imgClear = (ImageView) findViewById(R.id.img_clear);
        imgBack = (ImageView) findViewById(R.id.img_back);
        layoutHeader = (RelativeLayout) findViewById(R.id.browse_task_header);
        edtSearch = (EdittextHozo) findViewById(R.id.edt_search);
        layoutSearch = (RelativeLayout) findViewById(R.id.fr_search);
        rcvTask = (RecyclerView) findViewById(R.id.lv_list);
        rcvTask.setHasFixedSize(true);
        tvCountNewTask = (TextViewHozo) findViewById(R.id.tvCountNewTask);
        createSwipeToRefresh();
    }

    @Override
    protected void initData() {
        imgFilter.setOnClickListener(this);
        imgLocation.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        imgClear.setOnClickListener(this);
        tvCountNewTask.setOnClickListener(this);
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                search();
                hideKeyBoard(getActivity());
                return actionId == EditorInfo.IME_ACTION_SEARCH;
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtSearch.getText().toString().length() > 0) {
                    imgClear.setVisibility(View.VISIBLE);
                } else {
                    imgClear.setVisibility(View.GONE);
                }
                search();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mSettingsClient = LocationServices.getSettingsClient(getActivity());
        createLocationRequest();
        createLocationCallback();
        buildLocationSettingsRequest();
        if (PreferUtils.isUpDateGps(getActivity())) showDialogGps();
        else {
            if (PreferUtils.isAutoGps(getActivity()))
                checkPermissions();
        }
        setUpRecyclerView();
    }

    private void showDialogGps() {
        ConfirmGpsDialog confirmGpsDialog = new ConfirmGpsDialog(getActivity());
        confirmGpsDialog.setConfirmGpsListener(new ConfirmGpsDialog.ConfirmGpsListener() {
            @Override
            public void onConfirm() {
                checkPermissions();
            }
        });
        confirmGpsDialog.showView();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void getGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(@Nullable Bundle bundle) {
                            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                                @SuppressLint("MissingPermission")
                                @Override
                                public void onSuccess(Location location) {
                                    mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                            mLocationCallback, Looper.myLooper());

                                }
                            });

                            mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                                    .addOnFailureListener(getActivity(), new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            int statusCode = ((ApiException) e).getStatusCode();
                                            if (statusCode
                                                    == LocationSettingsStatusCodes
                                                    .RESOLUTION_REQUIRED) {
                                                // Location settings are not satisfied, but this can
                                                // be fixed by showing the user a dialog
                                                try {
                                                    // Show the dialog by calling
                                                    // startResolutionForResult(), and check the
                                                    // result in onActivityResult()
                                                    ResolvableApiException resolvable =
                                                            (ResolvableApiException) e;
                                                    resolvable.startResolutionForResult
                                                            (getActivity(), Constants.REQUEST_CHECK_SETTINGS_GPS);
                                                } catch (IntentSender.SendIntentException sendEx) {
                                                    // Ignore the error
                                                }
                                            }
                                        }
                                    });
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            Log.e(getClass().getName(), "onConnectionSuspended() ");
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            Log.e(getClass().getName(), "Get location failure : " + connectionResult.getErrorMessage());
                        }
                    })
                    .build();
        }
        mGoogleApiClient.connect();
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null && locationResult.getLastLocation() != null) {
                    mylocation = locationResult.getLastLocation();
                    if (isUpdateGps) {
                        postSettingAdvance();
                        isUpdateGps = false;
                    }
                }
            }
        };
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void checkPermissions() {
        int permissionLocation = ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                LogUtils.d(TAG, "checkPermissions" + "!isEmpty");
                requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), Constants.REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        } else {
            getGoogleApiClient();
        }

    }

    private void setUpRecyclerView() {
        taskAdapter = new TaskAdapter(getActivity(), taskList);
        SpeedyLinearLayoutManager linearLayoutManager = new SpeedyLinearLayoutManager(getActivity());
        rcvTask.setLayoutManager(linearLayoutManager);
        rcvTask.setAdapter(taskAdapter);
        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (isLoadingMoreFromServer) getTaskResponse(query);
                else {
                    if (taskList.size() >= 15) {
                        taskList.addAll(taskList);
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                taskAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            }
        };

        rcvTask.addOnScrollListener(endlessRecyclerViewScrollListener);

        taskAdapter.setTaskAdapterListener(new TaskAdapter.TaskAdapterListener() {
            @Override
            public void onTaskAdapterClickListener(int position) {
                LogUtils.d(TAG, "onclick");
                TaskResponse taskResponse = taskList.get(position);
                Intent intent = new Intent(getActivity(), DetailTaskActivity.class);
                intent.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
                startActivityForResult(intent, Constants.REQUEST_CODE_TASK_EDIT, TransitionScreen.RIGHT_TO_LEFT);
            }
        });
    }

    @Override
    protected void resumeData() {
        updateCountNewTask(MainActivity.countNewTask);
        PreferUtils.setLastTimeCountTask(getActivity(), DateTimeUtils.fromCalendarIso(Calendar.getInstance()));
        getActivity().registerReceiver(broadcastReceiverSmoothToTop, new IntentFilter(Constants.BROAD_CAST_SMOOTH_TOP_SEARCH));
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            getActivity().unregisterReceiver(broadcastReceiverSmoothToTop);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                mFusedLocationClient.removeLocationUpdates(new LocationCallback());
                mGoogleApiClient.disconnect();
            }
        }
    }

    private void getTaskResponse(final String query) {
        SettingAdvanceEntity settingAdvanceEntity = SettingAdvanceManager.getSettingAdvace();
        if (settingAdvanceEntity != null) {
            orderBy = settingAdvanceEntity.getOrderBy();
            order = settingAdvanceEntity.getOrder();
        }
        final Map<String, String> option = new HashMap<>();
        option.put("limit", String.valueOf(limit));
        option.put("page", String.valueOf(currentPage));
        if (orderBy != null) option.put("order_by", orderBy);
        if (order != null) option.put("order", order);
        if (query != null) option.put("query", query);
        LogUtils.d(TAG, "getTaskResponse String : " + option.toString());
        call = ApiClient.getApiService().getTasks(UserManager.getUserToken(), option);
        call.enqueue(new Callback<List<TaskResponse>>() {
            @Override
            public void onResponse(Call<List<TaskResponse>> call, Response<List<TaskResponse>> response) {
                LogUtils.d(TAG, "getTaskResponse code : " + response.code() + " , search key word : " + option.toString());
                LogUtils.d(TAG, "getTaskResponse body : " + response.body());
                switch (response.code()) {
                    case Constants.HTTP_CODE_OK:
                        List<TaskResponse> taskResponses = response.body();
                        LogUtils.d(TAG, "getTaskFromServer taskResponses size : " + (taskResponses != null ? taskResponses.size() : 0));
                        if (currentPage == 1) {
                            taskList.clear();
                            endlessRecyclerViewScrollListener.resetState();
                        }
                        currentPage++;
                        taskList.addAll(taskResponses);
                        taskAdapter.notifyDataSetChanged();
                        LogUtils.d(TAG, "getTaskResponse size : " + taskList.size());
//                    TaskManager.insertTasks(DataParse.convertListTaskResponseToTaskEntity(taskResponses));

                        if (taskResponses.size() < limit) {
                            isLoadingMoreFromServer = false;
                            taskAdapter.stopLoadMore();
                        }

                        break;
                    case Constants.HTTP_CODE_UNAUTHORIZED:
                        NetworkUtils.refreshToken(getActivity(), new NetworkUtils.RefreshListener() {
                            @Override
                            public void onRefreshFinish() {
                                getTaskResponse(query);
                            }
                        });
                        break;
                    case Constants.HTTP_CODE_BLOCK_USER:
                        Utils.blockUser(getActivity());
                        break;
                    default:
                        APIError error = ErrorUtils.parseError(response);
                        LogUtils.d(TAG, "errorBody" + error.toString());
                        Utils.showLongToast(getActivity(), error.message(), true, false);
                        break;
                }


                onStopRefresh();
                if (!(taskList.size() > 0)) {
                    findViewById(R.id.noitem).setVisibility(View.VISIBLE);
                    findViewById(R.id.swpRefresh).setVisibility(View.GONE);
                } else {
                    findViewById(R.id.noitem).setVisibility(View.GONE);
                    findViewById(R.id.swpRefresh).setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<List<TaskResponse>> call, Throwable t) {
                onStopRefresh();
                LogUtils.e(TAG, "getTaskResponse , onFailure : " + t.getMessage());

                if (!t.getMessage().equals("Canceled")) {
                    DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            taskAdapter.onLoadMore();
                            getTaskResponse(query);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });

                    taskAdapter.stopLoadMore();
                    onStopRefresh();
                    taskAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_location:
                goToMapScren();
                break;
            case R.id.img_search:
                edtSearch.requestFocus();
                Utils.showSoftKeyboard(getActivity(), edtSearch);
                showSearch(getActivity(), layoutSearch, true);
                showSearch(getActivity(), layoutHeader, false);

                break;
            case R.id.img_back:
                query = null;
                showSearch(getActivity(), layoutHeader, true);
                showSearch(getActivity(), layoutSearch, false);
                endlessRecyclerViewScrollListener.resetState();
                if (!edtSearch.getText().toString().isEmpty()) {
                    onRefresh();
                }

                if (!BuildConfig.DEBUG)
                    Answers.getInstance().logSearch(new SearchEvent()
                            .putQuery(edtSearch.getText().toString()));

                edtSearch.setText("");
                break;
            case R.id.img_clear:

                if (!BuildConfig.DEBUG)
                    Answers.getInstance().logSearch(new SearchEvent()
                            .putQuery(edtSearch.getText().toString()));

                edtSearch.setText("");
                break;
            case R.id.img_filter:
                startActivityForResult(new Intent(getActivity(), SettingActivity.class), Constants.REQUEST_CODE_SETTING, TransitionScreen.RIGHT_TO_LEFT);
                break;
            case R.id.tvCountNewTask:
                onRefresh();
                break;
        }

    }

    private JSONObject getJsonRequest() {
        JSONObject jsonRequest = new JSONObject();
        try {
            JSONArray jsonArray = new JSONArray();
            ArrayList<Double> locations = new ArrayList<>();
            locations.add(0, mylocation.getLatitude());
            locations.add(1, mylocation.getLongitude());
            if (locations.size() > 0) {
                for (int i = 0; i < locations.size(); i++)
                    jsonArray.put(locations.get(i));
            }
            jsonRequest.put("filter_original_location", jsonArray);
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getActivity(), Locale.getDefault());
            addresses = geocoder.getFromLocation(mylocation.getLatitude(), mylocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0);
            jsonRequest.put("filter_original_address", address);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonRequest;
    }

    private void postSettingAdvance() {
        ProgressDialogUtils.showProgressDialog(getActivity());
        final JSONObject jsonRequest = getJsonRequest();
        LogUtils.d(TAG, "postSettingAdvance activity json: " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
        ApiClient.getApiService().postSettingAdvance(UserManager.getUserToken(), body).enqueue(new Callback<SettingAdvance>() {

            @Override
            public void onResponse(Call<SettingAdvance> call, Response<SettingAdvance> response) {
                ProgressDialogUtils.dismissProgressDialog();
                switch (response.code()) {
                    case Constants.HTTP_CODE_OK:
                        SettingAdvanceManager.insertSettingAdvanceEntity(converToSettingAdvanceEntity(response.body()));
                        rcvTask.smoothScrollToPosition(0);
                        onRefresh();
                        break;
                    case Constants.HTTP_CODE_UNAUTHORIZED:
                        NetworkUtils.refreshToken(getActivity(), new NetworkUtils.RefreshListener() {
                            @Override
                            public void onRefreshFinish() {
                                postSettingAdvance();
                            }
                        });
                        break;
                    case Constants.HTTP_CODE_BLOCK_USER:
                        Utils.blockUser(getActivity());
                        break;
                    default:
                        DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                            @Override
                            public void onSubmit() {
                                postSettingAdvance();
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                        break;
                }
            }

            @Override
            public void onFailure(Call<SettingAdvance> call, Throwable t) {
                ProgressDialogUtils.dismissProgressDialog();
                LogUtils.e(TAG, "postSettingAdvance onFailure status code : " + t.getMessage());
                DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        postSettingAdvance();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
    }

    private void search() {
        if (call != null) call.cancel();
        currentPage = 1;
        query = edtSearch.getText().toString();
        getTaskResponse(query);
    }

    private void goToMapScren() {
        ArrayList<MiniTask> miniTasks = new ArrayList<>();

        for (int i = 0; i < taskList.size(); i++) {

            if (!taskList.get(i).isOnline()) {
                MiniTask miniTask = new MiniTask();
                miniTask.setId(taskList.get(i).getId());
                miniTask.setTitle(taskList.get(i).getTitle());
                miniTask.setAddress(taskList.get(i).getAddress());
                miniTask.setLat(taskList.get(i).getLatitude());
                miniTask.setLon(taskList.get(i).getLongitude());
                miniTasks.add(miniTask);
            }

        }

        if (miniTasks.size() > 0) {
            Intent intent = new Intent(getActivity(), BrowserTaskMapActivity.class);
            intent.putParcelableArrayListExtra(Constants.LIST_TASK_EXTRA, miniTasks);
            startActivityForResult(intent, Constants.POST_A_TASK_REQUEST_CODE, TransitionScreen.RIGHT_TO_LEFT);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        int permissionLocation = ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getGoogleApiClient();
        } else
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        LogUtils.d(TAG, "location" + "check not");
//                        displayPromptForEnablingGPS(getActivity());
                        // user also CHECKED "never ask again"
                        // you can either enable some fall back,
                        // disable features of your app
                        // or open another dialog explaining
                        // again the permission and directing to
                        // the app setting
                    } else if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permission)) {
                        LogUtils.d(TAG, "location" + "no check");
//                    showRationale(permission, R.string.permission_denied_contacts);
                        // user did NOT check "never ask again"
                        // this is a good place to explain the user
                        // why you need the permission and ask if he wants
                        // to accept it (the rationale)
                    }
                }
            }

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.d(TAG, "requestCode fragment" + requestCode);
        LogUtils.d(TAG, "resultCode fragment" + resultCode);
        if (requestCode == Constants.REQUEST_CODE_SETTING && resultCode == Constants.RESULT_CODE_SETTING) {
            onRefresh();
        } else if (requestCode == Constants.REQUEST_CODE_TASK_EDIT && resultCode == Constants.RESULT_CODE_TASK_EDIT) {
            TaskResponse taskEdit = (TaskResponse) data.getSerializableExtra(Constants.EXTRA_TASK);
            if (taskEdit.getOfferStatus() != null && !taskEdit.getOfferStatus().equals("")) {
                for (int i = 0; i < taskList.size(); i++) {
                    if (taskList.get(i).getId() == taskEdit.getId()) {
                        taskList.remove(i);
                        taskAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        } else if (requestCode == Constants.REQUEST_CODE_TASK_EDIT && resultCode == Constants.POST_A_TASK_RESPONSE_CODE) {
            openFragment(R.id.layout_container, MyTaskFragment.class, new Bundle(), false, TransitionScreen.RIGHT_TO_LEFT);
            updateMenuUi(3);
        } else if (requestCode == Constants.POST_A_TASK_REQUEST_CODE && resultCode == Constants.POST_A_TASK_RESPONSE_CODE) {
            openFragment(R.id.layout_container, MyTaskFragment.class, new Bundle(), false, TransitionScreen.RIGHT_TO_LEFT);
            updateMenuUi(3);
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (call != null) call.cancel();
        isLoadingMoreFromServer = true;
        currentPage = 1;
        taskAdapter.onLoadMore();
        getTaskResponse(query);
        updateCountNewTask(0);
        PreferUtils.setLastTimeCountTask(getActivity(), DateTimeUtils.fromCalendarIso(Calendar.getInstance()));
        if (getActivity() instanceof MainActivity)
            ((MainActivity) getActivity()).updateNewTask(0);
        MainActivity.countNewTask = 0;
    }

    private final BroadcastReceiver broadcastReceiverSmoothToTop = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.d(TAG, "BroadcastReceiver start" + intent.getBooleanExtra(Constants.BROAD_CAST_RESULT_GPS, false));
            if (intent.hasExtra(Constants.COUNT_NEW_TASK_EXTRA)) {
                int count = intent.getIntExtra(Constants.COUNT_NEW_TASK_EXTRA, 0);
                updateCountNewTask(count);
            }
        }
    };

    private void updateCountNewTask(int count) {

        if (count > 99) count = 99;
        if (count > 0) {
            tvCountNewTask.setText(getString(R.string.brower_new_task, count));
            tvCountNewTask.setVisibility(View.VISIBLE);
        } else
            tvCountNewTask.setVisibility(View.GONE);
    }

}

