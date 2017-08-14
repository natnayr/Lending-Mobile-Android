package com.crowdo.p2pconnect.view.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.model.others.WalletEntry;
import com.crowdo.p2pconnect.viewholders.ItemWithdrawHistoryViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwdsg05 on 14/8/17.
 */

public class WithdrawHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String LOG_TAG = WithdrawHistoryAdapter.class.getSimpleName();
    private List<WalletEntry> walletEntryHistoryList;
    private Context mContext;

    public WithdrawHistoryAdapter(Context context) {
        this.walletEntryHistoryList = new ArrayList<>();
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_withdraw_history, parent, false);
        ItemWithdrawHistoryViewHolder viewHolder =
                new ItemWithdrawHistoryViewHolder(view, this.mContext);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemWithdrawHistoryViewHolder){
            final ItemWithdrawHistoryViewHolder itemHolder = (ItemWithdrawHistoryViewHolder) holder;

            //inverse list, latest on top
            final WalletEntry walletEntryItem = walletEntryHistoryList.get(walletEntryHistoryList.size() - 1 - position);

            itemHolder.populateItemDetails(walletEntryItem);
        }

    }

    @Override
    public int getItemCount() {
        return this.walletEntryHistoryList.size();
    }

    public void setWalletEntryHistoryList(@Nullable List<WalletEntry> returnedList){
        if(returnedList == null) {
            return;
        }

        this.walletEntryHistoryList.clear();
        this.walletEntryHistoryList.addAll(returnedList);

        notifyDataSetChanged();
    }
}
