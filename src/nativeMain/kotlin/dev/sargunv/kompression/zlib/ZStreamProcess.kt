package dev.sargunv.kompression.zlib

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.usePinned
import kotlinx.io.Sink
import kotlinx.io.Source
import platform.zlib.Z_NEED_DICT
import platform.zlib.Z_OK
import platform.zlib.Z_STREAM_END

public actual fun Source.inflateTo(sink: Sink): Unit =
  withZStreamTo(
    sink,
    init = { inflateInit() },
    iterate = { inflate(ZStream.Flush.None) },
    deinit = { inflateEnd() },
  )

public actual fun Source.deflateTo(sink: Sink, level: Int): Unit =
  withZStreamTo(
    sink,
    init = { deflateInit(level) },
    iterate = { deflate(ZStream.Flush.None) },
    finish = { deflate(ZStream.Flush.Finish) },
    deinit = { deflateEnd() },
  )

@OptIn(ExperimentalForeignApi::class)
internal fun Source.withZStreamTo(
  sink: Sink,
  init: ZStream.() -> Unit,
  iterate: ZStream.() -> Int,
  finish: ZStream.() -> Int = iterate,
  deinit: ZStream.() -> Unit,
  chunkSize: Int = 8192,
) {
  val zStream = ZStream().apply(init)
  val input = ByteArray(chunkSize)
  val output = ByteArray(chunkSize)

  var ended = false

  input.usePinned { pinnedInput ->
    output.usePinned { pinnedOutput ->
      outer@ while (!ended) {
        // Read more data into the input buffer if needed
        if (zStream.inputBytesAvailable == 0u) {
          val bytesRead = readAtMostTo(sink = input, endIndex = input.size).coerceAtLeast(0)
          zStream.setInput(pinnedInput, available = bytesRead)
        }

        // Inflate and flush the data
        do {
          zStream.setOutput(pinnedOutput, available = output.size)

          val ret = if (exhausted()) zStream.finish() else zStream.iterate()
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

  zStream.deinit()
}
