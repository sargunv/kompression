package dev.sargunv.kompression.zlib

import org.khronos.webgl.Uint8Array

@JsModule("pako")
@JsNonModule
internal external object Pako {
  @JsName("constants")
  object Constants {
    // FlushValues
    val Z_NO_FLUSH: Int
    val Z_PARTIAL_FLUSH: Int
    val Z_SYNC_FLUSH: Int
    val Z_FULL_FLUSH: Int
    val Z_FINISH: Int
    val Z_BLOCK: Int
    val Z_TREES: Int

    // StrategyValues
    val Z_FILTERED: Int
    val Z_HUFFMAN_ONLY: Int
    val Z_RLE: Int
    val Z_FIXED: Int
    val Z_DEFAULT_STRATEGY: Int

    // ReturnCodes
    val Z_OK: Int
    val Z_STREAM_END: Int
    val Z_NEED_DICT: Int
    val Z_ERRNO: Int
    val Z_STREAM_ERROR: Int
    val Z_DATA_ERROR: Int
    val Z_BUF_ERROR: Int
  }

  interface DeflateOptions {
    var level: Int?
    var windowBits: Int?
    var memLevel: Int?
    var strategy: Int?
    var dictionary: Any?
    var raw: Boolean?
    var chunkSize: Int?
    var gzip: Boolean?
    var header: Header?
  }

  interface InflateOptions {
    var windowBits: Int?
    var dictionary: Any?
    var raw: Boolean?
    var chunkSize: Int?
  }

  interface Header {
    var text: Boolean?
    var time: Int?
    var os: Int?
    var extra: Array<Int>?
    var name: String?
    var comment: String?
    var hcrc: Boolean?
  }

  open class Deflate(options: DeflateOptions? = definedExternally) {
    val err: Int
    val msg: String
    val result: Uint8Array

    open fun onData(chunk: Uint8Array)

    open fun onEnd(status: Int)

    fun push(data: Uint8Array, mode: Int): Boolean
  }

  open class Inflate(options: InflateOptions? = definedExternally) {
    val header: Header?
    val err: Int
    val msg: String
    val result: Uint8Array

    open fun onData(chunk: Uint8Array)

    open fun onEnd(status: Int)

    fun push(data: Uint8Array, mode: Int): Boolean
  }
}
