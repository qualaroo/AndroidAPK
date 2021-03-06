package com.qualaroo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.appcompat.app.AppCompatActivity;

import com.qualaroo.internal.model.Survey;
import com.qualaroo.ui.SurveyComponent;
import com.qualaroo.ui.SurveyFragment;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;

@RestrictTo(LIBRARY)
public class QualarooActivity extends AppCompatActivity {

    private static final String KEY_SURVEY = "com.qualaroo.survey";

    public static void showSurvey(Context context, Survey survey) {
        Intent starter = new Intent(context, QualarooActivity.class);
        starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        starter.putExtra(KEY_SURVEY, survey);
        context.startActivity(starter);
    }

    private SurveyComponent surveyComponent;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        surveyComponent = (SurveyComponent) getLastCustomNonConfigurationInstance();
        if (surveyComponent == null) {
            Survey survey = (Survey) getIntent().getExtras().getSerializable(KEY_SURVEY);
            Qualaroo qualaroo = (Qualaroo) Qualaroo.getInstance();
            if (qualaroo == null) {
                finish();
                //TODO call Logger here to notify about this exception
                return;
            }
            this.surveyComponent = qualaroo.buildSurveyComponent(survey);
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, new SurveyFragment(), "survey")
                    .commit();
        }
    }

    @Override public Object getSystemService(@NonNull String name) {
        if (SurveyComponent.class.getName().equals(name)) {
            return surveyComponent;
        }
        return super.getSystemService(name);
    }

    @Override public Object onRetainCustomNonConfigurationInstance() {
        return surveyComponent;
    }

    @Override public void onBackPressed() {
        SurveyFragment surveyFragment = (SurveyFragment) getSupportFragmentManager().findFragmentById(android.R.id.content);
        if (surveyFragment != null) {
            surveyFragment.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }
}
