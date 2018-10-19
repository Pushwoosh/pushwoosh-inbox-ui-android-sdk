package com.pushwoosh.inbox.ui.model.repository

import com.pushwoosh.inbox.data.InboxMessage
import com.pushwoosh.inbox.event.InboxMessagesUpdatedEvent
import com.pushwoosh.inbox.internal.data.InboxMessageImpl
import com.pushwoosh.internal.event.EventBus
import org.junit.*

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.powermock.reflect.Whitebox
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLooper

//@RunWith(RobolectricTestRunner::class)
class InboxRepositoryImplTest {

    lateinit var inboxRepository: InboxRepositoryImpl

    private var callbacks = mutableListOf<(InboxEvent) -> Unit>()
    private var currentInboxMessages: MutableList<InboxMessage> = ArrayList()

    @Mock
    private lateinit var inboxMessageMock : InboxMessage

    @Mock
    private lateinit var callback: (InboxEvent) -> Unit



    @Mock
    private lateinit var inboxMessagesUpdatedEvent : InboxMessagesUpdatedEvent
    @Mock
    private lateinit var inboxMessageAdd : InboxMessage
    @Mock
    private lateinit var inboxMessageUpdate : InboxMessage


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        inboxRepository = InboxRepositoryImpl()

        callbacks = Whitebox.getInternalState(inboxRepository, "callbacks")
        currentInboxMessages = Whitebox.getInternalState(inboxRepository, "currentInboxMessages")
    }

    fun setInboxEvent(inboxEvent: InboxEvent?) {
        Whitebox.setInternalState(inboxRepository, "currentInboxEvent", inboxEvent)
    }

    @After
    fun tearDown() {
    }


    @Test
    fun addCallbackShouldAddCallBackInListAndInvokeInboxEvent() {
        val inboxEvent = InboxEvent.InboxEmpty()
        setInboxEvent(inboxEvent)

        inboxRepository.addCallback(callback)

        Assert.assertEquals(callback, callbacks.get(0))
        verify(callback).invoke(inboxEvent)
    }

    @Test
    fun removeCallback() {
        callbacks.add(callback)
        inboxRepository.removeCallback(callback)
        Assert.assertEquals(0, callbacks.size)
    }

    @Test
    @Ignore
    fun subscribeToEvent() {
        inboxRepository.subscribeToEvent()

        Mockito.`when`(inboxMessagesUpdatedEvent.messagesAdded).thenReturn(mutableListOf(inboxMessageAdd))
        Mockito.`when`(inboxMessagesUpdatedEvent.messagesUpdated).thenReturn(mutableListOf(inboxMessageUpdate))
        Mockito.`when`(inboxMessagesUpdatedEvent.messagesDeleted).thenReturn(mutableListOf("1"))


        EventBus.sendEvent(inboxMessagesUpdatedEvent)
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks()

    }



}