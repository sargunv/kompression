package dev.sargunv.kompression.deflate

import java.util.zip.Deflater

public actual class Deflater actual constructor(level: Int, nowrap: Boolean) : StreamingProcessor {
  private val buffer = ByteArray(8192)
  private val javaDeflater = Deflater(level, nowrap)

  actual override fun push(
    input: ByteArray,
    startIndex: Int,
    endIndex: Int,
    finish: Boolean,
    onOutput: (output: ByteArray, startIndex: Int, endIndex: Int) -> Unit,
  ) {
    javaDeflater.setInput(input, startIndex, endIndex - startIndex)
    if (finish) javaDeflater.finish()
    do {
      val numOutput = javaDeflater.deflate(buffer, 0, buffer.size)
      onOutput(buffer, 0, numOutput)
    } while (numOutput > 0 || !javaDeflater.needsInput())
  }

  actual override fun close() {
    javaDeflater.end()
  }
}
