package dev.sargunv.kompression.deflate

import dev.sargunv.kompression.zlib.Flush
import dev.sargunv.kompression.zlib.ResultCode
import dev.sargunv.kompression.zlib.ZStream
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.usePinned

@OptIn(ExperimentalForeignApi::class)
public actual class Deflater actual constructor(level: Int, nowrap: Boolean) : StreamingProcessor {
  private val buffer = ByteArray(8192)
  private val nativeDeflater = ZStream()

  init {
    nativeDeflater.deflateInit(windowBits = if (nowrap) -15 else 15, level = level)
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
        nativeDeflater.setInput(pinnedInput, startIndex, endIndex - startIndex)
        do {
          nativeDeflater.setOutput(pinnedOutput, 0, buffer.size)

          val ret = nativeDeflater.deflate(if (finish) Flush.Finish else Flush.None)
          val ended =
            when (ret) {
              ResultCode.NonFatal.Ok -> false
              ResultCode.NonFatal.StreamEnd -> true
              ResultCode.NonFatal.NeedDict -> TODO("preset dictionary")
            }

          val bytesWritten = buffer.size - nativeDeflater.outputBytesAvailable.toInt()
          onOutput(buffer, 0, bytesWritten)

          val outputFilled = nativeDeflater.outputBytesAvailable == 0u
          val inputEmpty = nativeDeflater.inputBytesAvailable == 0u
        } while ((outputFilled || !inputEmpty) && !ended)
      }
    }
  }

  actual override fun close() {
    nativeDeflater.deflateEnd()
    nativeDeflater.close()
  }
}
