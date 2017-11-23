package com.pushwoosh.inboxsample

import android.app.Application
import com.pushwoosh.Pushwoosh

class App: Application(){
    override fun onCreate() {
        super.onCreate()
        Pushwoosh.getInstance().registerForPushNotifications()
    }
}