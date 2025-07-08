package com.example.project_prm.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import com.example.project_prm.utils.DimensionUtils;

import com.example.project_prm.R;


public class EditTextFieldView extends LinearLayout {
    public interface OnEndIconClickListener {
        void onIconClick(EditTextFieldView view);
    }
    private OnEndIconClickListener iconClickListener;
    private LinearLayout layoutTemplate;
    private ImageView startIcon;
    private EditText inputField;
    private  ImageView endIcon;

    public EditTextFieldView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void bindingView(){
        startIcon = findViewById(R.id.startIcon);
        endIcon = findViewById(R.id.endIcon);
        inputField = findViewById(R.id.inputField);
        layoutTemplate = findViewById(R.id.inputLayout);
    }

    private void setLayout(Context context, AttributeSet attrs){
        // Handle XML attributes
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditTextFieldView);

        Drawable icon = a.getDrawable(R.styleable.EditTextFieldView_startIconSrc);
        if (icon != null) {
            startIcon.setImageDrawable(icon);
        }
        else {
            startIcon.setVisibility(View.GONE);
        }
        icon = a.getDrawable(R.styleable.EditTextFieldView_endIconSrc);
        if (icon != null) {
            endIcon.setImageDrawable(icon);
        }
        else {
            endIcon.setVisibility(View.GONE);
        }
        String hint = a.getString(R.styleable.EditTextFieldView_hintText);
        if (hint != null) {
            inputField.setHint(hint);
        }

        int type = a.getInt(R.styleable.EditTextFieldView_inputType, 1);
        switch (type) {
            case 2:
                inputField.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
            case 3:
                inputField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case 4:
                inputField.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
                break;
            case 5:
                inputField.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                break;
            default:
                inputField.setInputType(InputType.TYPE_CLASS_TEXT);
        }

        a.recycle();
    }

    private void bindingAction(){
        inputField.setOnFocusChangeListener(this::onInputFocusChangeListener);
        endIcon.setOnClickListener(this::onEndIconClick);
    }

    private void onEndIconClick(View view) {
        if (iconClickListener != null) {
            iconClickListener.onIconClick(this);
        }
    }

    private void onInputFocusChangeListener(View view, boolean hasFocus) {
        int strokeColor = ContextCompat.getColor(getContext(),
                hasFocus ? R.color.primary_blue : R.color.transparent);
        GradientDrawable bg = (GradientDrawable) layoutTemplate.getBackground();
        bg.setStroke(DimensionUtils.dpToPx(getResources(),2), strokeColor);

    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.edit_text_component, this, true);
        bindingView();
        setLayout(context, attrs);
        bindingAction();
    }


    public void setOnEndIconClickListener(OnEndIconClickListener listener) {
        this.iconClickListener = listener;
    }

    public EditText getEditText() {
        return inputField;
    }

    public String getFieldText() {
        return inputField.getText().toString();
    }

    public ImageView getEndIcon() {
        return endIcon;
    }

    public void setEndIcon(ImageView endIcon) {
        this.endIcon = endIcon;
    }

    public ImageView getStartIcon() {
        return startIcon;
    }

    public void setStartIcon(ImageView startIcon) {
        this.startIcon = startIcon;
    }
}
