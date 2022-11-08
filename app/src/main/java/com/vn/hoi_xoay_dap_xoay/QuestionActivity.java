package com.vn.hoi_xoay_dap_xoay;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vn.hoi_xoay_dap_xoay.model.Question;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity {


    private TextView txtviewquestion;
    private TextView txtviewscore;
    private TextView txtviewquestioncount;
    private TextView txtviewcategory;
    private TextView txtviewcountdown;
    private TextView txtTitle;
    private RadioGroup radioGroup;
    private RadioButton rb1, rb2, rb3, rb4;
    private Button btnXacNhan, btntrogiup1;

    private CountDownTimer countDownTimer;
    private ArrayList<Question> questionList;
    private long timeLeftInMillis;
    private int questionCounter;
    private int questionSize;

    private int Score;
    private boolean answered;
    private Question currentQuestion;
    private Vibrator vibrator;

    private int count = 0;

    private int checkPress = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
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

        addEvents();





    }

    private void addEvents() {
        //button xác nhận, câu tiếp, hoàn thành
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Nếu chưa trả lời câu hỏi
                if (!answered){
                    //Nếu chọn 1 trong 4 đáp án
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()){
                        //Kiểm tra đáp án
                        checkAnswer();
                    }else {
                        //Thông báo nếu không chọn
                        Toast.makeText(QuestionActivity.this, "Hãy chọn đáp án", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    showNextQuestion();
                }




            }
        });

        btntrogiup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Integer> _arr = new ArrayList<Integer>();
                _arr.add(1);
                _arr.add(2);
                _arr.add(3);
                _arr.add(4);

//                Log.e("TAG", currentQuestion.getAnswer()+" Dap an");

                //remove đi đáp ấn đúng trong list
                _arr.remove(currentQuestion.getAnswer()-1);
//                Log.e("TAG", _arr.toString()+"Da xoa");
                Random rd = new Random();

                //random ra 1 số và remove số đó đi trong list
                int d = rd.nextInt(_arr.get(rd.nextInt(_arr.size())));
                _arr.remove(d);

                //ẩn 2 số còn lại trong list
                for(int i=0;i<_arr.size();i++){
                    switch (_arr.get(i)) {
                        case 1:
                            rb1.setVisibility(View.INVISIBLE);
                            break;
                        case 2:
                            rb2.setVisibility(View.INVISIBLE);
                            break;
                        case 3:
                            rb3.setVisibility(View.INVISIBLE);
                            break;
                        case 4:
                            rb4.setVisibility(View.INVISIBLE);
                            break;
                    }
                }
                // ẩn button trợ giúp 50/50 khi đã ấn
                btntrogiup1.setVisibility(View.INVISIBLE);

            }
        });
    }

    // hiển thị câu hỏi
    private void showNextQuestion() {
        // add màu txt
        addMau();

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
            //Đếm ngược thời gian trả lời
            startCountDown();
        }
        else {
            finishQuestion();
        }
    }

    private void addMau() {
        rb1.setTextColor(Color.BLACK);
        rb2.setTextColor(Color.BLACK);
        rb3.setTextColor(Color.BLACK);
        rb4.setTextColor(Color.BLACK);



        txtviewscore.setTextColor(Color.BLACK);
        txtviewquestion.setTextColor(Color.BLACK);
        txtviewcountdown.setTextColor(Color.BLACK);
        txtviewquestioncount.setTextColor(Color.BLACK);
        txtTitle.setTextColor(Color.BLACK);
        txtviewcategory.setTextColor(Color.BLACK);
    }

    // Đếm ngược thời gian
    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMillis = l;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();

                // Phương thức kiểm tra đáp án
                checkAnswer();
            }
        }.start();
    }



    //Kiểm tra đáp án
    private void checkAnswer() {
        // true đã trả lời
        answered = true;
        //Trả về radiobutton trong rdGroup
        RadioButton rbSelected = findViewById(radioGroup.getCheckedRadioButtonId());
        //Vị trí của câu đã chọn
        int answer = radioGroup.indexOfChild(rbSelected) + 1;
        //Nếu trả lời đúng đáp án
        if (answer == currentQuestion.getAnswer()){
            Toast.makeText(this, "Chúc mừng, bạn đã trả lời đúng", Toast.LENGTH_SHORT).show();
            //Tăng 10 diểm
            Score = Score + 10;
            //Hiển thị điểm
            txtviewscore.setText("Điểm : "+ Score);

        }
        if (answer != currentQuestion.getAnswer() && timeLeftInMillis > 1000){
            Toast.makeText(this, "Rất tiếc, bạn sai rồi", Toast.LENGTH_SHORT).show();

        }
        if (answer != currentQuestion.getAnswer() && timeLeftInMillis == 0){
            Toast.makeText(this, "Rất tiếc, hết giờ mất rồi", Toast.LENGTH_SHORT).show();

        }
        //Phương thức hiển thị đáp án
        showSolution();


    }
    //Đáp án
    private void showSolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        rb4.setTextColor(Color.RED);
        //Kiểm tra đáp án
        switch (currentQuestion.getAnswer()){
            case 1:
                rb1.setTextColor(Color.GREEN);
                txtviewquestion.setText("Đáp án là A");
//                rb1.setBackgroundColor(Color.GREEN);
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                txtviewquestion.setText("Đáp án là B");
//                rb2.setBackgroundColor(Color.GREEN);
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                txtviewquestion.setText("Đáp án là C");
//                rb3.setBackgroundColor(Color.GREEN);
                break;
            case 4:
                rb4.setTextColor(Color.GREEN);
                txtviewquestion.setText("Đáp án là D");
//                rb4.setBackgroundColor(Color.GREEN);
                break;

        }

        //Nếu còn câu hỏi thì button sẽ hiện là câu tiếp
        if (questionCounter<questionSize){
            btnXacNhan.setText("Câu tiếp");
            rb1.setVisibility(View.VISIBLE);
            rb2.setVisibility(View.VISIBLE);
            rb3.setVisibility(View.VISIBLE);
            rb4.setVisibility(View.VISIBLE);
        }
        //setText hoàn thành
        else {
            btnXacNhan.setText("Hoàn thành");
        }
        countDownTimer.cancel();
    }

    //update thời gian
    private void updateCountDownText() {
        //tính phút
//        int minutes = (int) ((timeLeftInMillis/1000)/60);
        // tính giây
        int seconds = (int) ((timeLeftInMillis/1000)%60);
        //Định dạng thời gian
        String timeFormatted = String.format(Locale.getDefault(), "%02d", seconds);
        //Hiển thị lên màn hình
        txtviewcountdown.setText(timeFormatted);

        if (timeLeftInMillis < 10000 && timeLeftInMillis >9000){
            Toast.makeText(this, "Thời gian sắp hết!!!", Toast.LENGTH_SHORT).show();


        }
        //Nếu thời gian dưới 10s thì chuyển qua màu đỏ
        if (timeLeftInMillis < 10000){
            txtviewcountdown.setTextColor(Color.RED);

            //rung khi còn dưới 10s
            vibrator   = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            if (vibrator.hasVibrator()) {
                vibrator.vibrate(500); // for 500 ms
            }
        }
        else {
            txtviewcountdown.setTextColor(Color.BLACK);
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
        txtviewscore = findViewById(R.id.txt_view_score);
        txtviewquestioncount = findViewById(R.id.txt_view_question_count);

        radioGroup = findViewById(R.id.radioGroup);
        rb1 = findViewById(R.id.radioButton1);
        rb2 = findViewById(R.id.radioButton2);
        rb3 = findViewById(R.id.radioButton3);
        rb4 = findViewById(R.id.radioButton4);

        btnXacNhan = findViewById(R.id.btnXacNhan);
        btntrogiup1 = findViewById(R.id.btntrogiup1);
        txtTitle = findViewById(R.id.txtTitle);
    }


}