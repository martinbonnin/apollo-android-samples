package com.perf

import android.app.Activity
import android.os.Bundle
import android.os.Debug
import android.os.Trace
import com.squareup.moshi.Moshi
import okio.Buffer
import okio.ByteString
import okio.ByteString.Companion.encodeUtf8
import okio.buffer
import okio.source
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

class MainActivity : Activity() {

    @ExperimentalTime
    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val byteString = this.resources.openRawResource(R.raw.response).source().buffer().readByteString()
        val times = 100
        measureTime {
            repeat(times) {
                val operation = GetStuffQuery()
                val data = operation.parse(byteString)
            }
        }.also {
            println("Benchmark Apollo ${it / times}")
        }

        val json = byteString.utf8()
        measureTime {
            repeat(times) {
                val adapter = Moshi.Builder().build().adapter(Data::class.java)
                val data = adapter.fromJson(json)
            }
        }.also {
            println("Benchmark Moshi  ${it / times}")
        }
    }
}