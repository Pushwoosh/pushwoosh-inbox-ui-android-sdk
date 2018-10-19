package com.pushwoosh.inbox.ui.presentation.presenter

import android.os.Bundle
import com.pushwoosh.inbox.data.InboxMessage
import com.pushwoosh.inbox.event.InboxMessagesUpdatedEvent
import com.pushwoosh.inbox.ui.model.repository.InboxEvent
import com.pushwoosh.inbox.ui.model.repository.InboxRepository
import com.pushwoosh.inbox.ui.model.repository.InboxRepositoryImpl
import com.pushwoosh.inbox.ui.presentation.data.UserError
import com.pushwoosh.internal.event.Subscription
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.*
import org.mockito.Matchers.*
import org.mockito.Mockito.*
import org.powermock.reflect.Whitebox
import org.robolectric.RobolectricTestRunner
import java.io.Serializable
import java.util.ArrayList
import kotlin.concurrent.timer

@RunWith(RobolectricTestRunner::class)
class InboxPresenterTest {

    @Mock
    lateinit var inboxRepository: InboxRepository
    @Mock
    lateinit var inboxView: InboxView
    @Mock
    lateinit var inboxMessage: InboxMessage
    @Mock
    var subscription: Subscription<InboxMessagesUpdatedEvent>? = null
    @Mock
    lateinit var inboxMessage1: InboxMessage
    @Mock
    lateinit var inboxMessage2: InboxMessage
    @Captor
    lateinit var userErrorCaptor: ArgumentCaptor<UserError>


    lateinit var inboxPresenter: InboxPresenter


    private lateinit var arrayList: ArrayList<InboxMessage>

    private lateinit var callback: (InboxEvent) -> Unit

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        inboxPresenter = InboxPresenter(inboxView, inboxRepository)

        arrayList = ArrayList()
        arrayList.add(inboxMessage1)
        arrayList.add(inboxMessage2)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun removeItemIfArgumentIsNullShouldNotCallRemoveInboxMessageFromRepository() {
        inboxPresenter.removeItem(null)
        verify(inboxRepository, never()).removeItem(inboxMessage)
    }

    @Test
    fun removeItemShouldRemoveInboxMessageFromRepository() {
        inboxPresenter.removeItem(inboxMessage)
        verify(inboxRepository).removeItem(inboxMessage)
    }

    @Test
    fun refreshItemsShouldLoadInboxTrueAnd() {
        inboxPresenter.refreshItems()

        val swipeToRefresh = Whitebox.getInternalState(inboxPresenter, "swipeToRefresh") as Boolean
        Assert.assertEquals(true, swipeToRefresh)
        verify(inboxRepository).loadInbox(eq(true))
    }

    @Test
    fun onCreate() {
        Mockito.`when`(inboxRepository.subscribeToEvent()).thenReturn(subscription)

        val bundle = Bundle()

        bundle.putSerializable("KEY_COLLECTION", arrayList as Serializable)

        inboxPresenter.onCreate(bundle)

        val subscription = Whitebox.getInternalState(inboxPresenter, "subscription") as Subscription<InboxMessagesUpdatedEvent>?
        Assert.assertNotNull(subscription)
        verify(inboxRepository).subscribeToEvent()
        verify(inboxRepository).loadInbox(false)
        // todo fix commented code
        // verify(inboxRepository).addCallback(any())
        val list = Whitebox.getInternalState(inboxPresenter, "messageList") as ArrayList<InboxMessage>
        Assert.assertNotNull(list)
        Assert.assertEquals(2, list.size)
        Assert.assertEquals(inboxMessage1, list[0])
        Assert.assertEquals(inboxMessage2, list[1])
    }

    @Test
    fun onCreateBundleIsNull() {
        Mockito.`when`(inboxRepository.subscribeToEvent()).thenReturn(subscription)

        inboxPresenter.onCreate(null)

        val subscription = Whitebox.getInternalState(inboxPresenter, "subscription") as Subscription<InboxMessagesUpdatedEvent>?
        Assert.assertNotNull(subscription)
        verify(inboxRepository).subscribeToEvent()
        verify(inboxRepository).loadInbox(true)
        // todo fix commented code
        // verify(inboxRepository).addCallback(any())
    }

    @Test
    fun onViewCreatedShouldNotCallShowListIfRestoreFalse() {
        Whitebox.setInternalState(inboxPresenter,"restore", false)
        Whitebox.setInternalState(inboxPresenter,"messageList", arrayList)

        inboxPresenter.onViewCreated()

        verify(inboxView, never()).showList(arrayList)
    }

    @Test
    fun onViewCreatedShouldCallShowListIfRestoreTrue() {
        Whitebox.setInternalState(inboxPresenter,"restore", true)
        Whitebox.setInternalState(inboxPresenter,"messageList", arrayList)

        inboxPresenter.onViewCreated()

        verify(inboxView).showList(arrayList)
    }

    @Test
    fun onViewCreatedShouldHideProgressIfInboxEventIsFinishLoud() {
        Whitebox.setInternalState(inboxPresenter,"restore", true)
        Whitebox.setInternalState(inboxPresenter,"messageList", arrayList)
        Whitebox.setInternalState(inboxPresenter,"inboxEvent", InboxEvent.FinishLoading())

        inboxPresenter.onViewCreated()

        verify(inboxView).hideProgress()
    }

    @Test
    fun onViewCreatedShouldCallFailedLoadingInboxListIfInboxEventIsFailedLoading() {
        Whitebox.setInternalState(inboxPresenter,"restore", true)
        Whitebox.setInternalState(inboxPresenter,"messageList", arrayList)
        val testError = Throwable("test exception")
        Whitebox.setInternalState(inboxPresenter,"inboxEvent", InboxEvent.FailedLoading(testError))

        inboxPresenter.onViewCreated()

        verify(inboxView).failedLoadingInboxList(UserError(message = "It seems something went wrong. Please try again later!"))
    }

    @Test
    fun onViewCreatedShouldShowEmptyViewIfInboxEventIsInboxEmpty() {
        Whitebox.setInternalState(inboxPresenter,"restore", true)
        Whitebox.setInternalState(inboxPresenter,"messageList", arrayList)
        Whitebox.setInternalState(inboxPresenter,"inboxEvent", InboxEvent.InboxEmpty())

        inboxPresenter.onViewCreated()

        verify(inboxView).showEmptyView()
    }

    @Test
    fun onViewCreatedShouldShowEmptyViewIfInboxEventIsInboxMessagesUpdatedRemove() {
        Whitebox.setInternalState(inboxPresenter,"restore", true)
        Whitebox.setInternalState(inboxPresenter,"messageList", arrayList)
        val inboxMessagesUpdated = InboxEvent.InboxMessagesUpdated(ArrayList(),ArrayList(), arrayList)
        Whitebox.setInternalState(inboxPresenter,"inboxEvent", inboxMessagesUpdated)

        inboxPresenter.onViewCreated()
        //todo should not call two times
        verify(inboxView, times(2)).showList(ArrayList())
    }

    @Test
    fun onViewCreatedShouldShowEmptyViewIfInboxEventIsInboxMessagesUpdatedUpdate() {
        Whitebox.setInternalState(inboxPresenter,"restore", true)
        Whitebox.setInternalState(inboxPresenter,"messageList", arrayList)
        val inboxMessagesUpdated = InboxEvent.InboxMessagesUpdated(ArrayList(),arrayList,ArrayList())
        Whitebox.setInternalState(inboxPresenter,"inboxEvent", inboxMessagesUpdated)

        inboxPresenter.onViewCreated()
        //todo should not call two times
        verify(inboxView, times(2)).showList(arrayList)
    }

    @Test
    fun onViewCreatedShouldShowEmptyViewIfInboxEventIsInboxMessagesUpdatedAdd() {
        Whitebox.setInternalState(inboxPresenter,"restore", true)
        val arrayListEmpty = ArrayList<Any>()
        Whitebox.setInternalState(inboxPresenter,"messageList", arrayListEmpty)
        val inboxMessagesUpdated = InboxEvent.InboxMessagesUpdated(arrayList,ArrayList(),ArrayList())
        Whitebox.setInternalState(inboxPresenter,"inboxEvent", inboxMessagesUpdated)

        inboxPresenter.onViewCreated()

        verify(inboxView).showList(arrayList)
    }

    @Test
    fun onViewCreatedShouldShowEmptyViewIfInboxEventIsSuccessLoading() {
        Whitebox.setInternalState(inboxPresenter,"restore", true)
        val arrayListEmpty = ArrayList<Any>()
        Whitebox.setInternalState(inboxPresenter,"messageList", arrayListEmpty)
        val inboxMessagesUpdated = InboxEvent.SuccessLoading(arrayList)
        Whitebox.setInternalState(inboxPresenter,"inboxEvent", inboxMessagesUpdated)

        inboxPresenter.onViewCreated()

        verify(inboxView).showList(arrayList)
    }



    @Test
    fun onViewDestroy() {
        Whitebox.setInternalState(inboxPresenter, "viewEnable", true)
        inboxPresenter.onViewDestroy()
        Assert.assertEquals(false, Whitebox.getInternalState(inboxPresenter, "viewEnable"))
    }

    @Test
    fun onDestroy() {
        Whitebox.setInternalState(inboxPresenter, "subscription", subscription)
        // todo fix commented code
        //  Whitebox.setInternalState(inboxPresenter, "callback", callback)
        inboxPresenter.onDestroy(true)

        verify(subscription)?.unsubscribe()
        //   verify(inboxRepository).removeCallback(callback)


    }


    @Test
    fun onSaveInstanceState() {
        val bundle = Bundle()
        Whitebox.setInternalState(inboxPresenter, "swipeToRefresh", true)
        Whitebox.setInternalState(inboxPresenter, "messageList", arrayList)

        inboxPresenter.onSaveInstanceState(bundle)

        Assert.assertEquals(true, bundle.getBoolean("KEY_SWIPE_REFRESH"))
        Assert.assertEquals(arrayList, bundle.getSerializable("KEY_COLLECTION") as ArrayList<*>)

    }

}