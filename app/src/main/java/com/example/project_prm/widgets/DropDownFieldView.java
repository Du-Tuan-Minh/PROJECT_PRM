package com.example.project_prm.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.project_prm.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class DropDownFieldView extends LinearLayout {
    private ImageView startDropIcon;
    private ImageView endDropIcon;
    private AutoCompleteTextView dropdownField;
    private TextInputLayout dropdownItems;
    private OnItemSelectedListener itemSelectedListener;

    public interface OnItemSelectedListener {
        void onItemSelected(String item, int position);
    }

    public DropDownFieldView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public DropDownFieldView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DropDownFieldView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public TextInputLayout getDropdownItems() {
        return dropdownItems;
    }

    public void setDropdownItems(TextInputLayout dropdownItems) {
        this.dropdownItems = dropdownItems;
    }

    private void bindingView(){
        startDropIcon = findViewById(R.id.startDropIcon);
        endDropIcon = findViewById(R.id.endDropIcon);
        dropdownField = findViewById(R.id.dropdownField);
        dropdownItems = findViewById(R.id.dropdownItems);
    }

    private void setLayout(Context context, AttributeSet attrs){
        // Parse custom attributes
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DropDownFieldView, 0, 0);
        try {
            // Dropdown hint
            String dropdownHint = typedArray.getString(R.styleable.DropDownFieldView_dropdownHint);
            if (dropdownHint != null) {
                dropdownItems.setHint(dropdownHint);
                dropdownItems.setHintEnabled(true);
            }

            // Dropdown prompt
            String dropdownPrompt = typedArray.getString(R.styleable.DropDownFieldView_dropdownPrompt);
            if (dropdownPrompt != null) {
                dropdownField.setContentDescription(dropdownPrompt); // For accessibility
            }

            // Start icon
            int startIconResId = typedArray.getResourceId(R.styleable.DropDownFieldView_startDropIconSrc, 0);
            if (startIconResId != 0) {
                startDropIcon.setImageResource(startIconResId);
            }
            else{
                startDropIcon.setVisibility(View.GONE);
            }

            // End icon
            int endIconResId = typedArray.getResourceId(
                    R.styleable.DropDownFieldView_endDropIconSrc,
                    R.drawable.ic_dropdown_arrow
            );
            if (endIconResId != 0) {
                endDropIcon.setImageResource(endIconResId);
            }
            else{
                endDropIcon.setVisibility(View.GONE);
            }

            // Dropdown items
            int dropdownItemsResId = typedArray.getResourceId(R.styleable.DropDownFieldView_dropdownItems, 0);
            if (dropdownItemsResId != 0) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                        context,
                        dropdownItemsResId,
                        R.layout.item_dropdown_option
                );
                dropdownField.setAdapter(adapter);
            }
        } finally {
            typedArray.recycle();
        }
    }

    private void bindingAction(){
        dropdownField.setOnItemClickListener(this::onItemSelectedListener);
        endDropIcon.setOnClickListener(this::onEndIconClick);
    }

    private void onItemSelectedListener(AdapterView<?> adapterView, View view, int position, long id) {
        if (itemSelectedListener != null) {
            itemSelectedListener.onItemSelected(adapterView.getItemAtPosition(position).toString(), position);
        }
    }

    private void onEndIconClick(View view) {
        dropdownField.showDropDown();
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        // Inflate the layout
        LayoutInflater.from(context).inflate(R.layout.dropdown_field_component, this, true);
        bindingView();
        setLayout(context, attrs);
        bindingAction();
    }

    // Method to set dropdown items programmatically
    public void setDropdownItems(List<String> items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_dropdown_item_1line,
                items
        );
        dropdownField.setAdapter(adapter);
    }

    // Method to get the selected item
    public String getSelectedItem() {
        return dropdownField.getText() != null ? dropdownField.getText().toString() : null;
    }

    // Method to set the dropdown hint
    public void setDropdownHint(String hint) {
        dropdownItems.setHint(hint);
    }

    // Method to set the dropdown prompt
    public void setDropdownPrompt(String prompt) {
        dropdownField.setContentDescription(prompt);
    }

    // Method to set the start icon
    public void setStartIcon(@DrawableRes int drawableResId) {
        startDropIcon.setImageResource(drawableResId);
    }

    // Method to set the end icon
    public void setEndIcon(@DrawableRes int drawableResId) {
        endDropIcon.setImageResource(drawableResId);
    }

    // Method to set the item selection listener
    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.itemSelectedListener = listener;
    }
}
