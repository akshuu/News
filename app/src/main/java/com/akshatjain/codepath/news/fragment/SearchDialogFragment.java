package com.akshatjain.codepath.news.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.akshatjain.codepath.news.R;
import com.akshatjain.codepath.news.activities.NewsActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by akshatjain on 7/30/16.
 */
public class SearchDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.btnSave)
    Button btnSave;

    @BindView(R.id.txtDate)
    TextView txtDate;

    @BindView(R.id.chkArts)
    CheckBox chkArts;

    @BindView(R.id.chkSports)
    CheckBox chkSports;


    @BindView(R.id.chkFashion)
    CheckBox chkFashion;

    @BindView(R.id.spinner)
    Spinner spinnerOrder;

    private AppCompatActivity mActivity;

    private AdvanceSearchQuery asQuery;

    public interface AdvanceSearchQuery{
        public void updateSearchQuery(@Nullable String date,String sortOrder, @Nullable String facets);
    }

    public SearchDialogFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.advance_search_dialog, container);
        ButterKnife.bind(this, view);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mActivity, R.array.sortOrder,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrder.setAdapter(adapter);

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = null;
                StringBuilder facets = new StringBuilder();

                if(!TextUtils.isEmpty(txtDate.getText())){
                    date = txtDate.getText().toString();        // TODO: Format to YYYYMMDD
                }
                String  sortOrder = null;
                if(spinnerOrder.getSelectedItemPosition() == 0){
                    sortOrder = null;
                }else {
                    sortOrder =   (String) spinnerOrder.getSelectedItem();
                }

                if(chkArts.isChecked())
                    facets.append("Arts ");
                if(chkFashion.isChecked()) {
                    facets.append("Fashion ");
                }
                if(chkSports.isChecked()) {
                    facets.append("Sports ");
                }
                if(facets.length() >0) {
                    facets.deleteCharAt(facets.length() - 1);
                    facets.insert(0,"news_desk:(");
                    facets.append(")");
                }

                asQuery.updateSearchQuery(date,sortOrder,facets.toString());
                dismiss();
            }

        });

        SharedPreferences pref = mActivity.getSharedPreferences(NewsActivity.SEARCH, Context.MODE_PRIVATE);
        String beginDate = pref.getString(NewsActivity.BEGIN_DATE,null);
        String sortOrder  = pref.getString(NewsActivity.SORT_ORDER,null);
        String facets = pref.getString(NewsActivity.FACETS,null);

        txtDate.setText(beginDate);
        int selection = 0;
        if(sortOrder == null)
            selection = 0;
        else if(sortOrder.equalsIgnoreCase("newest"))
            selection = 1;
        else if(sortOrder.equalsIgnoreCase("oldest"))
            selection = 2;
        else if(sortOrder.equalsIgnoreCase("asc"))
            selection = 3;
        else if(sortOrder.equalsIgnoreCase("desc"))
            selection = 4;

        spinnerOrder.setSelection(selection);


        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (AppCompatActivity) activity;
        asQuery = (AdvanceSearchQuery)activity;
    }

    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setTargetFragment(SearchDialogFragment.this, 300);

        newFragment.show(mActivity.getSupportFragmentManager(), "datePicker");
    }

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String date = format.format(c.getTime());
        txtDate.setText(date);
    }
}
