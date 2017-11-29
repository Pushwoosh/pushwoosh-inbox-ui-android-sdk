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

package com.pubshwoosh.inbox.ui.presentation.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.pubshwoosh.inbox.ui.PushwooshInboxStyle
import com.pubshwoosh.inbox.ui.R
import com.pubshwoosh.inbox.ui.presentation.data.UserError
import com.pubshwoosh.inbox.ui.presentation.presenter.InboxPresenter
import com.pubshwoosh.inbox.ui.presentation.presenter.InboxView
import com.pubshwoosh.inbox.ui.presentation.view.adapter.SimpleItemTouchHelperCallback
import com.pubshwoosh.inbox.ui.presentation.view.adapter.inbox.InboxAdapter
import com.pubshwoosh.inbox.ui.presentation.view.style.ColorSchemeProvider
import com.pubshwoosh.inbox.ui.presentation.view.style.ColorSchemeProviderFactory
import com.pushwoosh.inbox.data.InboxMessage
import com.pushwoosh.internal.utils.PWLog
import kotlinx.android.synthetic.main.pw_fragment_inbox.*


open class InboxFragment : BaseFragment(), InboxView {

    private lateinit var inboxAdapter: InboxAdapter
    private val inboxPresenter: InboxPresenter
    private var callback: SimpleItemTouchHelperCallback? = null
    private lateinit var colorSchemeProvider: ColorSchemeProvider

    init {
        inboxPresenter = InboxPresenter(this).apply { addLifecycleListener(this) }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        colorSchemeProvider = ColorSchemeProviderFactory.generateColorScheme(context)
        inboxAdapter = InboxAdapter(context, colorSchemeProvider)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = LayoutInflater.from(context).inflate(R.layout.pw_fragment_inbox, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inboxAdapter.onItemRemoved = { inboxMessage ->
            if (inboxAdapter.itemCount == 0) {
                showEmptyView()
            }

            inboxSwipeRefreshLayout.isEnabled = true
            inboxPresenter.removeItem(inboxMessage)
        }
        inboxAdapter.onItemStartSwipe = { inboxSwipeRefreshLayout.isEnabled = false }
        inboxAdapter.onItemStopSwipe = { inboxSwipeRefreshLayout.isEnabled = true }
        inboxAdapter.onItemClick = { inboxMessage -> if(inboxMessage!=null) inboxPresenter.onItemClick(inboxMessage) }

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        inboxRecyclerView.layoutManager = layoutManager
        inboxRecyclerView.adapter = inboxAdapter

        val divider = colorSchemeProvider.divider
        if (divider != null) {
            val dividerItemDecoration = DividerItemDecoration(context, layoutManager.orientation)
            dividerItemDecoration.setDrawable(divider)
            inboxRecyclerView.addItemDecoration(dividerItemDecoration)
        }

        callback = SimpleItemTouchHelperCallback(adapter = inboxAdapter, context = context, colorSchemeProvider = colorSchemeProvider)
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(inboxRecyclerView)

        inboxSwipeRefreshLayout.setOnRefreshListener {
            inboxPresenter.refreshItems()
            callback?.setTouchable(false)
        }

        inboxErrorImageView.setImageResource(PushwooshInboxStyle.listErrorImage)
        inboxEmptyImageView.setImageResource(PushwooshInboxStyle.listEmptyImage)
    }

    override fun showSwipeRefreshProgress() {
        inboxSwipeRefreshLayout.isRefreshing = true
    }

    override fun showTotalProgress() {
        updateContent(showProgress = true)
    }

    override fun hideProgress() {
        updateContent(showProgress = false, swipeRefresh = false)
        callback?.setTouchable(true)
    }

    override fun failedLoadingInboxList(userError: UserError) {
        val localView = view

        if (localView != null) {
            if (inboxAdapter.itemCount != 0) {
                if (TextUtils.isEmpty(userError.message)) {
                    return
                }
                Snackbar.make(localView, userError.message!!, Snackbar.LENGTH_LONG)
                        .show()
            } else {
                updateContent(isError = true)
                updateMessageTextView(inboxErrorTextView, userError.message)
            }
        }
    }

    override fun showList(inboxList: Collection<InboxMessage>) {
        inboxAdapter.setCollection(inboxList)
        updateContent(isEmpty = false)
    }

    override fun showEmptyView() {
        updateContent(isEmpty = true)
        inboxSwipeRefreshLayout.isEnabled = true
        updateMessageTextView(inboxEmptyTextView, PushwooshInboxStyle.listEmptyText)

    }

    private fun updateContent(showProgress: Boolean = false, isEmpty: Boolean = false, swipeRefresh: Boolean = false, isError: Boolean = false) {
        val contentVisibility = if (showProgress && !swipeRefresh) View.GONE else View.VISIBLE
        val totalProgressVisibility = if (showProgress && !swipeRefresh) View.VISIBLE else View.GONE

        if (isEmpty) {
            inboxRecyclerView.visibility = View.GONE
            inboxEmpty.visibility = contentVisibility
            inboxError.visibility = View.GONE
        } else if (!isError) {
            inboxRecyclerView.visibility = contentVisibility
            inboxEmpty.visibility = View.GONE
            inboxError.visibility = View.GONE
        } else {
            inboxError.visibility = contentVisibility
            inboxEmpty.visibility = View.GONE
            inboxRecyclerView.visibility = View.GONE
        }

        inboxTotalProgressBar.visibility = totalProgressVisibility
        inboxSwipeRefreshLayout.isRefreshing = swipeRefresh
    }

    private fun updateMessageTextView(messageTextView: TextView, message: CharSequence?) {
        if (TextUtils.isEmpty(message)) {
            messageTextView.visibility = View.GONE
        } else {
            messageTextView.visibility = View.VISIBLE
            messageTextView.text = message
        }
    }
}