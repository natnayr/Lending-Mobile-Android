package com.crowdo.p2pmobile;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;

import com.crowdo.p2pmobile.data.LoanItem;

import java.util.List;

/**
 * Created by cwdsg05 on 15/11/16.
 */

public class LoansAdapter extends BaseAdapter {
    private Context mContext;
    private List<LoanItem> mList;

    public LoansAdapter() {
        super();
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
