package dev.sargunv.kompression.zlib

import kotlinx.io.Buffer
import kotlinx.io.RawSink
import kotlinx.io.Sink

public actual class DeflaterSink actual constructor(private val sink: Sink, level: Int) : RawSink {
  private val deflater = Deflater(level)
  private var closed = false

  actual override fun close() {
    if (closed) return
    closed = true
    deflater.close()
    sink.close()
  }

  actual override fun flush() {
    if (closed) return
    sink.flush()
  }

  actual override fun write(source: Buffer, byteCount: Long) {
    if (closed) error("already closed")
    deflater.transfer(source, sink, maxReadBytes = byteCount)
  }
}
