package com.github.minhnguyen31093.twitsplit.activity

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatEditText
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.github.minhnguyen31093.twitsplit.R
import com.github.minhnguyen31093.twitsplit.fragment.ChatFragment
import java.util.*


class MainActivity : AppCompatActivity() {

    private var exitApp: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().add(R.id.frlMain, ChatFragment()).commit()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (view != null && (view is AppCompatEditText || view is EditText)) {
                val outRect = Rect()
                view.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    view.clearFocus()
                    val imm = (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onBackPressed() {
        if (exitApp == 1) {
            finishAffinity()
        } else {
            Toast.makeText(this, "Pressed back again to exit!", Toast.LENGTH_SHORT).show()
            exitApp = 1
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    exitApp = 0
                }
            }, 3000)
        }
    }
}