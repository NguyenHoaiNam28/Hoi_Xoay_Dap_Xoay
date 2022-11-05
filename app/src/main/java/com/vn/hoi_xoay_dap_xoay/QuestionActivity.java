package com.vn.hoi_xoay_dap_xoay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.vn.hoi_xoay_dap_xoay.model.Question;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class QuestionActivity extends AppCompatActivity {


    private TextView txtviewquestion;
    private TextView txtviewscore;
    private TextView txtviewquestioncount;
    private TextView txtviewcategory;
    private TextView txtviewcountdown;
    private RadioGroup radioGroup;
    private RadioButton rb1, rb2, rb3, rb4;
    private Button btnXacNhan;

    private CountDownTimer countDownTimer;
    private ArrayList<Question> questionList;
    private long timeLeftInMillis;
    private int questionCounter;
    private int questionSize;

    private int Score;
    private boolean answered;
    private Question currentQuestion;

    private int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        addControls();


        // nhận dữ liệu chủ đề
        Intent intent = getIntent();
        int categoryID = intent.getIntExtra("idcategory", 0);
        String categoryName = intent.getStringExtra("namecategory");

        txtviewcategory.setText("Chủ đề : " + categoryName);



        DataBase dataBase = new DataBase(this);

        questionList = dataBase.getQuestions(categoryID);

        questionSize = questionList.size();
        // đảo vị trí các phần tử câu hỏi
        Collections.shuffle(questionList);

        showNextQuestion();

    }
    // hiển thị câu hỏi
    private void showNextQuestion() {
        rb1.setTextColor(Color.BLACK);
        rb2.setTextColor(Color.BLACK);
        rb3.setTextColor(Color.BLACK);
        rb4.setTextColor(Color.BLACK);
        // xóa chọn
        radioGroup.clearCheck();
        // Nếu còn câu hỏi
        if (questionCounter < questionSize ){
            // Lấy dữ liêu ở vị trí counter
            currentQuestion = questionList.get(questionCounter);

            txtviewquestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            rb4.setText(currentQuestion.getOption4());

            //Tăng sau mỗi câu hỏi
            questionCounter++;

            txtviewquestioncount.setText("Câu hỏi : " + questionCounter + " / "+ questionSize );
            //Giá trị false, chưa trả lời
            answered = false;
            // Gán tên cho button
            btnXacNhan.setText("Xác nhận");
            // thời gian chạy 30s
            timeLeftInMillis = 30000;
        }
        else {
            finishQuestion();
        }
    }
    //back
    @Override
    public void onBackPressed() {
        count++;
        if (count >=1){
            finishQuestion();
        }
        count = 0;

    }

    // thoát qua giao diện chính
    private void finishQuestion() {
        //Chứa dữ liệu gửi qua Activity main
        Intent resultIntent = new Intent();
        resultIntent.putExtra("score", Score);
        setResult(RESULT_OK, resultIntent);
        finish();

    }

    private void addControls() {
        txtviewcountdown = findViewById(R.id.txt_countdown);
        txtviewcategory = findViewById(R.id.txt_view_category);
        txtviewquestion = findViewById(R.id.txtQuestion);
        txtviewscore = findViewById(R.id.txtScore);
        txtviewquestioncount = findViewById(R.id.txt_view_question_count);

        radioGroup = findViewById(R.id.radioGroup);
        rb1 = findViewById(R.id.radioButton1);
        rb2 = findViewById(R.id.radioButton2);
        rb3 = findViewById(R.id.radioButton3);
        rb4 = findViewById(R.id.radioButton4);

        btnXacNhan = findViewById(R.id.btnXacNhan);

    }


}