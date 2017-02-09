package com.crowdo.p2pmobile.model.learning_center;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by cwdsg05 on 9/2/17.
 */

public class Category extends RealmObject {

    @Required public String category;
    public RealmList<QuestionAnswer> questionAnswers;
}
