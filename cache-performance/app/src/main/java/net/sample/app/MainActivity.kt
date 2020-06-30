package net.sample.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.sample.lib.Cache

class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val start = System.currentTimeMillis()

        Cache.plentyOfWrites(this, 100_000_000)
        println("CachePerf: ${System.currentTimeMillis() - start} millis")
    }
}