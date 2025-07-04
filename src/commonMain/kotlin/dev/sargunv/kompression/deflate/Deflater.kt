package dev.sargunv.kompression.deflate

public expect class Deflater(level: Int = 6, nowrap: Boolean = false) : StreamingProcessor {
  override fun push(
    input: ByteArray,
    startIndex: Int,
    endIndex: Int,
    finish: Boolean,
    onOutput: (output: ByteArray, startIndex: Int, endIndex: Int) -> Unit,
  )

  override fun close()
}
