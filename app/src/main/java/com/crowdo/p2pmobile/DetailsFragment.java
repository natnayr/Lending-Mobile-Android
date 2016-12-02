package com.crowdo.p2pmobile;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsFragment extends Fragment {

    public DetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);


        ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.loan_detail_progress_bar);
        progressBar.setProgress(70);

        return rootView;
    }
}
