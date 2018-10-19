package com.pushwoosh.inbox.ui.presentation.view.adapter.inbox

import android.content.Context
import com.pushwoosh.inbox.data.InboxMessage
import com.pushwoosh.inbox.ui.presentation.view.style.ColorSchemeProvider
import junit.framework.TestCase
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
@Ignore
class InboxAdapterTest {

    lateinit var inboxAdapter : InboxAdapter

    lateinit var context : Context
    lateinit var colorSchemeProvider: ColorSchemeProvider

    var onItemRemoved: ((InboxMessage?) -> Unit)? = null
    var onItemStartSwipe: (() -> Unit)? = null
    var onItemStopSwipe: (() -> Unit)? = null
    var onItemClick: ((InboxMessage?) -> Unit)? = null

    @Before
    fun setUp() {
        context = Mockito.spy(RuntimeEnvironment.application)
        colorSchemeProvider = Mockito.mock(ColorSchemeProvider::class.java)

        inboxAdapter = InboxAdapter(context, colorSchemeProvider)

        colorSchemeProvider = Mockito.mock(ColorSchemeProvider::class.java)
    }

    @Before
    fun tearDown() {}

    @Test
    fun testGetOnItemRemoved() {

    }

    @Test
    fun testSetOnItemRemoved() {

    }

    @Test
    fun testGetOnItemStartSwipe() {
        inboxAdapter.startSwipe()

    }

    @Test
    fun testSetOnItemStartSwipe() {
        inboxAdapter.stopSwipe()
    }

    @Test
    fun testGetOnItemStopSwipe() {

    }

    @Test
    fun testSetOnItemStopSwipe() {}

    @Test
    fun testGetOnItemClick() {

    }

    @Test
    fun testSetOnItemClick() {}

    @Test
    fun testStartSwipe() {}

    @Test
    fun testStopSwipe() {}

    @Test
    fun testOnItemSwiped() {}

    @Test
    fun testAnimateItem() {}

    @Test
    fun testOnBindViewHolder() {}

    @Test
    fun testCreateViewHolderInstance() {}

    @Test
    fun testGetItemViewType() {}
}