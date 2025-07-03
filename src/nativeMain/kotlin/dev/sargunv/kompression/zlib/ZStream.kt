package dev.sargunv.kompression.zlib

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.Pinned
import kotlinx.cinterop.UnsafeNumber
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.interpretCPointer
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.nativeNullPtr
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString
import platform.zlib.Z_BLOCK
import platform.zlib.Z_BUF_ERROR
import platform.zlib.Z_DATA_ERROR
import platform.zlib.Z_ERRNO
import platform.zlib.Z_FINISH
import platform.zlib.Z_FULL_FLUSH
import platform.zlib.Z_MEM_ERROR
import platform.zlib.Z_NEED_DICT
import platform.zlib.Z_NO_FLUSH
import platform.zlib.Z_OK
import platform.zlib.Z_PARTIAL_FLUSH
import platform.zlib.Z_STREAM_END
import platform.zlib.Z_STREAM_ERROR
import platform.zlib.Z_SYNC_FLUSH
import platform.zlib.Z_TREES
import platform.zlib.Z_VERSION_ERROR
import platform.zlib.z_stream

@OptIn(ExperimentalForeignApi::class)
internal class ZStream() : SafeNativeObj<z_stream>(nativeHeap.alloc<z_stream>()) {

  val inputBytesAvailable: UInt
    get() = self.avail_in

  @OptIn(UnsafeNumber::class)
  val totalBytesRead: ULong
    get() = self.total_in.toULong()

  val outputBytesAvailable: UInt
    get() = self.avail_out

  @OptIn(UnsafeNumber::class)
  val totalBytesWritten: ULong
    get() = self.total_out.toULong()

  /** Must call this when inputBytesAvailable has dropped to zero. */
  fun setInput(buffer: Pinned<ByteArray>, offset: Int = 0, available: Int) {
    self.next_in = buffer.addressOf(offset).reinterpret()
    self.avail_in = available.toUInt()
  }

  /** Must call this when outputBytesAvailable has dropped to zero. */
  fun setOutput(buffer: Pinned<ByteArray>, offset: Int = 0, available: Int) {
    self.next_out = buffer.addressOf(offset).reinterpret()
    self.avail_out = available.toUInt()
  }

  fun deflateInit(level: Int): Int {
    self.zalloc = interpretCPointer(nativeNullPtr)
    self.zfree = interpretCPointer(nativeNullPtr)
    self.opaque = interpretCPointer(nativeNullPtr)
    return handleErrorCode { platform.zlib.deflateInit(self.ptr, level) }
  }

  fun inflateInit(): Int {
    self.next_in = interpretCPointer(nativeNullPtr)
    self.avail_in = 0u
    self.zalloc = interpretCPointer(nativeNullPtr)
    self.zfree = interpretCPointer(nativeNullPtr)
    self.opaque = interpretCPointer(nativeNullPtr)
    return handleErrorCode { platform.zlib.inflateInit(self.ptr) }
  }

  fun deflate(mode: Flush): Int = handleErrorCode { platform.zlib.deflate(self.ptr, mode.value) }

  fun inflate(mode: Flush): Int = handleErrorCode { platform.zlib.inflate(self.ptr, mode.value) }

  fun deflateEnd() = handleErrorCode { platform.zlib.deflateEnd(self.ptr) }

  fun inflateEnd() = handleErrorCode { platform.zlib.inflateEnd(self.ptr) }

  fun deflateReset() = handleErrorCode { platform.zlib.deflateReset(self.ptr) }

  fun inflateReset() = handleErrorCode { platform.zlib.inflateReset(self.ptr) }

  private inline fun handleErrorCode(block: () -> Int) =
    when (val ret = block()) {
      Z_OK,
      Z_NEED_DICT,
      Z_STREAM_END -> ret

      Z_ERRNO -> error("Z_ERRNO: ${self.msg?.toKString()}")
      Z_STREAM_ERROR -> error("Z_STREAM_ERROR: ${self.msg?.toKString()}")
      Z_DATA_ERROR -> error("Z_DATA_ERROR: ${self.msg?.toKString()}")
      Z_MEM_ERROR -> error("Z_MEM_ERROR: ${self.msg?.toKString()}")
      Z_BUF_ERROR -> error("Z_BUF_ERROR: ${self.msg?.toKString()}")
      Z_VERSION_ERROR -> error("Z_VERSION_ERROR: ${self.msg?.toKString()}")
      else -> error("Unknown zlib error code: $ret")
    }

  internal enum class Flush(internal val value: Int) {
    None(Z_NO_FLUSH),
    Partial(Z_PARTIAL_FLUSH),
    Sync(Z_SYNC_FLUSH),
    Full(Z_FULL_FLUSH),
    Finish(Z_FINISH),
    Block(Z_BLOCK),
    Trees(Z_TREES),
  }
}
