package com.college.collegeconnect.customviews

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.text.Editable
import android.text.InputType.*
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import com.college.collegeconnect.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class EditTextWithEditButton @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var textInputLayout: TextInputLayout
    private var editText: TextInputEditText
    private var editButton: TextView

    private var doneListener: DoneListener? = null

    init {
        inflate(context, R.layout.edittext_with_edit_button, this)

        textInputLayout = findViewById(R.id.text_input_layout)
        editText = findViewById(R.id.text_input_edit_text)
        editButton = findViewById(R.id.edit)

        editButton.setOnClickListener {
            if (editText.isEnabled) {
                doneListener?.onDonePressed()
                disable()
            } else {
                enable()
            }
        }

        context.withStyledAttributes(attrs, R.styleable.EditTextWithEditButton) {
            getString(R.styleable.EditTextWithEditButton_hint)?.let { textInputLayout.hint = it }
            setEditTextInputType(InputType.values()[getInt(R.styleable.EditTextWithEditButton_inputType, 0)])
        }
    }

    fun setDoneListener(doneListener: DoneListener) {
        this.doneListener = doneListener
    }

    private fun setEditTextInputType(inputType: InputType) {
        when(inputType) {
            InputType.NORMAL_TEXT -> editText.inputType = TYPE_CLASS_TEXT
            InputType.EMAIL_ADDRESS -> editText.inputType = TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            InputType.NUMBER -> editText.inputType = TYPE_CLASS_NUMBER
            InputType.PASSWORD -> {
                editText.inputType = TYPE_TEXT_VARIATION_PASSWORD
                editText.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
    }

    fun getText(): Editable? = editText.text

    fun setText(value: String) = editText.setText(value)

    fun enable() {
        editText.isEnabled = true
        editText.requestFocus()
        showKeyboard()
        editText.text?.length?.let { editText.setSelection(it) }
        editButton.text = context.getString(R.string.done)
    }

    fun disable() {
        editText.isEnabled = false
        editButton.text = context.getString(R.string.edit)
    }

    private fun showKeyboard() {
        val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

}

enum class InputType {
    NORMAL_TEXT,
    EMAIL_ADDRESS,
    NUMBER,
    PASSWORD
}

interface DoneListener {
    fun onDonePressed()
}