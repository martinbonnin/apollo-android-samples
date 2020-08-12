package com.perf

import android.app.Activity
import android.os.Bundle
import com.squareup.moshi.Moshi
import okio.Buffer
import okio.ByteString
import okio.buffer
import okio.source
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

class MainActivity : Activity() {

  @ExperimentalTime
  @ExperimentalStdlibApi
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val operation = GetStuffQuery()

    val byteString = this.resources.openRawResource(R.raw.response).source().buffer().readByteString()
    val times = 100
    measureTime {
      repeat(times) {
        val data = operation.parse(byteString)
      }
    }.also {
      println("Benchmark Apollo ${it/times}")
    }

    val adapter = Moshi.Builder().build().adapter(Data::class.java)
    val json = byteString.utf8()
    measureTime {
      repeat(times) {
        val data = adapter.fromJson(json)
      }
    }.also {
      println("Benchmark Moshi  ${it/times}")
    }
  }
}