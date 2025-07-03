package dev.sargunv.kompression.zlib

import kotlinx.io.Buffer
import kotlinx.io.RawSource
import kotlinx.io.Source

public expect class DeflaterSource(source: Source, level: Int = 6) : RawSource {
  override fun close()

  override fun readAtMostTo(sink: Buffer, byteCount: Long): Long
}
