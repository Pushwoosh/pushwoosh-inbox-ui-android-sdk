package com.pushwoosh.inboxsample.ui.activity

import android.os.Bundle
import android.support.design.internal.BottomNavigationItemView
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.pushwoosh.inbox.PushwooshInbox
import com.pushwoosh.inbox.event.InboxMessagesUpdatedEvent
import com.pushwoosh.inboxsample.ui.fragment.InboxExtensionFragment
import com.pushwoosh.inboxsample.R
import com.pushwoosh.inboxsample.ui.fragment.TextFragment
import com.pushwoosh.internal.event.EventBus
import com.pushwoosh.internal.event.Subscription
import kotlinx.android.synthetic.main.activity_inbox_bottom.*
import kotlinx.android.synthetic.main.notification_badge.view.*


class InboxBottomActivity : AppCompatActivity() {

    companion object {
        const val KEY_NAVIGATION_POSITION = "KEY_NAVIGATION_POSITION"
    }

    private var badgeTextView: TextView? = null
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { openScreen(it.itemId) }


    private var subscription: Subscription<InboxMessagesUpdatedEvent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox_bottom)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        openScreen(savedInstanceState?.getInt(KEY_NAVIGATION_POSITION) ?: R.id.navigation_home)
        addBadge()
        updateBadge()

        subscription = EventBus.subscribe(InboxMessagesUpdatedEvent::class.java, { updateBadge() })
    }

    private fun openScreen(itemId: Int) = when (itemId) {
        R.id.navigation_home -> {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameContent, TextFragment.createFragment(getString(R.string.title_home)))
                    .commitAllowingStateLoss()
            title = getString(R.string.title_home)
            true
        }
        R.id.navigation_dashboard -> {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameContent, TextFragment.createFragment(getString(R.string.title_dashboard)))
                    .commitAllowingStateLoss()
            title = getString(R.string.title_dashboard)
            true
        }
        R.id.navigation_notifications -> {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameContent, InboxExtensionFragment())
                    .commitAllowingStateLoss()
            true
        }
        else -> false
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_NAVIGATION_POSITION, navigation.selectedItemId)
    }

    private fun updateBadge() {
        PushwooshInbox.messagesWithNoActionPerformedCount { badgeTextView?.text = (it.data ?: 0).toString() }
    }

    private fun addBadge() {
        val itemView = (navigation.getChildAt(0) as ViewGroup).getChildAt(2) as BottomNavigationItemView
        val badge = LayoutInflater.from(this).inflate(R.layout.notification_badge, navigation, false)
        badgeTextView = badge.notificationsBadge
        itemView.addView(badge)
    }

    override fun onDestroy() {
        super.onDestroy()
        subscription?.unsubscribe()
    }
}
