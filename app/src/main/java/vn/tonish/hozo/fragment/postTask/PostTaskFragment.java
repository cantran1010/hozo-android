package vn.tonish.hozo.fragment.postTask;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.CustomArrayAdapter;
import vn.tonish.hozo.adapter.HozoSpinnerAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.dialog.AgeDialog;
import vn.tonish.hozo.dialog.HozoNumberDialog;
import vn.tonish.hozo.fragment.BaseFragment;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ExpandableLayout;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.utils.Utils.formatNumber;
import static vn.tonish.hozo.utils.Utils.hideSoftKeyboard;

/**
 * Created by CanTran on 12/6/17.
 */

public class PostTaskFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = PostTaskFragment.class.getSimpleName();
    private Calendar calendar = GregorianCalendar.getInstance();
    private TextViewHozo tvDate, tvWorkingHour, tvNumberWorker, tvTime, tvMoreShow, tvAge;
    private AutoCompleteTextView edtBudget;
    private int ageFrom = 18;
    private int ageTo = 60;
    private static final int MAX_BUGDET = 500000;
    private static final int MIN_BUGDET = 10000;
    private static final int MAX_WORKER = 30;
    private static final int MIN_WORKER = 1;
    private static final int MIN_HOUR = 1;
    private static final int MAX_HOUR = 12;
    private final ArrayList<String> vnds = new ArrayList<>();
    private CustomArrayAdapter adapter;
    private ExpandableLayout advanceExpandableLayout;
    private Spinner spGender;
    private String strGender = "";


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
        tvMoreShow = (TextViewHozo) findViewById(R.id.tv_more_show);
        advanceExpandableLayout = (ExpandableLayout) findViewById(R.id.advance_expandable_layout);
        TextViewHozo tvMoreHide = (TextViewHozo) findViewById(R.id.tv_more_hide);
        tvMoreHide.setOnClickListener(this);
        spGender = (Spinner) findViewById(R.id.sp_gender);

        tvAge.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        tvWorkingHour.setOnClickListener(this);
        tvMoreShow.setOnClickListener(this);
        tvNumberWorker.setOnClickListener(this);

    }


    @Override
    protected void initData() {
        edtBudget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                adapter = null;
                vnds.clear();
                if (!edtBudget.getText().toString().equals("") && Long.valueOf(getLongAutoCompleteTextView(edtBudget)) <= MAX_BUGDET)
                    edtBudget.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    edtBudget.setThreshold(edtBudget.getText().length());
                    formatMoney(Long.parseLong(edtBudget.getText().toString().trim().replace(",", "").replace(".", "")));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!edtBudget.getText().toString().equals(""))
                    if (Long.valueOf(getLongAutoCompleteTextView(edtBudget)) > MAX_BUGDET) {
                        edtBudget.setText(edtBudget.getText().toString().substring(0, edtBudget.length() - 1));
                        edtBudget.setError(getString(R.string.max_budget_error, formatNumber(MAX_BUGDET)));
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

        createGenderSpinner();
    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

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

    private void formatMoney(long mn) {
        if (mn * 10 >= MIN_BUGDET && mn * 10 <= MAX_BUGDET && mn * 10 % 1000 == 0)
            vnds.add(formatNumber(mn * 10));
        if (mn * 100 >= MIN_BUGDET && mn * 100 <= MAX_BUGDET && mn * 100 % 1000 == 0)
            vnds.add(formatNumber(mn * 100));
        if (mn * 1000 >= MIN_BUGDET && mn * 1000 <= MAX_BUGDET)
            vnds.add(formatNumber(mn * 1000));
        if (mn * 10000 >= MIN_BUGDET && mn * 10000 <= MAX_BUGDET)
            vnds.add(formatNumber(mn * 10000));
        if (mn * 100000 >= MIN_BUGDET && mn * 100000 <= MAX_BUGDET)
            vnds.add(formatNumber(mn * 100000));
        if (mn * 1000000 >= MIN_BUGDET && mn * 1000000 <= MAX_BUGDET)
            vnds.add(formatNumber(mn * 1000000));
        if (mn * 10000000 >= MIN_BUGDET && mn * 10000000 <= MAX_BUGDET)
            vnds.add(formatNumber(mn * 10000000));
        adapter = new CustomArrayAdapter(getContext(), vnds);
        edtBudget.setAdapter(adapter);
    }

    private String getLongAutoCompleteTextView(AutoCompleteTextView autoCompleteTextView) {
        return autoCompleteTextView.getText().toString().replace(",", "").replace(".", "");
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
}
