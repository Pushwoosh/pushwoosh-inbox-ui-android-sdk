package com.pushwoosh.inboxsample.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.pubshwoosh.inbox.ui.presentation.view.activity.InboxActivity
import com.pushwoosh.inboxsample.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        simpleInboxActivityButton.setOnClickListener { startActivity(Intent(this, InboxActivity::class.java)) }
        styleInboxButton.setOnClickListener { startActivity(Intent(this, CustomStyleFromResourceInboxActivity::class.java)) }
        codeInboxButton.setOnClickListener { startActivity(Intent(this, CustomStyleFromCodeInboxActivity::class.java)) }
        bottomBarInboxButton.setOnClickListener { startActivity(Intent(this, InboxBottomActivity::class.java)) }
    }
}
