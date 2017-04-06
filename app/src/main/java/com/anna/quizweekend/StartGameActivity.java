package com.anna.quizweekend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartGameActivity extends AppCompatActivity {

    @BindView(R.id.name_field)
    EditText mEditText;

    private IQuestionsDatabase mQuestionDatabase = new RandomQuestionsDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.next_button)
    void openNextScreen(){
        //1. Odczytać wpisany tekst
        String name = mEditText.getText().toString();
        //2. Otworzyć nowe okno przekazując wpisany tekst
        Intent nameIntent = new Intent(this, GreetingActivity.class);
        nameIntent.putExtra(GreetingActivity.EXTRA_NAME, name);

        //Losowanie pytań
        List<Questions>questionses = mQuestionDatabase.getQuestions();
        Random random = new Random();
        while(questionses.size()>5){
            //Usuwa losowy element z listy (
            // random.nextInt(questionses.size()) usuwa losowy element na liście
            questionses.remove(random.nextInt(questionses.size()));
        }
        Collections.shuffle(questionses);

        nameIntent.putExtra(GreetingActivity.EXTRA_QUESTIONSES,new ArrayList<>(questionses));

        startActivity(nameIntent);

    }
}




