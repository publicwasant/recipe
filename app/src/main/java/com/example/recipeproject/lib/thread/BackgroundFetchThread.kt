package com.example.recipeproject.lib.thread

import android.os.Handler
import android.os.Looper

class BackgroundFetchThread(): Thread() {
    private lateinit var task: (Int) -> Unit

    override fun run() {
        super.run()

        var count = 0

        while (true) {
            Thread.sleep(1000*60*5)
            Handler(Looper.getMainLooper()).post {
                this.task(count)
                count += 1
            }
        }
    }

    fun setTask(t: (Int) -> Unit) {
        this.task = t
    }
}