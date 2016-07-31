package com.akshatjain.codepath.news.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.akshatjain.codepath.news.R;
import com.akshatjain.codepath.news.activities.NewsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by akshatjain on 7/30/16.
 */
public class SearchDialogFragment extends DialogFragment {

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

    private Activity mActivity;

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
        mActivity = activity;
        asQuery = (AdvanceSearchQuery)activity;


    }
}
