package com.crowdo.p2pmobile.model.learning_center;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.Required;

/**
 * Created by cwdsg05 on 9/2/17.
 */

public class QuestionAnswer extends RealmObject{

    @Index
    @Required
    public String question;

    @Index
    @Required
    public String answer;
}
