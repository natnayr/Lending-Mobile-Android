package com.crowdo.p2pmobile.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;

import com.crowdo.p2pmobile.model.learning_center.QuestionAnswer;

import java.util.List;
import java.util.Observable;

import rx.Subscription;

/**
 * Created by cwdsg05 on 9/2/17.
 */

public class LearningCenterViewModel extends Observable{

    private List<QuestionAnswer> questionAnswer;
    private Context context;
    private Subscription subscription;

    public LearningCenterViewModel(@NonNull Context context){
        this.context = context;
    }



}
