package dev.sargunv.kompression.pako

import org.khronos.webgl.Uint8Array

internal fun handlePakoError(status: Int) =
  when (status) {
    Pako.Constants.Z_OK,
    Pako.Constants.Z_NEED_DICT,
    Pako.Constants.Z_STREAM_END -> status
    Pako.Constants.Z_ERRNO -> error("Z_ERRNO")
    Pako.Constants.Z_STREAM_ERROR -> error("Z_STREAM_ERROR")
    Pako.Constants.Z_DATA_ERROR -> error("Z_DATA_ERROR")
    Pako.Constants.Z_BUF_ERROR -> error("Z_BUF_ERROR")
    else -> error("Pako error: $status")
  }

internal class StreamingPakoInflater(
  var dataHandler: ((Uint8Array) -> Unit) = {},
  var endHandler: (() -> Unit) = {},
  options: Pako.InflateOptions.() -> Unit = {},
) : Pako.Inflate(js("({})").unsafeCast<Pako.InflateOptions>().apply { options() }) {

  override fun onData(chunk: Uint8Array) {
    dataHandler(chunk)
  }

  override fun onEnd(status: Int) {
    val status = handlePakoError(status)
    if (status != Pako.Constants.Z_OK) error("unexpected pako status: $status")
    endHandler()
  }
}

internal class StreamingPakoDeflater(
  var dataHandler: ((Uint8Array) -> Unit) = {},
  var endHandler: (() -> Unit) = {},
  options: Pako.DeflateOptions.() -> Unit = {},
) : Pako.Deflate(js("({})").unsafeCast<Pako.DeflateOptions>().apply { options() }) {

  override fun onData(chunk: Uint8Array) {
    dataHandler(chunk)
  }

  override fun onEnd(status: Int) {
    val status = handlePakoError(status)
    if (status != Pako.Constants.Z_OK) error("unexpected pako status: $status")
    endHandler
  }
}
