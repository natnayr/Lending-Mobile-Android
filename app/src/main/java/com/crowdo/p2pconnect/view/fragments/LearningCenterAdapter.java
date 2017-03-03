package com.crowdo.p2pconnect.view.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.databinding.ItemLearningCenterBinding;
import com.crowdo.p2pconnect.model.LearningCenter;
import com.crowdo.p2pconnect.viewmodel.LearningCenterItemViewModel;

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
        if(searchText != null) {
            searchText = searchText.toLowerCase().trim();

            mCollection.clear();
            if (searchText.length() == 0) {
                mCollection.addAll(mFullList);
            } else {
                for (LearningCenter lcItem : mFullList) {
                    if (lcItem.getQuestion().toLowerCase().contains(searchText) ||
                            lcItem.getAnswer().toLowerCase().contains(searchText)) {
                        mCollection.add(lcItem);
                    }
                }
            }
            notifyDataSetChanged();
        }
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
        binding.setLcvm(new LearningCenterItemViewModel(mCollection.get(position), mContext));
        binding.itemLearningCenterRelativeLayoutQuestion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(binding.itemLearningCenterExpandLayout.isExpanded()) {
                    binding.itemLearningCenterDrawableQuestion.setRotation(0f);
                }else{
                    binding.itemLearningCenterDrawableQuestion.setRotation(180f);
                }
                binding.itemLearningCenterExpandLayout.toggle();
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
