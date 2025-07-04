package dev.sargunv.kompression.deflate

public actual class Inflater actual constructor(nowrap: Boolean) : StreamingProcessor {
  private val buffer = ByteArray(8192)
  private val javaInflater = java.util.zip.Inflater(nowrap)

  actual override fun push(
    input: ByteArray,
    startIndex: Int,
    endIndex: Int,
    finish: Boolean,
    onOutput: (output: ByteArray, startIndex: Int, endIndex: Int) -> Unit,
  ) {
    javaInflater.setInput(input, startIndex, endIndex - startIndex)
    do {
      val numOutput = javaInflater.inflate(buffer, 0, buffer.size)
      onOutput(buffer, 0, numOutput)
    } while (numOutput > 0 && !javaInflater.finished() && !javaInflater.needsInput())
    if (javaInflater.needsDictionary()) TODO("preset dictionary")
  }

  actual override fun close() {
    javaInflater.end()
  }
}
