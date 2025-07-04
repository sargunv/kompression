package dev.sargunv.kompression.deflate

import dev.sargunv.kompression.pako.Pako
import dev.sargunv.kompression.pako.StreamingPakoInflater
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.khronos.webgl.set

public actual class Inflater actual constructor(nowrap: Boolean) : StreamingProcessor {
  private val pako = StreamingPakoInflater { windowBits = if (nowrap) -15 else 15 }

  actual override fun push(
    input: ByteArray,
    startIndex: Int,
    endIndex: Int,
    finish: Boolean,
    onOutput: (output: ByteArray, startIndex: Int, endIndex: Int) -> Unit,
  ) {
    val inputArray = Uint8Array(endIndex - startIndex)
    for (i in startIndex until endIndex) inputArray[i - startIndex] = input[i]

    pako.dataHandler = { chunk ->
      val outputArray = ByteArray(chunk.length)
      for (i in 0 until chunk.length) outputArray[i] = chunk[i]
      onOutput(outputArray, 0, outputArray.size)
    }
    pako.endHandler = {}

    pako.push(inputArray, if (finish) Pako.Constants.Z_FINISH else Pako.Constants.Z_NO_FLUSH)
  }

  actual override fun close() {
    pako.push(Uint8Array(0), Pako.Constants.Z_FINISH)
  }
}
