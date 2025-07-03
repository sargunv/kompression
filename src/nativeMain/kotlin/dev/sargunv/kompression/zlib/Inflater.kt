package dev.sargunv.kompression.zlib

internal class Inflater(chunkSize: Int = 8192) : ZStreamProcess(chunkSize = chunkSize) {
  init {
    zStream.inflateInit()
  }

  override fun process(sourceExhausted: Boolean) = zStream.inflate(ZStream.Flush.None)
}
