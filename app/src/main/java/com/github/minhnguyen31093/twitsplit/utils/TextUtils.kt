package com.github.minhnguyen31093.twitsplit.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.github.minhnguyen31093.twitsplit.R
import com.github.minhnguyen31093.twitsplit.models.Message
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TextUtils {

    companion object {

        const val MAX_LENGTH = 50
        const val ERROR_MESSAGE_INVALID = "ERROR_MESSAGE_INVALID"

        @SuppressLint("SimpleDateFormat")
        fun getDateTimeFromLong(milliseconds: Long): String {
            val format = SimpleDateFormat("MMM, dd/yyyy kk:mm")
            val currentTime = Date()
            currentTime.time = milliseconds
            return format.format(currentTime)
        }

        fun splitMessage(context: Context, contentMessage: String): ArrayList<Message> {
            var messages = ArrayList<Message>()
            var temp = contentMessage.replace("\n", " ").replace("\r", " ").trim()
            Log.d("Sample Text", temp)
            if (temp.isNotEmpty()) {
                if (temp.length > MAX_LENGTH) {
                    if (temp.contains(" ")) {
                        val maxPages = getMaxPages(temp.length)
                        while (temp.isNotEmpty()) {
                            temp = getSplitMessage(temp, messages, maxPages)
                            if (temp.equals(ERROR_MESSAGE_INVALID)) {
                                messages = ArrayList()
                                toastErrorInvalid(context)
                                return messages
                            }
                        }
                        messages.last().dateTime = Date().time
                    } else {
                        toastErrorInvalid(context)
                    }
                } else {
                    messages.add(Message(temp, Date().time))
                }
            } else {
                toastErrorEmpty(context)
            }

            if (messages.isNotEmpty()) {
                var len = 0
                var m = ""
                for (message in messages) {
                    len += message.content.length
                    m += message.content + "\n"
                }
                Log.d("Final Length", len.toString())
                Log.d("Final Length Message", m)
            }

            return messages
        }

        private fun toastErrorInvalid(context: Context) {
            if (context is Activity) {
                Toast.makeText(context, R.string.invalid_message, Toast.LENGTH_SHORT).show()
            } else {
                Log.w("Warning", "Your input message invalid!")
            }
        }

        private fun toastErrorEmpty(context: Context) {
            if (context is Activity) {
                Toast.makeText(context, R.string.empty_message, Toast.LENGTH_SHORT).show()
            } else {
                Log.w("Warning", "Your input message is empty!")
            }
        }

        private fun getMaxPages(currentLength: Int): Int {
            return getPages(getIncreaseLength(currentLength))
        }

        private fun getIncreaseLength(len: Int): Int {
            var count = 0
            var increasement = 10
            var newLen = len
            var i = 0
            while (i <= newLen) {
                count++
                newLen += count.toString().length * 2 + 1
                if (count == increasement) {
                    newLen += increasement
                    increasement *= 10
                }
                i += 50
            }
            return newLen
        }

        private fun getPages(len: Int): Int {
            var pages = len / MAX_LENGTH
            if (len % MAX_LENGTH != 0) {
                pages += 1
            }
            Log.d("Text Length", len.toString())
            return pages
        }

        private fun getSplitMessage(temp: String, messages: ArrayList<Message>, maxPages: Int): String {
            val currentPage = (messages.size + 1).toString() + "/" + maxPages.toString() + " "
            var content = currentPage + temp
            content = if (content.length > MAX_LENGTH) {
                if (temp.contains(" ")) {
                    val position = if (content.length == MAX_LENGTH + 1 || content.substring(MAX_LENGTH + 1, MAX_LENGTH + 2) == " ") {
                        MAX_LENGTH + 1
                    } else if (content.length < MAX_LENGTH + 1) {
                        content.length - 1
                    } else {
                        content.substring(0, MAX_LENGTH + 1).lastIndexOf(" ")
                    }
                    if (position > 0) {
                        messages.add(Message(content.substring(0, position), -1))
                        content.substring(position + 1, content.length)
                    } else {
                        ERROR_MESSAGE_INVALID
                    }
                } else {
                    ERROR_MESSAGE_INVALID
                }
            } else {
                messages.add(Message(content, 0))
                ""
            }
            return content.trim()
        }

        fun hideKeyboard(context: Context, view: View) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}