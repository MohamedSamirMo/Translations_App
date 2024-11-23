package com.GlobalTongue.translationapp

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.airbnb.lottie.LottieAnimationView


class splashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        // تغيير لون شريط التنقل السفلي على الأجهزة التي تعمل بنظام Android 10 أو أحدث
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.navigationBarColor = Color.WHITE // تغيير لون شريط التنقل السفلي
            window.statusBarColor = Color.WHITE // تغيير لون شريط الحالة

            window.decorView.systemUiVisibility =
                window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

        }
        val lottieAnimationView = findViewById<LottieAnimationView>(R.id.lottie)
        lottieAnimationView.setSpeed(2.0f) // تضبط السرعة إلى الضعف
        lottieAnimationView.playAnimation() // تشغيل الأنيميشن
        val lottieAnimationView2 = findViewById<LottieAnimationView>(R.id.lottie2)
        lottieAnimationView2.setSpeed(2.0f) // تضبط السرعة إلى الضعف
        lottieAnimationView2.playAnimation() // تشغيل الأنيميشن
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        },6000)
    }
}