package dev.sargunv.kompression.zlib

internal class Deflater(level: Int, chunkSize: Int = 8192) : ZStreamProcess(chunkSize = chunkSize) {
  init {
    zStream.deflateInit(level)
  }

  override fun process(sourceExhausted: Boolean): Int {
    val flush = if (sourceExhausted) ZStream.Flush.Finish else ZStream.Flush.None
    return zStream.deflate(flush)
  }
}
