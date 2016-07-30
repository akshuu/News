package com.akshatjain.codepath.news.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.akshatjain.codepath.news.R;

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


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = null;
                StringBuilder facets = new StringBuilder();

                if(!TextUtils.isEmpty(txtDate.getText())){
                    date = txtDate.getText().toString();
                }
                String  sortOrder = (String) spinnerOrder.getSelectedItem();

                if(chkArts.isChecked())
                    facets.append("Arts,");
                if(chkFashion.isChecked()) {
                    facets.append("Fashion,");
                }
                if(chkSports.isChecked()) {
                    facets.append("Sports,");
                }
                if(facets.length() >0)
                    facets.deleteCharAt(facets.length()-1);

                asQuery.updateSearchQuery(date,sortOrder,facets.toString());
                dismiss();
            }

        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        asQuery = (AdvanceSearchQuery)activity;

    }
}
