package com.crowdo.p2pconnect.view.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.model.others.TopUp;
import com.crowdo.p2pconnect.viewholders.ItemTopUpHistoryViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwdsg05 on 2/8/17.
 */

public class TopUpHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String LOG_TAG = TopUpHistoryAdapter.class.getSimpleName();
    private List<TopUp> topUpHistoryList;
    private Context mContext;

    public TopUpHistoryAdapter(Context context) {
        this.topUpHistoryList = new ArrayList<>();
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_top_up_history, parent, false);
        ItemTopUpHistoryViewHolder viewHolder =
                new ItemTopUpHistoryViewHolder(view, this.mContext);

        viewHolder.initView();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemTopUpHistoryViewHolder){
            final ItemTopUpHistoryViewHolder itemHolder = (ItemTopUpHistoryViewHolder) holder;

            //inverse list, latest on top
            final TopUp topUpItem = topUpHistoryList.get(topUpHistoryList.size() - 1 - position);

            itemHolder.populateItemDetails(topUpItem);
        }
    }

    @Override
    public int getItemCount() {
        return this.topUpHistoryList.size();
    }


    public void setTopUpHistoryList(@Nullable List<TopUp> returnedList){
        if(returnedList == null){
            return;
        }

        this.topUpHistoryList.clear();
        this.topUpHistoryList.addAll(returnedList);

        notifyDataSetChanged();
    }
}
