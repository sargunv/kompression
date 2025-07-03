package dev.sargunv.kompression.zlib

import kotlinx.io.Buffer
import kotlinx.io.RawSource
import kotlinx.io.Source

public actual class DeflaterSource actual constructor(private val source: Source, level: Int) :
  RawSource {
  private val deflater = Deflater(level)
  private var closed = false

  actual override fun close() {
    if (closed) return
    closed = true
    deflater.close()
    source.close()
  }

  actual override fun readAtMostTo(sink: Buffer, byteCount: Long): Long {
    if (closed) error("already closed")
    if (source.exhausted()) return -1L
    return deflater.transfer(source = source, sink = sink, maxReadBytes = byteCount)
  }
}
