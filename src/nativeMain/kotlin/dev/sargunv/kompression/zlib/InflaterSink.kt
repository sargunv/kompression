package dev.sargunv.kompression.zlib

import kotlinx.io.Buffer
import kotlinx.io.RawSink
import kotlinx.io.Sink

public actual class InflaterSink actual constructor(private val sink: Sink) : RawSink {
  private val inflater = Inflater()
  private var closed = false

  actual override fun close() {
    if (closed) return
    inflater.close()
    sink.close()
    closed = true
  }

  actual override fun flush() {
    if (closed) return
    sink.flush()
  }

  actual override fun write(source: Buffer, byteCount: Long) {
    if (closed) error("already closed")
    inflater.inflate(source, sink, maxReadBytes = byteCount)
  }
}
