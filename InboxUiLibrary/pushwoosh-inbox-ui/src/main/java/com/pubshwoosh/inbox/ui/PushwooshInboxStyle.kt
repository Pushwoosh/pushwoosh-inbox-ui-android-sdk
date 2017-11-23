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

package com.pubshwoosh.inbox.ui

import android.support.annotation.AnimRes
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import com.pubshwoosh.inbox.ui.model.customizing.formatter.DefaultDateFormatter
import com.pubshwoosh.inbox.ui.model.customizing.formatter.InboxDateFormatter

object PushwooshInboxStyle {
    const val EMPTY_ANIMATION = -1

    /**
     * Inbox message date format
     */
    var dateFormatter: InboxDateFormatter = DefaultDateFormatter()

    /**
     * Item appearing animation
     */
    @AnimRes
    var listAnimation: Int = android.R.anim.slide_in_left

    /**
     * The default icon in the cell next to the message; if not specified, the app icon is used
     */
    @DrawableRes
    var defaultImageIcon: Int = -1

    /**
     * The image which is displayed if an error occurs and the list of inbox messages is empty
     */
    var listErrorImage = R.drawable.inbox_ic_error

    /**
     * The error text which is displayed when an error occurs
     */
    var listErrorMessage: CharSequence? = "It seems something went wrong. Please try again later!"

    /**
     * The image which is displayed if the list of inbox messages is empty
     */
    var listEmptyImage = R.drawable.inbox_ic_empty

    /**
     * The text which is displayed if the list of inbox messages is empty
     */
    var listEmptyText: CharSequence? = "Currently there are no messages in the Inbox."

    @ColorRes var accentColor: Int? = null

    @ColorRes var backgroundColor: Int? = null
    @ColorRes var highlightColor: Int? = null

    @ColorRes var imageTypeColor: Int? = null
    @ColorRes var readImageTypeColor: Int? = null

    @ColorRes var titleColor: Int? = null
    @ColorRes var readTitleColor: Int? = null

    @ColorRes var descriptionColor: Int? = null
    @ColorRes var readDescriptionColor: Int? = null

    @ColorRes var dateColor: Int? = null
    @ColorRes var readDateColor: Int? = null

    @ColorRes var dividerColor: Int? = null

    /**
     * Clear all setting colors
     */
    fun clearColors(){
        accentColor = null
        backgroundColor = null
        highlightColor = null
        imageTypeColor = null
        readImageTypeColor = null
        titleColor = null
        readTitleColor = null
        descriptionColor = null
        readDescriptionColor = null
        dateColor = null
        readDateColor = null
        dividerColor = null
    }
}