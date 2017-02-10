package com.crowdo.p2pmobile.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;
import java.util.Observable;

import rx.Subscription;

/**
 * Created by cwdsg05 on 9/2/17.
 */

public class LearningCenterViewModel extends Observable{

    private Context context;
    private Subscription subscription;

    public LearningCenterViewModel(@NonNull Context context){
        this.context = context;
    }

}
