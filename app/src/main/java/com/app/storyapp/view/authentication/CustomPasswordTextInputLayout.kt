package com.app.storyapp.view.authentication

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class CustomPasswordTextInputLayout : AppCompatEditText {

    private var passwordLengthListener: PasswordLengthListener? = null

    interface PasswordLengthListener {
        fun onPasswordLengthValid()
        fun onPasswordLengthInvalid()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No implementation needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No implementation needed
            }

            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                if (password.length >= 8) {
                    passwordLengthListener?.onPasswordLengthValid()
                } else {
                    passwordLengthListener?.onPasswordLengthInvalid()
                }
            }
        })
    }

    fun setPasswordLengthListener(listener: PasswordLengthListener) {
        passwordLengthListener = listener
    }
}
