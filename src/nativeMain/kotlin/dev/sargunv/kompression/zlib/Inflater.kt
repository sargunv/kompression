package dev.sargunv.kompression.zlib

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.usePinned
import kotlinx.io.Sink
import kotlinx.io.Source
import platform.zlib.Z_NEED_DICT
import platform.zlib.Z_OK
import platform.zlib.Z_STREAM_END

@OptIn(ExperimentalForeignApi::class)
internal class Inflater private constructor(private val zStream: ZStream) :
  AutoCloseable by zStream {

  private val input = ByteArray(8192)
  private val output = ByteArray(8192)

  constructor() : this(ZStream())

  init {
    zStream.inflateInit()
  }

  fun inflate(source: Source, sink: Sink, maxReadBytes: Long): Long {
    var totalBytesRead = 0L
    var ended = false

    input.usePinned { pinnedInput ->
      output.usePinned { pinnedOutput ->
        outer@ while (!ended && totalBytesRead < maxReadBytes) {
          // Read more data into the input buffer if needed
          if (zStream.inputBytesAvailable == 0u) {
            val maxRead = minOf((maxReadBytes - totalBytesRead).toInt(), input.size)
            val bytesRead = source.readAtMostTo(sink = input, endIndex = maxRead).coerceAtLeast(0)
            totalBytesRead += bytesRead
            zStream.setInput(pinnedInput, available = bytesRead)
          }

          // Inflate and flush the data
          do {
            zStream.setOutput(pinnedOutput, available = output.size)

            val ret = zStream.inflate(ZStream.Flush.None)
            when (ret) {
              Z_OK -> {}
              Z_NEED_DICT -> error("need dictionary")
              Z_STREAM_END -> {
                ended = true
              }
              else -> error("unexpected zlib return code: $ret")
            }

            val bytesWritten = output.size - zStream.outputBytesAvailable.toInt()
            sink.write(output, 0, bytesWritten)
          } while (zStream.outputBytesAvailable == 0u)
        }
      }
    }

    return totalBytesRead
  }
}
