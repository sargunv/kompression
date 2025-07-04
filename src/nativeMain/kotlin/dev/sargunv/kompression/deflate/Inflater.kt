package dev.sargunv.kompression.deflate

import dev.sargunv.kompression.zlib.Flush
import dev.sargunv.kompression.zlib.ResultCode
import dev.sargunv.kompression.zlib.ZStream
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.usePinned

@OptIn(ExperimentalForeignApi::class)
public actual class Inflater actual constructor(nowrap: Boolean) : StreamingProcessor {
  private val buffer = ByteArray(8192)
  private val nativeInflater = ZStream()

  init {
    nativeInflater.inflateInit(windowBits = if (nowrap) -15 else 15)
  }

  actual override fun push(
    input: ByteArray,
    startIndex: Int,
    endIndex: Int,
    finish: Boolean,
    onOutput: (output: ByteArray, startIndex: Int, endIndex: Int) -> Unit,
  ) {
    input.usePinned { pinnedInput ->
      buffer.usePinned { pinnedOutput ->
        nativeInflater.setInput(pinnedInput, startIndex, endIndex - startIndex)
        do {
          nativeInflater.setOutput(pinnedOutput, 0, buffer.size)

          val ret = nativeInflater.inflate(if (finish) Flush.Finish else Flush.None)
          val ended =
            when (ret) {
              ResultCode.NonFatal.Ok -> false
              ResultCode.NonFatal.StreamEnd -> true
              ResultCode.NonFatal.NeedDict -> TODO("preset dictionary")
            }

          val bytesWritten = buffer.size - nativeInflater.outputBytesAvailable.toInt()
          onOutput(buffer, 0, bytesWritten)

          val outputFilled = nativeInflater.outputBytesAvailable == 0u
          val inputEmpty = nativeInflater.inputBytesAvailable == 0u
        } while ((outputFilled || !inputEmpty) && !ended)
      }
    }
  }

  actual override fun close() {
    nativeInflater.inflateEnd()
    nativeInflater.close()
  }
}
