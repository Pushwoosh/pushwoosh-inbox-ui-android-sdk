/*
 *
 * Copyright (c) 2017. Pushwoosh Inc. (http://www.pushwoosh.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * (i) the original and/or modified Software should be used exclusively to work with Pushwoosh services,
 *
 * (ii) the above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.pubshwoosh.inbox.ui.presentation.presenter

import android.os.Bundle
import com.pubshwoosh.inbox.ui.PushwooshInboxStyle
import com.pubshwoosh.inbox.ui.model.repository.InboxEvent
import com.pubshwoosh.inbox.ui.model.repository.InboxRepository
import com.pubshwoosh.inbox.ui.presentation.data.UserError
import com.pubshwoosh.inbox.ui.presentation.lifecycle.Lifecycle
import com.pushwoosh.inbox.PushwooshInbox
import com.pushwoosh.inbox.data.InboxMessage
import com.pushwoosh.inbox.event.InboxMessagesUpdatedEvent
import com.pushwoosh.internal.event.Subscription
import java.lang.ref.WeakReference
import java.util.*

class InboxPresenter(inboxView: InboxView) : BasePresenter() {

    companion object {
        private const val KEY_SWIPE_REFRESH = "KEY_SWIPE_REFRESH"
        private const val KEY_COLLECTION = "KEY_COLLECTION"
    }

    private val inboxViewRef: WeakReference<InboxView> = WeakReference(inboxView)

    private var inboxEvent: InboxEvent? = null
    private var swipeToRefresh = false
    private val messageList = ArrayList<InboxMessage>()
    private var subscription: Subscription<InboxMessagesUpdatedEvent>? = null

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        subscription = InboxRepository.subscribeToEvent()

        InboxRepository.callback = { inboxEvent ->
            this.inboxEvent = inboxEvent
            implementState()
        }
        InboxRepository.loadInbox(forceRequest = !restore)
    }

    override fun restoreState(bundle: Bundle) {
        swipeToRefresh = bundle.getBoolean(KEY_SWIPE_REFRESH, swipeToRefresh)
        messageList.clear()
        @Suppress("UNCHECKED_CAST")
        messageList.addAll(bundle.getSerializable(KEY_COLLECTION) as ArrayList<out InboxMessage>)
    }

    override fun onViewCreated() {
        super.onViewCreated()
        if (!messageList.isEmpty() && restore) {
            inboxViewRef.get()?.showList(messageList)
        }
        implementState()
    }

    private fun implementState() {
        if (!viewEnable) {
            return
        }

        val localInboxEvent = inboxEvent
        when (localInboxEvent) {
            is InboxEvent.Loading -> if (!swipeToRefresh) inboxViewRef.get()?.showTotalProgress() else inboxViewRef.get()?.showSwipeRefreshProgress()
            is InboxEvent.FinishLoading -> inboxViewRef.get()?.hideProgress()
            is InboxEvent.FailedLoading -> {
                inboxViewRef.get()?.failedLoadingInboxList(UserError(message = PushwooshInboxStyle.listErrorMessage))
                swipeToRefresh = false
            }
            is InboxEvent.SuccessLoading -> {
                messageList.clear()
                messageList.addAll(localInboxEvent.inboxMessages)

                showList()
            }
            is InboxEvent.InboxEmpty -> {
                messageList.clear()
                inboxViewRef.get()?.showEmptyView()
            }
            is InboxEvent.InboxMessagesUpdated -> {
                messageList.removeAll(localInboxEvent.deleted)
                messageList.addAll(localInboxEvent.addedInboxMessages)

                localInboxEvent.updatedInboxMessages
                        .forEach {
                            if (messageList.contains(it)) {
                                messageList[messageList.indexOf(it)] = it
                            } else {
                                messageList.add(it)
                            }
                        }

                showList()
            }
        }
    }

    private fun showList() {
        Collections.sort(messageList, { i1, i2 ->
            return@sort i2.compareTo(i1)
        })

        inboxViewRef.get()?.showList(messageList)
    }

    override fun onSaveInstanceState(out: Bundle) {
        out.putBoolean(KEY_SWIPE_REFRESH, swipeToRefresh)
        out.putSerializable(KEY_COLLECTION, messageList)
    }

    override fun onDestroy(isFinished: Boolean) {
        subscription?.unsubscribe()
        InboxRepository.callback = null
    }

    fun removeItem(inboxMessage: InboxMessage?) {
        if (inboxMessage != null) {
            messageList.remove(inboxMessage)
            InboxRepository.removeItem(inboxMessage)
        }
    }

    fun refreshItems() {
        swipeToRefresh = true
        InboxRepository.loadInbox(true)
    }

    fun onItemClick(inboxMessage: InboxMessage) {
        PushwooshInbox.performAction(inboxMessage.code)
    }
}

interface InboxView : Lifecycle {
    fun showSwipeRefreshProgress()
    fun showTotalProgress()
    fun hideProgress()
    fun failedLoadingInboxList(userError: UserError)
    fun showList(inboxList: Collection<InboxMessage>)
    fun showEmptyView()
}
