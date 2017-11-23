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

package com.pubshwoosh.inbox.ui.presentation.view.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.pubshwoosh.inbox.ui.R
import com.pubshwoosh.inbox.ui.presentation.view.fragment.InboxFragment

open class InboxActivity : AppCompatActivity() {

    companion object {
        const val TAG = "pushwoosh.inbox.ui.InboxActivity"
        const val FRAGMENT_TAG = TAG + ".InboxFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pw_activity_inbox)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        attachInboxFragment()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    protected open fun attachInboxFragment() {
        var needToAdd = true
        val fragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG)?.also { needToAdd = false } ?: InboxFragment()

        val beginTransaction = supportFragmentManager.beginTransaction()
        if (needToAdd) {
            beginTransaction.add(R.id.inboxContentContainer, fragment, FRAGMENT_TAG)
        } else {
            beginTransaction.attach(fragment)
        }
        beginTransaction.commitNowAllowingStateLoss()
    }
}
