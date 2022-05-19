package com.thor.storyapp.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Patterns
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.thor.storyapp.R


class MyButton : AppCompatButton {

    private lateinit var myBackground: Drawable

    private var txtColor: Int = 0

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
        background = myBackground
        setTextColor(txtColor)
        textSize = 12f
        gravity = Gravity.CENTER
    }

    private fun init() {
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        myBackground = ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
    }

    fun validateEmailPassword(email: String, password: String):Boolean = emailIsValid(email) && passwordIsValid(password.length)

    private fun emailIsValid(etMail: String): Boolean =
        when {
            etMail.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(etMail)
                .matches()
            ->
                true
            else -> {
                Toast.makeText(context, "Enter valid Email address !", Toast.LENGTH_SHORT).show()
                false
            }
        }

    private fun passwordIsValid(passwordLength: Int): Boolean =
        when {
            passwordLength >= 6 -> true
            passwordLength == 0 -> {
                Toast.makeText(context, "Password Is Empty !", Toast.LENGTH_SHORT).show()
                false
            }
            else -> {
                Toast.makeText(context, "Enter Valid Password !", Toast.LENGTH_SHORT).show()
                false
            }
        }


}