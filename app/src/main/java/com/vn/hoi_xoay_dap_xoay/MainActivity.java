package com.vn.hoi_xoay_dap_xoay;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vn.hoi_xoay_dap_xoay.model.Category;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView txtScore, txtDiemVuaChoi;
    private Spinner spinnerCategories;
    private Button btnStart, btnHoTro, btnCancel, btnOk;
    private EditText txtFeedBack;
    private int hightscore, scorevuachoi;
    private static final int REQUEST_CODE_QUESTION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();

        addEvents();
        //load chủ đề
        loadCategories();
        //load điểm cao
        loadHightScore();

        //Điểm vừa chơi
//        txtDiemVuaChoi.setText("Điểm Vừa Chơi: "+scorevuachoi);



    }

    private void loadHightScore() {
        SharedPreferences preferences = getSharedPreferences("share", MODE_PRIVATE);
        SharedPreferences pre = getSharedPreferences("sharescorevuachoi", MODE_PRIVATE);
        hightscore = preferences.getInt("hightscore", 0);
        scorevuachoi  = pre.getInt("scorevuachoi", 0);
        txtScore.setText("Điểm Cao Nhất : "+ hightscore);
        txtDiemVuaChoi.setText("Điểm Vừa Chơi : " + scorevuachoi);

    }



    private void addEvents() {

        // click bắt đầu
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQuestion();
            }
        });


        btnHoTro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openFeedbackDialog(Gravity.CENTER);


//                Toast.makeText(MainActivity.this, "hihi", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void openFeedbackDialog(int gravity) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_kiemtraadmin);

        Window window = dialog.getWindow();
        if (window == null){
            return;
        }else {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            windowAttributes.gravity = gravity;
            window.setAttributes(windowAttributes);
        }

        if (Gravity.BOTTOM == gravity){
            dialog.setCancelable(true);

        }else {
            dialog.setCancelable(false);

        }
        txtFeedBack = dialog.findViewById(R.id.txtFeedBack);
        btnCancel = dialog.findViewById(R.id.btnCancel);
        btnOk = dialog.findViewById(R.id.btnOK);




        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String feed = "nam";
                String st = txtFeedBack.getText().toString();
                if (st.equals(feed)){
                Intent intent = new Intent(MainActivity.this, HoTroActivity.class);
                startActivityForResult(intent, REQUEST_CODE_QUESTION);

                }else {
                    Toast.makeText(MainActivity.this, "Xin lỗi bạn nhập sai rồi!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();



    }

    // Hàm bắt đầu câu hỏi
    private void startQuestion() {
        // Lấy id, name chủ đề đã chọn
        Category category = (Category) spinnerCategories.getSelectedItem();
        int categoryID = category.getId();
        String categoryName = category.getName();

        //chuyển qua activity question
        Intent intent = new Intent(MainActivity.this, QuestionActivity.class);

        intent.putExtra("idcategory", categoryID);
        intent.putExtra("namecategory", categoryName);

        // sử dụng startActivityForResult để có thể nhận lại dữ liệu kết quả trả về thông qua phương thức onActivityResult()
        startActivityForResult(intent, REQUEST_CODE_QUESTION);
    }



    private  void addControls(){
        txtScore = findViewById(R.id.txtScore);
        txtDiemVuaChoi = findViewById(R.id.txtDiemVuaChoi);
        btnStart = findViewById(R.id.btnStart);
        spinnerCategories = findViewById(R.id.spinnerCategories);
        btnHoTro = findViewById(R.id.btnHoTro);



    }

    private void loadCategories(){
        DataBase dataBase = new DataBase(this);
        //lấy dữ liệu danh sách chủ đề
        List<Category> categories = dataBase.getDataCategories();
        ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCategories.setAdapter(categoryArrayAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_QUESTION){
            if (resultCode == RESULT_OK){
                int score = data.getIntExtra("score", 0);

                if (score > hightscore) {
                    updateHightScore(score);
                }
                if (score < hightscore || score > hightscore || score == hightscore){
                    updateHightScoreVuaChoi(score);
                }
            }
        }
    }

    private void updateHightScoreVuaChoi(int score) {

        scorevuachoi = score;

        txtDiemVuaChoi.setText("Điểm Vừa Chơi: "+scorevuachoi);
        //Lưu trữ điểm
        SharedPreferences sharedPreferences = getSharedPreferences("sharescorevuachoi", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //gán giá trị điểm cao mới
        editor.putInt("scorevuachoi", scorevuachoi);
        //hoàn tất
        editor.apply();
    }


    //cập nhập điểm cao
    private void updateHightScore(int score) {
        //Gán điểm cao mới
        hightscore = score;
        // Hiển thị
        txtScore.setText("Điểm Cao Nhất : " + hightscore);
//        Lưu trữ điểm
        SharedPreferences sharedPreferences = getSharedPreferences("share", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //gán giá trị điểm cao mới
        editor.putInt("hightscore", hightscore);
        //hoàn tất
        editor.apply();
    }
}