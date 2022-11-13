package com.vn.hoi_xoay_dap_xoay;

import static com.vn.hoi_xoay_dap_xoay.R.id.btn_add;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class AddCategory extends AppCompatActivity {
    EditText ed_name;
    ImageButton btn_back;
    Button btn_add;
    int ID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcategory);
        Addcontrols();
        AddEvents();
    }

    private void Addcontrols() {
        ed_name = findViewById(R.id.ed_Name);
        btn_back = findViewById(R.id.btn_back);
        btn_add = findViewById(R.id.btn_add1);
    }

    private void AddEvents() {
       //  su kien tro ve trang
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddCategory.this,tabhost.class);
                startActivity(i);

            }
        });
        // su kien them danh muc moi
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ed_name.getText().toString().isEmpty())
                {
                    Toast.makeText(AddCategory.this, "Vui Lòng Điền Đầy Đủ", Toast.LENGTH_SHORT).show();
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
                            Edit();
                        }
                    }


                }
            }


        });

    }


    //add cau danh muc
    private void Add() {

        DataBase db = new DataBase(AddCategory.this);
        db.db = openOrCreateDatabase(db.getDatabaseName(),MODE_PRIVATE,null);
//        // them du lieu vo
        ContentValues values = new ContentValues();
        values.put(Table.CategoriesTable.COLUMN_NAME, "" + ed_name.getText().toString());
        long i =  db.db.insert(Table.CategoriesTable.TABLE_NAME, null, values);

        if(i > 0)
        {
            ed_name.setText("");
            Toast.makeText(this, "Thêm Thành Công", Toast.LENGTH_SHORT).show();

        }else
        {
            Toast.makeText(this, "Thêm Không Thành Công", Toast.LENGTH_SHORT).show();

        }
        db.db.close();

    }
    //sua
    private void Edit() {

        DataBase db = new DataBase(AddCategory.this);
        db.db = openOrCreateDatabase(db.getDatabaseName(),MODE_PRIVATE,null);
//        // them du lieu vo
        ContentValues values = new ContentValues();
        values.put(Table.CategoriesTable.COLUMN_NAME, "" + ed_name.getText().toString());
        long i =  db.db.update(Table.CategoriesTable.TABLE_NAME, values,"_id = ?",new String[]{"" + ID});

        if(i > 0)
        {
            ed_name.setText("");
            Toast.makeText(this, "Chỉnh Sửa Thành Công", Toast.LENGTH_SHORT).show();
            tabhost.Adaptercategory.notifyDataSetChanged();
        }else
        {
            Toast.makeText(this, "Chỉnh Sửa Không Thành Công", Toast.LENGTH_SHORT).show();

        }
        db.db.close();

    }
    @Override
    protected void onResume() {
        super.onResume();
        Intent i = getIntent();
        String a = i.getStringExtra("add");
        if(i != null)
        {
            if(a.equals("2"))
            {

                Category category = (Category) i.getSerializableExtra("category");
                 ID = category.getId();
                ed_name.setText(category.getName());
                btn_add.setText("Sửa");
            }
        }

    }
}
