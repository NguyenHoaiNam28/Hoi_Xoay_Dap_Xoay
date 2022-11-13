package com.vn.hoi_xoay_dap_xoay;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vn.hoi_xoay_dap_xoay.model.Category;
import com.vn.hoi_xoay_dap_xoay.model.Question;

import java.util.ArrayList;
import java.util.List;

public class AddQuestion extends AppCompatActivity {
    Spinner spinnerCategories;
    EditText ed_question,ed_opt1,ed_opt2,ed_opt3,ed_opt4,ed_answer;
    ImageButton btn_back;
    Button btn_add;
    int ID;
    List<Category> categories;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addquestion);
        addControls();
        loadCategories();

        AddEvents();

    }

    private void AddEvents() {
        // su kien tro ve trang
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddQuestion.this, tabhost.class);
                startActivity(i);

            }
        });
        // su kien them danh sach cau hoi moi
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ed_question.getText().toString().isEmpty() || ed_opt1.getText().toString().isEmpty() || ed_opt2.getText().toString().isEmpty()
                || ed_opt3.getText().toString().isEmpty() || ed_opt4.getText().toString().isEmpty() ||
                        ed_answer.getText().toString().isEmpty())
                {
                    Toast.makeText(AddQuestion.this, "Vui Lòng Điền Đầy Đủ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent  = getIntent();
                    if(intent != null)
                    {

                        String i = intent.getStringExtra("add");
                        if(i.equals("1"))
                        {
                            Add();
                        }
                        if(i.equals("2"))
                        {
                            edit();
                        }
                    }

                }
            }


        });

    }
    //lay id
    private int ID()
    {
        Category category = (Category) spinnerCategories.getSelectedItem();
        int categoryID = category.getId();
        return  categoryID;
    }

    //add cau hoi
    private void Add() {
       int categoryID =  ID();
        DataBase db = new DataBase(AddQuestion.this);
        db.db = openOrCreateDatabase(db.getDatabaseName(),MODE_PRIVATE,null);
        // them du lieu vo
        ContentValues values = new ContentValues();
        values.put(Table.QuestionsTable.COLUMN_QUESTION, "" + ed_question.getText().toString());
        values.put(Table.QuestionsTable.COLUMN_OPTION1, "A. " + ed_opt1.getText().toString());
        values.put(Table.QuestionsTable.COLUMN_OPTION2, "B. " + ed_opt2.getText().toString());
        values.put(Table.QuestionsTable.COLUMN_OPTION3, "C. " + ed_opt3.getText().toString());
        values.put(Table.QuestionsTable.COLUMN_OPTION4, "D. " + ed_opt4.getText().toString());
        values.put(Table.QuestionsTable.COLUMN_ANSWER, Integer.parseInt(ed_answer.getText().toString()));
        values.put(Table.QuestionsTable.COLUMN_CATEGORY_ID, categoryID);
        long  i =  db.db.insert(Table.QuestionsTable.TABLE_NAME, null, values);
        if(i > 0)
        {
            ed_question.setText("");
            ed_opt1.setText("");
            ed_opt2.setText("");
            ed_opt3.setText("");
            ed_opt4.setText("");
            ed_answer.setText("");
            Toast.makeText(this, "Thêm Thành Công", Toast.LENGTH_SHORT).show();

        }else
        {
            Toast.makeText(this, "Thêm Không Thành Công", Toast.LENGTH_SHORT).show();

        }
        db.db.close();

    }
    //sua
    private void edit() {
        int categoryID =  ID();
        DataBase db = new DataBase(AddQuestion.this);
        db.db = openOrCreateDatabase(db.getDatabaseName(),MODE_PRIVATE,null);
        // them du lieu vo
        ContentValues values = new ContentValues();
        values.put(Table.QuestionsTable.COLUMN_QUESTION, ed_question.getText().toString());
        values.put(Table.QuestionsTable.COLUMN_OPTION1,  ed_opt1.getText().toString());
        values.put(Table.QuestionsTable.COLUMN_OPTION2,  ed_opt2.getText().toString());
        values.put(Table.QuestionsTable.COLUMN_OPTION3, ed_opt3.getText().toString());
        values.put(Table.QuestionsTable.COLUMN_OPTION4, ed_opt4.getText().toString());
        values.put(Table.QuestionsTable.COLUMN_ANSWER, Integer.parseInt(ed_answer.getText().toString()));
        values.put(Table.QuestionsTable.COLUMN_CATEGORY_ID, categoryID);
        long  i =  db.db.update(Table.QuestionsTable.TABLE_NAME, values,"_id = ?",new String[]{"" + ID});
        if(i > 0)
        {
            ed_question.setText("");
            ed_opt1.setText("");
            ed_opt2.setText("");
            ed_opt3.setText("");
            ed_opt4.setText("");
            ed_answer.setText("");
            Toast.makeText(this, "Chỉnh sửa Thành Công", Toast.LENGTH_SHORT).show();
            tabhost.adapterQuestion.notifyDataSetChanged();
        }else
        {
            Toast.makeText(this, "Chỉnh sửa Không Thành Công", Toast.LENGTH_SHORT).show();

        }
        db.db.close();

    }
    // add controls
    private  void addControls() {
        spinnerCategories = findViewById(R.id.spn_category);
        ed_question = findViewById(R.id.ed_question);
        ed_opt1 = findViewById(R.id.ed_opt1);
        ed_opt2 = findViewById(R.id.ed_opt2);
        ed_opt3 = findViewById(R.id.ed_opt3);
        ed_opt4 = findViewById(R.id.ed_opt4);
        ed_answer = findViewById(R.id.ed_answer);
        btn_add = findViewById(R.id.btn_add);
        btn_back = findViewById(R.id.btn_back);


        //lay giu lieu
    }
    // load danh sach
    private void loadCategories(){
        DataBase dataBase = new DataBase(this);
        //lấy dữ liệu danh sách chủ đề
        categories  = dataBase.getDataCategories();
        ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCategories.setAdapter(categoryArrayAdapter);
    }
    //


    @Override
    protected void onResume() {
        super.onResume();
        Intent i = getIntent();
        String a = i.getStringExtra("add");
        if(i != null)
        {
            if(a.equals("2"))
            {

                Question question = (Question) i.getSerializableExtra("question");
                ID = question.getId();
                for (int j = 0; j < categories.size(); j++) {
                    if(categories.get(j).getId() == question.getCategoryID() )
                    {
                        spinnerCategories.setSelection(j);

                    }
                }
                ed_question.setText(question.getQuestion());
                ed_opt1.setText(question.getOption1());
                ed_opt2.setText(question.getOption2());
                ed_opt3.setText(question.getOption3());
                ed_opt4.setText(question.getOption4());
                ed_answer.setText(String.valueOf(question.getAnswer()));
                btn_add.setText("Sửa");
            }
        }

    }
}
