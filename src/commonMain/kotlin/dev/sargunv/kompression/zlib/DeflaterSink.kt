package dev.sargunv.kompression.zlib

import kotlinx.io.Buffer
import kotlinx.io.RawSink
import kotlinx.io.Sink

public expect class DeflaterSink(sink: Sink, level: Int = 6) : RawSink {
  override fun close()

  override fun flush()

  override fun write(source: Buffer, byteCount: Long)
}
