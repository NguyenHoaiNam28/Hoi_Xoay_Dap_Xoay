package com.vn.hoi_xoay_dap_xoay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vn.hoi_xoay_dap_xoay.model.Category;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView txtScore;
    private Spinner spinnerCategories;
    private Button btnStart;

    private static final int REQUEST_CODE_QUESTION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();

        addEvents();
        //load chủ đề
        loadCategories();


    }

    private void addEvents() {

        // click bắt đầu
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQuestion();
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
}