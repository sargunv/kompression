package dev.sargunv.kompression.zlib

import platform.zlib.Z_BUF_ERROR
import platform.zlib.Z_DATA_ERROR
import platform.zlib.Z_ERRNO
import platform.zlib.Z_MEM_ERROR
import platform.zlib.Z_NEED_DICT
import platform.zlib.Z_OK
import platform.zlib.Z_STREAM_END
import platform.zlib.Z_STREAM_ERROR
import platform.zlib.Z_VERSION_ERROR

internal sealed interface ResultCode {
  val value: Int

  enum class NonFatal(override val value: Int) : ResultCode {
    Ok(Z_OK),
    NeedDict(Z_NEED_DICT),
    StreamEnd(Z_STREAM_END),
  }

  enum class Fatal(override val value: Int) : ResultCode {
    ErrNo(Z_ERRNO),
    StreamError(Z_STREAM_ERROR),
    DataError(Z_DATA_ERROR),
    MemoryError(Z_MEM_ERROR),
    BufferError(Z_BUF_ERROR),
    VersionError(Z_VERSION_ERROR),
  }

  fun nonFatalOrThrow() = (this as? NonFatal) ?: throw ZlibException(this)

  companion object {
    fun of(value: Int): ResultCode =
      NonFatal.entries.find { it.value == value }
        ?: Fatal.entries.find { it.value == value }
        ?: error("Unknown zlib result code: $value")
  }
}

internal class ZlibException(val resultCode: ResultCode) : Exception(resultCode.toString())
