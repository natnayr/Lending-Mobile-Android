package com.crowdo.p2pmobile.view.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.crowdo.p2pmobile.R;
import com.crowdo.p2pmobile.databinding.ItemLearningCenterBinding;
import com.crowdo.p2pmobile.model.LearningCenter;
import com.crowdo.p2pmobile.viewmodel.LearningCenterViewModel;
import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListener;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;


/**
 * Created by cwdsg05 on 10/2/17.
 */

public class LearningCenterAdapter extends RealmRecyclerViewAdapter<LearningCenter, LearningCenterAdapter.BindingHolder>{

    private OrderedRealmCollection<LearningCenter> mResult;
    private Context mContext;

    public LearningCenterAdapter(Context context, OrderedRealmCollection<LearningCenter> data) {
        super(context, data, true);
        mResult = data;
        mContext = context;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemLearningCenterBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_learning_center, parent, false);

        return new BindingHolder(binding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        final ItemLearningCenterBinding binding = holder.binding;
        holder.setIsRecyclable(false);
        binding.setLcvm(new LearningCenterViewModel(mResult.get(position), mContext));
        binding.itemLearningCenterExpandLayout.initLayout();
        binding.itemLearningCenterExpandLayout.setInRecyclerView(true);
        binding.itemLearningCenterQuestions.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onClickTextView(binding.itemLearningCenterExpandLayout);
            }
        });

        binding.itemLearningCenterExpandLayout.setListener(new ExpandableLayoutListener() {
            @Override
            public void onAnimationStart() { }

            @Override
            public void onAnimationEnd() { }

            @Override
            public void onPreOpen() { }

            @Override
            public void onPreClose() { }

            @Override
            public void onOpened() {
                binding.itemLearningCenterExpandLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            @Override
            public void onClosed() {

            }
        });
    }

    private void onClickTextView(final ExpandableLayout expandableLayout){
        expandableLayout.toggle();
    }

    @Override
    public int getItemCount() {
        return mResult.size();
    }


    public static class BindingHolder extends RecyclerView.ViewHolder{

        private ItemLearningCenterBinding binding;

        public BindingHolder(ItemLearningCenterBinding binding) {
            super(binding.itemLearningCanterCardView);
            this.binding = binding;
        }
    }
}
