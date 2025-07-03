package dev.sargunv.kompression.zlib

import kotlinx.io.Buffer
import kotlinx.io.RawSink
import kotlinx.io.Sink
import kotlinx.io.Source
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.khronos.webgl.set
import kotlin.js.json

public actual fun Source.inflateTo(sink: Sink) {
  transferTo(InflatingSink(sink))
}

public actual fun Source.deflateTo(sink: Sink, level: Int) {
  transferTo(DeflatingSink(sink, level))
}

private fun handleError(status: Int): Int {
  return when (status) {
    Pako.Constants.Z_OK,
    Pako.Constants.Z_STREAM_END -> status
    Pako.Constants.Z_NEED_DICT -> error("Z_NEED_DICT")
    Pako.Constants.Z_ERRNO -> error("Z_ERRNO")
    Pako.Constants.Z_STREAM_ERROR -> error("Z_STREAM_ERROR")
    Pako.Constants.Z_DATA_ERROR -> error("Z_DATA_ERROR")
    Pako.Constants.Z_BUF_ERROR -> error("Z_BUF_ERROR")
    else -> error("Pako error: $status")
  }
}

internal class InflatingSink(sink: Sink) : RawSink {
  private val inflater = StreamingInflater(sink)

  override fun write(source: Buffer, byteCount: Long) {
    if (byteCount < 0) error("Cannot inflate zero or negative byte count: $byteCount")
    var buf = Uint8Array(ArrayBuffer(byteCount.toInt()))
    for (i in 0 until byteCount.toInt()) {
      if (source.exhausted()) {
        buf = buf.subarray(0, i)
        break
      }
      buf[i] = source.readByte()
    }
    inflater.push(
      buf,
      if (source.exhausted()) Pako.Constants.Z_FINISH else Pako.Constants.Z_NO_FLUSH,
    )
  }

  override fun flush() {}

  override fun close() {
    inflater.push(Uint8Array(0), Pako.Constants.Z_FINISH)
  }
}

internal class DeflatingSink(sink: Sink, level: Int) : RawSink {
  private val deflater = StreamingDeflater(sink, level)

  override fun write(source: Buffer, byteCount: Long) {
    if (byteCount < 0) error("Cannot deflate zero or negative byte count: $byteCount")
    var buf = Uint8Array(ArrayBuffer(byteCount.toInt()))
    for (i in 0 until byteCount.toInt()) {
      if (source.exhausted()) {
        buf = buf.subarray(0, i)
        break
      }
      buf[i] = source.readByte()
    }
    deflater.push(
      buf,
      if (source.exhausted()) Pako.Constants.Z_FINISH else Pako.Constants.Z_NO_FLUSH,
    )
  }

  override fun flush() {}

  override fun close() {
    deflater.push(Uint8Array(0), Pako.Constants.Z_FINISH)
  }
}

internal class StreamingInflater(private val sink: Sink) : Pako.Inflate(js("({})")) {
  override fun onData(chunk: Uint8Array) {
    for (i in 0 until chunk.length) sink.writeByte(chunk[i])
  }

  override fun onEnd(status: Int) {
    val status = handleError(status)
    if (status != Pako.Constants.Z_OK) {
      error("Inflater ended with status: $status")
    }
    sink.close()
  }
}

internal class StreamingDeflater(private val sink: Sink, level: Int) :
  Pako.Deflate(json("level" to level).unsafeCast<Pako.DeflateOptions>()) {
  override fun onData(chunk: Uint8Array) {
    for (i in 0 until chunk.length) sink.writeByte(chunk[i])
  }

  override fun onEnd(status: Int) {
    val status = handleError(status)
    if (status != Pako.Constants.Z_OK) {
      error("Deflater ended with status: $status")
    }
    sink.close()
  }
}
