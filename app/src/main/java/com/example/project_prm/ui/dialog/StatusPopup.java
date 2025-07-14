package com.example.project_prm.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.project_prm.R;

public class StatusPopup extends AppCompatActivity {
    private Context context;
    private final Dialog dialog;
    private final LinearLayout iconContainer;
    private final TextView titleText;
    private final TextView messageText;
    private final Button primaryButton;
    private final Button cancelButton;

    public StatusPopup(Activity activity) {
        context = activity;
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_status_popup);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        iconContainer = dialog.findViewById(R.id.iconContainer);
        titleText = dialog.findViewById(R.id.titleText);
        messageText = dialog.findViewById(R.id.messageText);
        primaryButton = dialog.findViewById(R.id.primaryButton);
        cancelButton = dialog.findViewById(R.id.cancelButton);

        // init event dialog here
        setPrimaryClick(s -> dismiss());
        setCancelClick(s -> dismiss());
    }

    @SuppressLint("ResourceAsColor")
    public void setSuccessPopup(String title, String message, String primaryText) {
        iconContainer.setBackgroundResource(R.drawable.ic_success_dialog);
        titleText.setText(title);
        titleText.setTextColor(ContextCompat.getColor(context, R.color.blue_dialog));
        messageText.setText(message);
        primaryButton.setText(primaryText);
    }

    @SuppressLint("ResourceAsColor")
    public void setErrorPopup(String title, String message, String primaryText) {
        iconContainer.setBackgroundResource(R.drawable.ic_fail_dialog);
        titleText.setText(title);
        titleText.setTextColor(R.color.red_dialog);
        messageText.setText(message);
        primaryButton.setText(primaryText);
    }

    @SuppressLint("ResourceAsColor")
    public void  setCustomPopup(int icon, String title, int color,String message, String primaryText){
        iconContainer.setBackgroundResource(icon);
        titleText.setText(title);
        titleText.setTextColor(color);
        messageText.setText(message);
        primaryButton.setText(primaryText);
    }

    public void setPrimaryClick(View.OnClickListener listener) {
        primaryButton.setOnClickListener(listener);
    }

    public void setCancelClick(View.OnClickListener listener) {
        cancelButton.setOnClickListener(listener);
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public void hiddenCancelButton(){
        cancelButton.setVisibility(View.GONE);
    }

    public void displayCancelButton(){
        cancelButton.setVisibility(View.VISIBLE);
    }
}

