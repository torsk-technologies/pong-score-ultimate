package com.carlosreiakvam.android.handsdowntabletennis

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
//        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        return super.onCreateView(name, context, attrs)
    }
}