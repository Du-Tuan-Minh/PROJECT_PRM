package com.example.project_prm.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.project_prm.R;

public class BackView extends LinearLayout {

    public interface OnBackIconClickListener {
        void onIconClick(BackView view);
    }

    private Context context;
    private ImageView backIcon;
    private TextView textBackView;
    private OnBackIconClickListener iconClickListener;

    public BackView(Context context) {
        super(context);
        init(context, null);
        this.context = context;
    }

    public BackView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        this.context = context;
    }

    private void bindingView(){
        backIcon = findViewById(R.id.backIcon);
        textBackView = findViewById(R.id.textBackView);
    }

    private void setLayout(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BackView);

        String title = a.getString(R.styleable.BackView_textBackView);
        if (title != null) {
            textBackView.setText(title);
        }
        else {
            textBackView.setVisibility(View.GONE);
        }
        a.recycle();
    }
    private void bindingAction(){
        backIcon.setOnClickListener(this::onBackIconClick);
    }

    private void onBackIconClick(View view) {
        if (iconClickListener != null) {
            iconClickListener.onIconClick(this);
        }
        else {
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) context).onBackPressed();
                }
            });
        }
    }


    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.back_view_component, this, true);
        bindingView();
        setLayout(context, attrs);
        bindingAction();
    }


    public void setContext(Context context) {
        this.context = context;
    }

    public ImageView getBackIcon() {
        return backIcon;
    }

    public void setBackIcon(ImageView backIcon) {
        this.backIcon = backIcon;
    }

    public TextView getTextBackView() {
        return textBackView;
    }

    public void setTextBackView(TextView textBackView) {
        this.textBackView = textBackView;
    }

    public OnBackIconClickListener getIconClickListener() {
        return iconClickListener;
    }

    public void setOnIconClickListener(OnBackIconClickListener iconClickListener) {
        this.iconClickListener = iconClickListener;
    }
}