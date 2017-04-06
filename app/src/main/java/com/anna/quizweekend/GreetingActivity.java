package com.anna.quizweekend;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GreetingActivity extends AppCompatActivity {
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_QUESTIONSES = "questionses";
    public static final String CURRENT_QUESTION = "currentQuestion";
    public static final String CHOICES = "choices";

    @BindView(R.id.question)
    TextView mQuestion;
    @BindView(R.id.answer_choice)
    RadioGroup mAnswers;
    @BindViews({R.id.answer_1,R.id.answer_2,R.id.answer_3})
    List<RadioButton> mRadioButtons;
    @BindView(R.id.back)
    Button mBackButton;
    @BindView(R.id.next)
    Button mNextButton;

    private List<Questions> mQuestions;
    private int mCurrentQuestion;
    private int[] mChoices;
    private String mPlayerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeting);
        ButterKnife.bind(this);

        // 1. Odczytanie przekazanego parametru
        mPlayerName = getIntent().getStringExtra(EXTRA_NAME);
        mQuestions = (List<Questions>) getIntent().getSerializableExtra(EXTRA_QUESTIONSES);
        mChoices = new int[mQuestions.size()];
        refreshView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mChoices[mCurrentQuestion]= mAnswers.getCheckedRadioButtonId();
        outState.putInt(CURRENT_QUESTION, mCurrentQuestion);
        outState.putIntArray(CHOICES, mChoices);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentQuestion = savedInstanceState.getInt(CURRENT_QUESTION, 0);
        mChoices = savedInstanceState.getIntArray(CHOICES);
        refreshView();
    }

    @OnClick(R.id.back)
    void onBackClick() {
        //Zapisanie wybranej odpowiedzi na bieżące pytanie
        mChoices[mCurrentQuestion]= mAnswers.getCheckedRadioButtonId();
        mCurrentQuestion--;
        refreshView();
    }

    @OnClick(R.id.next)
    void onNextClick() {
        //Zapisanie wybranej odpowiedzi na bieżące pytanie
        mChoices[mCurrentQuestion]= mAnswers.getCheckedRadioButtonId();
        boolean isLastQuestion = mCurrentQuestion + 1 == mQuestions.size();
        if (isLastQuestion) {
            countResult();
            return;
        }
        mCurrentQuestion++;
        refreshView();
    }

    private void refreshView() {
        Questions questions = mQuestions.get(mCurrentQuestion);
        mQuestion.setText(questions.getQuestion()); //Ustawienie treści pytania

        // Ustawienie odpowiedzi w RadioButtonach
        int index = 0;
        for (RadioButton rb : mRadioButtons) {
            rb.setText(questions.getAnswers().get(index++));
        }
        //Ustawienie widoczności/ tekstu przycisków
        mBackButton.setVisibility(mCurrentQuestion == 0 ? View.GONE : View.VISIBLE);
        mNextButton.setText(mCurrentQuestion == mQuestions.size() - 1 ? "Zakończ" : "Dalej");

        //Czyszczenie zaznaczenia przy przejściach
        mAnswers.clearCheck();
        if(mChoices[mCurrentQuestion]>0){
            mAnswers.check(mChoices[mCurrentQuestion]);
        }
    }

    private void countResult(){
        int correctAnswers = 0;
        int questionsCount = mQuestions.size();

        for( int i = 0; i < questionsCount; i++){
            int correctAnswerIndex = mQuestions.get(i).getCorrectAnswer();
            int choiceRadioButtonId = mChoices[i];
            int choiceIndex = -1;
            for(int j = 0; j< mRadioButtons.size(); j++){
                if(mRadioButtons.get(j).getId() == choiceRadioButtonId){
                    choiceIndex = j;
                    break;
                }
            }
            if (correctAnswerIndex == choiceIndex){
                correctAnswers++;
            }
        }

        QuizResultDialogFragment.createDialog(mPlayerName,correctAnswers,questionsCount)
                .show(getSupportFragmentManager(),"");
    }
}
