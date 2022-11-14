package com.vn.hoi_xoay_dap_xoay;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vn.hoi_xoay_dap_xoay.model.Category;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView txtScore, txtDiemVuaChoi;
    private Spinner spinnerCategories;
    private Button btnStart;
    private ImageButton btn_admin;
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
        btn_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setTitle("Bạn Có Muốn Vào Trang Quản Lí Không");
                builder1.setMessage("nếu có hãy nhập mã xác Minh Vào");
                builder1.setCancelable(true);
                EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setHint("Nhập Vào Mã Số");

                    builder1.setView(input);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(Integer.parseInt(input.getText().toString()) == 11111)
                                {
                                    Toast.makeText(MainActivity.this, "Xác Minh Thành Công", Toast.LENGTH_SHORT).show();
                                     startActivity(new Intent(MainActivity.this,tabhost.class));

                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this, "Xác Minh Thất Bại", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
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
        btnStart = findViewById(R.id.btnStart);
        txtDiemVuaChoi = findViewById(R.id.txtDiemVuaChoi);
        btn_admin = findViewById(R.id.btn_admin);
        spinnerCategories = findViewById(R.id.spinnerCategories);


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