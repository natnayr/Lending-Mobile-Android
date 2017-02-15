package com.crowdo.p2pmobile.view.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.crowdo.p2pmobile.R;
import com.crowdo.p2pmobile.databinding.ItemLearningCenterBinding;
import com.crowdo.p2pmobile.model.LearningCenter;
import com.crowdo.p2pmobile.viewmodel.LearningCenterViewModel;
import com.github.aakira.expandablelayout.ExpandableLayoutListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by cwdsg05 on 10/2/17.
 */

public class LearningCenterAdapter extends RecyclerView.Adapter<LearningCenterAdapter.ViewHolder>{

    private List<LearningCenter> mCollection;
    private List<LearningCenter> mFullList;
    private Context mContext;

    private static final String LOG_TAG = LearningCenterAdapter.class.getSimpleName();


    public LearningCenterAdapter(Context context, List<LearningCenter> collection) {
        this.mCollection = new ArrayList<>(collection);
        this.mFullList = new ArrayList<>(collection);
        this.mContext = context;
    }

    public void search(String searchText){
        searchText = searchText.toLowerCase();

        mCollection.clear();
        if(searchText.length() == 0){
            mCollection.addAll(mFullList);
        }else{
            for(LearningCenter lcItem : mFullList){
                if(lcItem.getQuestion().toLowerCase().contains(searchText) ||
                        lcItem.getAnswer().toLowerCase().contains(searchText)){
                    mCollection.add(lcItem);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemLearningCenterBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_learning_center, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ItemLearningCenterBinding binding = holder.binding;
        holder.setIsRecyclable(false);
        binding.setLcvm(new LearningCenterViewModel(mCollection.get(position), mContext));
        binding.itemLearningCenterExpandLayout.initLayout();
        binding.itemLearningCenterExpandLayout.setInRecyclerView(true);
        binding.itemLearningCenterQuestions.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                binding.itemLearningCenterExpandLayout.toggle();
            }
        });

        binding.itemLearningCenterExpandLayout.setListener(new ExpandableLayoutListener() {
            @Override
            public void onAnimationStart() {
                binding.itemLearningCenterExpandLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

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

    @Override
    public int getItemCount() {
        return mCollection.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        private ItemLearningCenterBinding binding;

        public ViewHolder(ItemLearningCenterBinding binding) {
            super(binding.itemLearningCanterCardView);
            this.binding = binding;
        }
    }
}
