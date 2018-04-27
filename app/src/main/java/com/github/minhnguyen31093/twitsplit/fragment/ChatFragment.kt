package com.github.minhnguyen31093.twitsplit.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.github.minhnguyen31093.twitsplit.R
import com.github.minhnguyen31093.twitsplit.adapter.ChatAdapter
import com.github.minhnguyen31093.twitsplit.models.Message
import com.github.minhnguyen31093.twitsplit.utils.TextUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_chat.*


class ChatFragment : Fragment() {

    private val BUNDLE_TEXT = "BUNDLE_TEXT"
    private val BUNDLE_MESSAGES = "BUNDLE_MESSAGES"

    private var chatAdapter: ChatAdapter = ChatAdapter(ArrayList())

    private val keyboardLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        val heightDiff = cslChat.getRootView().getHeight() - cslChat.getHeight()
        val contentViewTop = activity!!.window.findViewById<View>(Window.ID_ANDROID_CONTENT).top
        if (heightDiff <= contentViewTop) {
            edtChat.clearFocus()
        } else {
            if (edtChat.isFocused && rvChat.adapter != null && rvChat.adapter.itemCount > 0) {
                (rvChat.layoutManager as LinearLayoutManager).scrollToPosition(chatAdapter.itemCount - 1)
            }
        }
    }

    private val onClickListener = View.OnClickListener {
        pbChat.visibility = View.VISIBLE
        Observable.just(TextUtils.splitMessage(requireContext(), edtChat.text.toString()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { messages: ArrayList<Message> ->
                    pbChat.visibility = View.GONE
                    TextUtils.hideKeyboard(requireContext(), edtChat)
                    chatAdapter.add(messages)
                    if (messages.isNotEmpty()) {
                        edtChat.setText("")
                        (rvChat.layoutManager as LinearLayoutManager).scrollToPosition(chatAdapter.itemCount - 1)
                    }
                }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(BUNDLE_TEXT, edtChat.text.toString())
        outState.putParcelableArrayList(BUNDLE_MESSAGES, chatAdapter.getItems())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restoreState(savedInstanceState)
        initEvent()
        rvChat.layoutManager = LinearLayoutManager(requireContext())
        rvChat.adapter = chatAdapter
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            edtChat.setText(savedInstanceState.getString(BUNDLE_TEXT, ""))
            val items = savedInstanceState.getParcelableArrayList<Message>(BUNDLE_MESSAGES)

            if (items.isEmpty()) {
                getSample()
            } else {
                chatAdapter.add(items)
            }
        } else {
            getSample()
        }
    }

    private fun initEvent() {
        cslChat.getViewTreeObserver().addOnGlobalLayoutListener(keyboardLayoutListener)
        btnSend.setOnClickListener(onClickListener)
    }

    private fun getSample() {
        val sample = "I can't believe Tweeter now supports chunking my messages, so I don't have to do it myself."
        chatAdapter.add(TextUtils.splitMessage(requireContext(), sample))
    }
}