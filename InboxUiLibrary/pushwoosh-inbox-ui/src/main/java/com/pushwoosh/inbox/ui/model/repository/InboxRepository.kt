package com.pushwoosh.inbox.ui.model.repository

import com.pushwoosh.inbox.data.InboxMessage
import com.pushwoosh.inbox.event.InboxMessagesUpdatedEvent
import com.pushwoosh.internal.event.Subscription

interface InboxRepository {

    fun removeCallback(callback: (InboxEvent) -> Unit)

    fun addCallback(callback: (InboxEvent) -> Unit)

    fun subscribeToEvent(): Subscription<InboxMessagesUpdatedEvent>

    fun loadInbox(forceRequest: Boolean)

    fun removeItem(inboxMessage: InboxMessage)

}