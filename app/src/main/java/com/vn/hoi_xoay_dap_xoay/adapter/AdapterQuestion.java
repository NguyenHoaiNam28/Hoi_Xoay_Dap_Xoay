package com.vn.hoi_xoay_dap_xoay.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vn.hoi_xoay_dap_xoay.R;
import com.vn.hoi_xoay_dap_xoay.model.Question;

import java.util.List;

public class AdapterQuestion extends ArrayAdapter {
    Activity context;
    int resource;
    @NonNull List objects;
    public AdapterQuestion(@NonNull Activity context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View View, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View = inflater.inflate(this.resource,null);
        TextView tv_question = View.findViewById(R.id.tv_question);
        TextView tv_opt1 = View.findViewById(R.id.tv_opt1);
        TextView tv_opt2 = View.findViewById(R.id.tv_opt2);
        TextView tv_opt3 = View.findViewById(R.id.tv_opt3);
        TextView tv_opt4 = View.findViewById(R.id.tv_opt4);
        TextView answer = View.findViewById(R.id.tv_answer);

        Question question = (Question) this.objects.get(position);
        tv_question.setText(question.getQuestion());
        tv_opt1.setText(question.getOption1());
        tv_opt2.setText(question.getOption2());
        tv_opt3.setText(question.getOption3());
        tv_opt4.setText(question.getOption4());
        answer.setText( "Câu Trả Lời Đúng " + String.valueOf(question.getAnswer()));

        return View;
    }
}
