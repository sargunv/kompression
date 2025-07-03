package dev.sargunv.kompression.zlib

import kotlinx.io.Buffer
import kotlinx.io.RawSource
import kotlinx.io.Source

public actual class InflaterSource actual constructor(private val source: Source) : RawSource {
  private val inflater = Inflater()
  private var closed = false

  actual override fun close() {
    if (closed) return
    closed = true
    inflater.close()
    source.close()
  }

  actual override fun readAtMostTo(sink: Buffer, byteCount: Long): Long {
    if (closed) error("already closed")
    if (source.exhausted()) return -1L
    return inflater.inflate(source = source, sink = sink, maxReadBytes = byteCount)
  }
}
