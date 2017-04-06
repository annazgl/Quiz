package com.anna.quizweekend;


import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class RandomQuestionsDatabase implements IQuestionsDatabase{
    @Override
    public List<Questions> getQuestions() {
        List<Questions>questionses = new LinkedList<>();
        Random random = new Random();

        for(int i = 0; i < 30; i++){
            Questions questions = new Questions();

            //Treść pytania
            int left = random.nextInt(100), right = random.nextInt(100);
            questions.setQuestion(String.format("%d + %d = ?", left , right));
            int correctAnswer = left + right;

            //Odpowiedzi
            List<String> answers = new LinkedList<>();
            int correctPosition = random.nextInt(3);
            for(int a = 0; a <3; a++){
                answers.add(random.nextInt(200)+ ""); // "" przerobienie inta na stringa
            }
            answers.set(correctPosition, correctAnswer + ""); //zamienia nam jedna odp na prawidłową
            questions.setAnswers(answers);
            questions.setCorrectAnswer(correctPosition);

            questionses.add(questions);
        }
        return questionses;
    }
}
