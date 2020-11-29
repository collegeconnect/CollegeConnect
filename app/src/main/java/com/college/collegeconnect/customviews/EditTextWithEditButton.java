package com.college.collegeconnect.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.college.collegeconnect.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class EditTextWithEditButton extends ConstraintLayout {

    public enum InputType {NORMAL_TEXT, EMAIL_ADDRESS, NUMBER, PASSWORD}

    TextInputLayout textInputLayout;
    TextInputEditText editText;
    TextView edit;

    public EditTextWithEditButton(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public EditTextWithEditButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EditTextWithEditButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.edittext_with_edit_button, this);
        textInputLayout = findViewById(R.id.text_input_layout);
        editText = findViewById(R.id.text_input_edit_text);
        edit = findViewById(R.id.edit);

        edit.setOnClickListener(view -> {
            if (editText.isEnabled()) {
                disable();
            } else {
                enable();
            }
        });

        if (attrs == null) return;

        TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.EditTextWithEditButton, 0, 0);

        String hint = attrsArray.getString(R.styleable.EditTextWithEditButton_hint);
        if (hint != null) textInputLayout.setHint(hint);

        InputType inputType = InputType.values()[attrsArray.getInt(R.styleable.EditTextWithEditButton_inputType, 0)];
        setEditTextInputType(inputType);

        attrsArray.recycle();
    }

    private void setEditTextInputType(InputType inputType) {
        Log.d("Input Type", inputType.toString());
        switch (inputType) {
            case NORMAL_TEXT:
                editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
                break;
            case EMAIL_ADDRESS:
                editText.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
            case NUMBER:
                editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
                break;
            case PASSWORD:
                editText.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                break;
        }
    }

    public Editable getText() {
        return editText.getText();
    }

    public void setText(String value) {
        editText.setText(value);
    }

    public void enable() {
        editText.setEnabled(true);
        editText.requestFocus();
        showKeyboard();
        editText.setSelection(editText.getText().length());
        edit.setText(getContext().getString(R.string.done));
    }

    public void disable() {
        editText.setEnabled(false);
        edit.setText(getContext().getString(R.string.edit));
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

}
