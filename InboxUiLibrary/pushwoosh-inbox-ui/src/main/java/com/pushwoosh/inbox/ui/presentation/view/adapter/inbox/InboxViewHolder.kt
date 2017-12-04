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

package com.pushwoosh.inbox.ui.presentation.view.adapter.inbox

import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.pushwoosh.inbox.ui.R
import com.pushwoosh.inbox.ui.presentation.view.adapter.BaseRecyclerAdapter
import com.pushwoosh.inbox.ui.presentation.view.style.ColorSchemeProvider
import com.pushwoosh.inbox.ui.utils.parseToString
import com.pushwoosh.inbox.data.InboxMessage
import com.pushwoosh.inbox.data.InboxMessageType
import kotlinx.android.synthetic.main.pw_item_inbox.view.*

class InboxViewHolder(viewGroup: ViewGroup,
                      adapter: InboxAdapter,
                      private val colorSchemeProvider: ColorSchemeProvider) : BaseRecyclerAdapter.ViewHolder<InboxMessage>(R.layout.pw_item_inbox, viewGroup, adapter) {

    override fun fillView(model: InboxMessage?, position: Int) {
        if (model == null) {
            return
        }

        itemView.background = colorSchemeProvider.cellBackground
        itemView.inboxLabelTextView.setTextColor(colorSchemeProvider.titleColor)
        itemView.inboxDescriptionTextView.setTextColor(colorSchemeProvider.descriptionColor)
        itemView.inboxDateTextView.setTextColor(colorSchemeProvider.dateColor)
        itemView.inboxStatusImageView.setColorFilter(colorSchemeProvider.imageColor)

        itemView.inboxLabelTextView.visibility = if (TextUtils.isEmpty(model.title)) View.GONE else View.VISIBLE
        itemView.inboxLabelTextView.text = model.title
        itemView.inboxDescriptionTextView.text = model.message
        itemView.inboxDateTextView.text = model.sendDate.parseToString()
        itemView.inboxStatusImageView.setImageResource(model.type.getResource())

        itemView.inboxLabelTextView.isSelected = !model.isActionPerformed
        itemView.inboxStatusImageView.isSelected = itemView.inboxLabelTextView.isSelected
        itemView.inboxDateTextView.isSelected = itemView.inboxLabelTextView.isSelected
        itemView.inboxDescriptionTextView.isSelected = itemView.inboxLabelTextView.isSelected

        Glide.with(itemView)
                .clear(itemView)

        if (TextUtils.isEmpty(model.imageUrl)) {
            Glide.with(itemView.context)
                    .load(colorSchemeProvider.defaultIcon)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            itemView.inboxImageView.setImageDrawable(colorSchemeProvider.defaultIcon)
                            return true
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                    })
        } else {
            Glide.with(itemView)
                    .load(model.imageUrl)

        }.apply(RequestOptions.circleCropTransform())
                .into(itemView.inboxImageView)
    }
}

fun InboxMessageType.getResource() = when (this) {
    InboxMessageType.PLAIN -> R.drawable.inbox_ic_app_open
    InboxMessageType.RICH_MEDIA -> R.drawable.inbox_ic_rich_media
    InboxMessageType.URL -> R.drawable.inbox_ic_remote_url
    InboxMessageType.REMOTE_URL -> R.drawable.inbox_ic_remote_url
    InboxMessageType.DEEP_LINK -> R.drawable.inbox_ic_deep_link
}