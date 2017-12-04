package com.pushwoosh.inboxsample.ui.fragment

import android.os.Bundle
import android.view.View
import com.pushwoosh.inbox.ui.presentation.view.fragment.InboxFragment
import com.pushwoosh.inbox.PushwooshInbox
import com.pushwoosh.inbox.event.InboxMessagesUpdatedEvent
import com.pushwoosh.internal.event.EventBus
import com.pushwoosh.internal.event.Subscription

class InboxExtensionFragment: InboxFragment(){

    private var subscription: Subscription<InboxMessagesUpdatedEvent>? = null
    private var unreadMessagesCount: Int = -1
    private var totalMessageCount: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscription = EventBus.subscribe(InboxMessagesUpdatedEvent::class.java, {
            PushwooshInbox.messagesWithNoActionPerformedCount {
                unreadMessagesCount = it.data ?: -1
                updateTitle()
            }

            PushwooshInbox.messagesCount {
                totalMessageCount = it.data ?: -1
                updateTitle()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        subscription?.unsubscribe()
    }

    private fun updateTitle() {
        if (unreadMessagesCount != -1 && totalMessageCount != -1) {
            activity?.title = "$unreadMessagesCount/$totalMessageCount"
        }
    }
}