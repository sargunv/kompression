package dev.sargunv.kompression.deflate

public interface StreamingProcessor : AutoCloseable {
  public fun push(
    input: ByteArray,
    startIndex: Int = 0,
    endIndex: Int = input.size,
    finish: Boolean,
    onOutput: (output: ByteArray, startIndex: Int, endIndex: Int) -> Unit,
  )
}
