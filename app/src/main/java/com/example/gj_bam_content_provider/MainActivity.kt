package com.example.gj_bam_content_provider

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val readButton: Button = findViewById(R.id.readButton)
        readButton.setOnClickListener {
            val cursor = contentResolver.query(
                Uri.parse("content://com.example.gj_bam_1.provider/userData"),
                null,
                null,
                null,
                null
            )

            try {
                if (cursor!!.moveToFirst()) {
                    while (!cursor.isAfterLast) {
                        Log.d(
                            "Provider",
                            "Username: ${cursor.getString(cursor.getColumnIndex("username"))}, number: ${
                                cursor.getInt(cursor.getColumnIndex("number"))
                            }"
                        )
                        cursor.moveToNext()
                    }
                } else {
                    Log.d("Provider", "No records found")
                }
            } catch (exception: NullPointerException) {
                Log.e("Provider","Failed to connect")
            }
        }
    }
}
