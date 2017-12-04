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

package com.pushwoosh.inbox.ui

import android.support.annotation.AnimRes
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import com.pushwoosh.inbox.ui.model.customizing.formatter.DefaultDateFormatter
import com.pushwoosh.inbox.ui.model.customizing.formatter.InboxDateFormatter

object PushwooshInboxStyle {
    /**
     * Use this property for clearing list animation {@link #listAnimation}
     */
    const val EMPTY_ANIMATION = -1

    /**
     * Inbox message date format.
     * By default used {@link com.pushwoosh.inbox.ui.model.customizing.formatter.InboxDateFormatter#DEFAULT_DATE_FORMAT} format
     */
    var dateFormatter: InboxDateFormatter = DefaultDateFormatter()

    /**
     * Item appearing animation. Set {@link #EMPTY_ANIMATION} for clear animation
     */
    @AnimRes
    var listAnimation: Int = android.R.anim.slide_in_left

    /**
     * The icon shown near the message if there's no icon in push payload. If not specified, the app icon is used
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

    /**
     * Accent color of inbox cell. By default used AppCompat R.attr.colorAccent
     */
    @ColorRes
    var accentColor: Int? = null

    /**
     * The color of cell background. By default used Android android.R.attr.windowBackground
     */
    @ColorRes
    var backgroundColor: Int? = null
    /**
     * The color of cell highlight. By default used AppCompat R.attr.colorControlHighlight
     */
    @ColorRes
    var highlightColor: Int? = null

    /**
     * The color of the unread message action icon (Deep Link, URL, etc.). By default used {@link #accentColor}
     */
    @ColorRes
    var imageTypeColor: Int? = null
    /**
     * The color of the read message action icon. By default used {@link #readDateColor}
     */
    @ColorRes
    var readImageTypeColor: Int? = null

    /**
     * The title color of unread messages. By default used Android android.R.attr.textColorPrimary
     */
    @ColorRes
    var titleColor: Int? = null
    /**
     * The title color of read messages. By default used Android android.R.attr.textColorSecondary
     */
    @ColorRes
    var readTitleColor: Int? = null

    /**
     * The text color of unread messages. By default used Android android.R.attr.textColorSecondary
     */
    @ColorRes
    var descriptionColor: Int? = null
    /**
     * The text color of read messages. By default used Android android.R.attr.textColorSecondary
     */
    @ColorRes
    var readDescriptionColor: Int? = null

    /**
     * The date color of unread messages. By default used Android android.R.attr.textColorSecondary
     */
    @ColorRes
    var dateColor: Int? = null
    /**
     * The date color of read messages. By default used Android android.R.attr.textColorSecondary
     */
    @ColorRes
    var readDateColor: Int? = null
    /**
     * The divider color. By default used Android android.R.attr.listDivider
     */
    @ColorRes
    var dividerColor: Int? = null

    /**
     * Clear all setting colors
     */
    fun clearColors() {
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