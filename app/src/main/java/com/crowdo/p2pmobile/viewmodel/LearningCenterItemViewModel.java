package com.crowdo.p2pmobile.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import com.crowdo.p2pmobile.model.LearningCenter;

/**
 * Created by cwdsg05 on 9/2/17.
 */
public class LearningCenterItemViewModel extends BaseObservable{

    private Context mContext;
    private LearningCenter learningCenter;

    public LearningCenterItemViewModel(LearningCenter learningCenter, @NonNull Context context){
        this.learningCenter = learningCenter;
        this.mContext = context;
    }

    @Bindable
    public String getQuestion(){
        return learningCenter.getQuestion();
    }

    @Bindable
    public String getAnswer() { return learningCenter.getAnswer(); }

    @Bindable
    public String getIndex() { return learningCenter.getIndex(); }
}
