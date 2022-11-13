package com.vn.hoi_xoay_dap_xoay;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vn.hoi_xoay_dap_xoay.adapter.AdapterQuestion;
import com.vn.hoi_xoay_dap_xoay.model.Category;
import com.vn.hoi_xoay_dap_xoay.model.Question;

import java.util.ArrayList;
import java.util.List;

public class tabhost extends AppCompatActivity {
    TabHost tabHost;
    boolean ClickQuestion ,Clickcategory;
    TextView tv_size;

    Button btn_addquestion,btn_addcategory;
    ArrayList<Question> questions;
    public static  ArrayAdapter<Category> Adaptercategory;
    List<Category> categoryList;
    ListView lv_question,lv_category;
    Question question;
    Category category;
   public static AdapterQuestion adapterQuestion;
    int ID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabhost);
        Addcontrols();
        AddEvents();

    }

    private void AddEvents() {
        btn_addquestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(tabhost.this,AddQuestion.class);
                i.putExtra("add","1");
                startActivity(i);
            }
        });
        btn_addcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(tabhost.this,AddCategory.class);
                i.putExtra("add","1");
                startActivity(i);
            }
        });

        ChangList();

    }

    private void ChangList() {

        lv_question.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                question = questions.get(i);
                ID = questions.get(i).getId();
                ClickQuestion = true;
                Clickcategory = false;
                return false;
            }
        });
        lv_category.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                category = categoryList.get(i);
                ID = categoryList.get(i).getId();
                ClickQuestion = false;
                Clickcategory = true;
                return false;
            }
        });
    }

    private void GetListCategory() {
        DataBase dataBase = new DataBase(this);
        categoryList = dataBase.getDataCategories();
        Adaptercategory = new ArrayAdapter<Category>(this, android.R.layout.simple_list_item_1,categoryList);
        lv_category.setAdapter(Adaptercategory);
    }
    public void GetListquestion() {
        DataBase dataBase = new DataBase(this);
        questions = dataBase.getQuestions();
        tv_size.setText("" +questions.size() );
        adapterQuestion = new AdapterQuestion(this,R.layout.items,questions);
        lv_question.setAdapter(adapterQuestion);
        adapterQuestion.notifyDataSetChanged();
    }

    private boolean Check()
    {
        boolean check  = false;

            for (int j = 1; j <questions.size() ; j++) {
                if(questions.get(j).getCategoryID() == ID)
                {
                    check = true;
                }



        }

        return  check;
    }
    private void Addcontrols() {

        questions = new ArrayList<>();
        categoryList = new ArrayList<>();
        tv_size = findViewById(R.id.tv_size);
        btn_addquestion = findViewById(R.id.btn_addquestion);
        btn_addcategory = findViewById(R.id.btn_addcategory);
        lv_category = findViewById(R.id.lv_listcategory);
        lv_question = findViewById(R.id.lv_listquestion);
        tabHost =  findViewById(R.id.TabModels);
        tabHost.setup();
        //
        TabHost.TabSpec spec1;
        TabHost.TabSpec spec2;


        //tab listnhac
        spec1 = tabHost.newTabSpec("Câu Hỏi");
        spec1.setContent(R.id.tab1);
//        spec1.setIndicator("", getResources().getDrawable(R.drawable.musc));
        spec1.setIndicator("Câu Hỏi");
        tabHost.addTab(spec1);

        //
        spec2 = tabHost.newTabSpec("Danh Mục");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("Danh Mục");

//        spec2.setIndicator("", getResources().getDrawable(R.drawable.heartred));
        tabHost.addTab(spec2);
        GetListCategory();
        GetListquestion();

        //
        registerForContextMenu(lv_question);
        registerForContextMenu(lv_category);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.contexmenu,menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.btn_sua)
        {
            if(ClickQuestion == true)
            {
                Intent i = new Intent(tabhost.this,AddQuestion.class);
                i.putExtra("add","2");
                i.putExtra("question",question);
                startActivity(i);
            }
            if(Clickcategory == true)
            {
                Intent i = new Intent(tabhost.this,AddCategory.class);
                i.putExtra("add","2");
                i.putExtra("category",category);
                startActivity(i);
            }
        }
        if(item.getItemId() == R.id.btn_xoa)
        {
            if(ClickQuestion == true)
            {
                DataBase db = new DataBase(tabhost.this);
                db.db = openOrCreateDatabase(db.getDatabaseName(),MODE_PRIVATE,null);
                long  i =  db.db.delete(Table.QuestionsTable.TABLE_NAME,"_id = ?",new String[]{"" + ID});

                if(i > 0)
                {
                    Toast.makeText(this, "Xoa Thành Công", Toast.LENGTH_SHORT).show();
                    GetListquestion();
                }
                else
                {
                    Toast.makeText(this, "xóa Thất Bại", Toast.LENGTH_SHORT).show();
                }
                db.db.close();
            }
            if(Clickcategory == true)
            {
                boolean check = Check();
                if(check == true)
                {
                    Toast.makeText(this, "Danh muc dda ton tai cau hoi", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    DataBase db = new DataBase(tabhost.this);
                    db.db = openOrCreateDatabase(db.getDatabaseName(),MODE_PRIVATE,null);
                    long  i =  db.db.delete(Table.CategoriesTable.TABLE_NAME,"_id = ?",new String[]{"" + ID});
                    if(i > 0)
                    {


                        Toast.makeText(this, "Xoa Thành Công", Toast.LENGTH_SHORT).show();
                        GetListCategory();

                    }
                    else
                    {
                        Toast.makeText(this, "xóa Thất Bại", Toast.LENGTH_SHORT).show();
                    }
                    db.db.close();

                }

            }

        }
        return super.onContextItemSelected(item);
    }
}
