package com.thor.storyapp.customview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.google.android.material.textfield.TextInputLayout


class MyEditPasswordLayout : TextInputLayout {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "Masukkan password Anda"
        textAlignment = View.TEXT_ALIGNMENT_TEXT_START
    }


    private fun init() {
        endIconMode = END_ICON_PASSWORD_TOGGLE
    }

    fun errorPassword(password: String){
        if(password.length in 1 until 6) error = "Password anda masih kurang dari 6 huruf" else isErrorEnabled = false
    }
}
