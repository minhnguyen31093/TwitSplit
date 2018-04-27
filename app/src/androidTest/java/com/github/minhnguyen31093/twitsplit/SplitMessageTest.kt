package com.github.minhnguyen31093.twitsplit

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.github.minhnguyen31093.twitsplit.utils.TextUtils

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class SplitMessageTest {
    private lateinit var correctMessage: String
    private lateinit var incorrectMessage: String

    @org.junit.Before
    @Throws(Exception::class)
    fun setUp() {
        correctMessage = "I can't believe Tweeter now supports chunking my messages, so I don't have to do it myself."
        incorrectMessage = "Ican'tbelieveTweeternowsupportschunkingmymessages,soIdon'thavetodoitmyself."
    }

    @Test
    fun testSplitMessageCorrect() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val messages = TextUtils.splitMessage(appContext, correctMessage)
        assertEquals(messages[0].content, "1/2 I can't believe Tweeter now supports chunking")
        assertEquals(messages[1].content, "2/2 my messages, so I don't have to do it myself.")
    }

    @Test
    fun testSplitMessageInCorrect() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val messages = TextUtils.splitMessage(appContext, incorrectMessage)
        assertEquals(messages.size, 0)
    }
}
