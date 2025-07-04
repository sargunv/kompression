package dev.sargunv.kompression.deflate

import kotlinx.io.Buffer
import kotlinx.io.Source
import kotlinx.io.writeString

fun String.asSource(): Source =
  Buffer().apply {
    writeString(this@asSource)
    flush()
  }

fun ByteArray.asSource(): Source =
  Buffer().apply {
    write(this@asSource)
    flush()
  }
