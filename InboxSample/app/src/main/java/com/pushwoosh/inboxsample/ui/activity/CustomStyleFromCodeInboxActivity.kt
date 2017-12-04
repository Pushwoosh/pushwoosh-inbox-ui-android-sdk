package com.pushwoosh.inboxsample.ui.activity

import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.pushwoosh.inbox.ui.PushwooshInboxStyle
import com.pushwoosh.inbox.ui.presentation.view.activity.InboxActivity
import com.pushwoosh.inboxsample.R

class CustomStyleFromResourceInboxActivity : InboxActivity()

class CustomStyleFromCodeInboxActivity : InboxActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        PushwooshInboxStyle.clearColors()
        PushwooshInboxStyle.accentColor = ContextCompat.getColor(this, R.color.sampleAccentColor)
        PushwooshInboxStyle.backgroundColor = ContextCompat.getColor(this, R.color.sampleBackgroundColor)
        PushwooshInboxStyle.highlightColor = ContextCompat.getColor(this, R.color.sampleHighlightColor)
        PushwooshInboxStyle.imageTypeColor = ContextCompat.getColor(this, R.color.sampleImageTypeColor)
        PushwooshInboxStyle.readImageTypeColor = ContextCompat.getColor(this, R.color.sampleReadImageTypeColor)
        PushwooshInboxStyle.titleColor = ContextCompat.getColor(this, R.color.sampleTitleColor)
        PushwooshInboxStyle.readTitleColor = ContextCompat.getColor(this, R.color.sampleReadTitleColor)
        PushwooshInboxStyle.descriptionColor = ContextCompat.getColor(this, R.color.sampleDescriptionColor)
        PushwooshInboxStyle.readDescriptionColor = ContextCompat.getColor(this, R.color.sampleReadDescriptionColor)
        PushwooshInboxStyle.dateColor = ContextCompat.getColor(this, R.color.sampleDateColor)
        PushwooshInboxStyle.readDateColor = ContextCompat.getColor(this, R.color.sampleReadDateColor)
        PushwooshInboxStyle.dateColor = ContextCompat.getColor(this, R.color.sampleDividerColor)
        PushwooshInboxStyle.defaultImageIcon = R.drawable.ic_account_circle_black_24dp
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        PushwooshInboxStyle.clearColors()
    }
}