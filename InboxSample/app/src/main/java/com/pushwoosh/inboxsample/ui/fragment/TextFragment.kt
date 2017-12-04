package com.pushwoosh.inboxsample.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pushwoosh.inboxsample.R
import kotlinx.android.synthetic.main.fragment_text.*

class TextFragment : Fragment() {

    companion object {
        const val KEY_MESSAGE = "messageKey"
        fun createFragment(message: String): TextFragment {
            val fragment = TextFragment()
            val bundle = Bundle(1)
            bundle.putString(KEY_MESSAGE, message)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_text, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        message.text = arguments?.getString(KEY_MESSAGE)
    }
}