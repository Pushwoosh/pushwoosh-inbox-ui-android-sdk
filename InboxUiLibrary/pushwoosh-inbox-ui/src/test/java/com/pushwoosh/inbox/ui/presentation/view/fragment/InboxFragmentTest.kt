package com.pushwoosh.inbox.ui.presentation.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pushwoosh.inbox.data.InboxMessage
import com.pushwoosh.inbox.ui.presentation.data.UserError
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
@Ignore
class InboxFragmentTest {

    private lateinit var inboxFragment : InboxFragment
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = Mockito.spy(RuntimeEnvironment.application)
        inboxFragment = InboxFragment()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun onAttach() {
        inboxFragment.onAttach(context)


    }

    @Test
    fun onCreateView() {
        val layoutInflater = Mockito.mock(LayoutInflater::class.java)
        val viewGroup = Mockito.mock(ViewGroup::class.java)
        val bundle = Mockito.mock(Bundle::class.java)

        inboxFragment.onCreateView(layoutInflater, viewGroup, bundle)
    }

    @Test
    fun onViewCreated() {
        var bundle = Bundle()
        var view = View(context)

        inboxFragment.onViewCreated(view, bundle)
    }

    @Test
    fun showSwipeRefreshProgress() {
        val permission = "permssion"
        inboxFragment.shouldShowRequestPermissionRationale(permission)
    }

    @Test
    fun showTotalProgress() {
        inboxFragment.showTotalProgress()
    }

    @Test
    fun hideProgress() {
        inboxFragment.hideProgress()
    }

    @Test
    fun failedLoadingInboxList() {
        var userError = UserError()
        inboxFragment.failedLoadingInboxList(userError)
    }

    @Test
    fun showList() {
        val inboxList: Collection<InboxMessage> =  ArrayList()
        inboxFragment.showList(inboxList)
    }

    @Test
    fun showEmptyView() {
        inboxFragment.showEmptyView()

    }
}