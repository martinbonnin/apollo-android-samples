package net.sample.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.sample.lib.Cache

class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GlobalScope.launch(Dispatchers.IO) {
            val start = System.currentTimeMillis()

            Cache.plentyOfWrites(this@MainActivity, 100)
            println("CachePerf: ${System.currentTimeMillis() - start} millis")
        }
    }
}