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

package com.pubshwoosh.inbox.ui.presentation.view.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.TypedValue
import com.pubshwoosh.inbox.ui.R
import com.pubshwoosh.inbox.ui.presentation.view.style.ColorSchemeProvider
import com.pubshwoosh.inbox.ui.utils.getBitmap
import com.pubshwoosh.inbox.ui.utils.pxFromDp


class SimpleItemTouchHelperCallback(private val adapter: ItemTouchHelperAdapter,
                                    context: Context,
                                    colorSchemeProvider: ColorSchemeProvider,
                                    @DrawableRes swipeIcon: Int = R.drawable.inbox_ic_delete) : ItemTouchHelper.Callback() {
    private var touchable = true
    private val icon = getBitmap(context, swipeIcon)
    private val paint = Paint()

    init {
        paint.color = colorSchemeProvider.accentColor
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.onItemSwiped(viewHolder.adapterPosition)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        when (actionState) {
            ItemTouchHelper.ACTION_STATE_SWIPE -> {
                updateState(dX, dY, isCurrentlyActive, viewHolder)

                val itemView = viewHolder.itemView
                val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                val leftMargin = itemView.context.pxFromDp(20f)

                val topMarigin = if (height > icon.height) {
                    height / 2 - icon.height / 2f
                } else {
                    height / 3
                }

                if (dX > 0) {
                    val background = RectF(itemView.left.toFloat(), itemView.top.toFloat(), dX, itemView.bottom.toFloat())
                    c.drawRect(background, paint)
                    val icon_dest = RectF(itemView.left.toFloat() + leftMargin, itemView.top.toFloat() + topMarigin, itemView.left.toFloat() + icon.width + leftMargin, itemView.bottom.toFloat() - topMarigin)
                    c.drawBitmap(icon, null, icon_dest, paint)
                } else {
                    val background = RectF(itemView.right.toFloat() + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
                    c.drawRect(background, paint)
                    val icon_dest = RectF(itemView.right.toFloat() - leftMargin - icon.width, itemView.top.toFloat() + topMarigin, itemView.right.toFloat() - leftMargin, itemView.bottom.toFloat() - topMarigin)
                    c.drawBitmap(icon, null, icon_dest, paint)
                }
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun updateState(dX: Float, dY: Float, isCurrentlyActive: Boolean, viewHolder: RecyclerView.ViewHolder) {
        if (dX == 0f && dY == 0f) {
            if (isCurrentlyActive) {
                adapter.startSwipe()
            } else {
                adapter.stopSwipe()
            }
        }

        if ((dX == viewHolder.itemView.width.toFloat() || dY == viewHolder.itemView.height.toFloat()) && !isCurrentlyActive) {
            adapter.stopSwipe()
        }
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        if (!touchable) {
            return 0
        }

        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return ItemTouchHelper.Callback.makeMovementFlags(0, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    fun setTouchable(touchable: Boolean) {
        this.touchable = touchable
    }
}
