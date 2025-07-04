package dev.sargunv.kompression.zlib

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.Pinned
import kotlinx.cinterop.UnsafeNumber
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.free
import kotlinx.cinterop.interpretCPointer
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.nativeNullPtr
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import platform.zlib.Z_DEFLATED
import platform.zlib.z_stream

@OptIn(ExperimentalForeignApi::class)
internal class ZStream() : AutoCloseable {
  private var isClosed = false

  private val impl = nativeHeap.alloc<z_stream>()
    get() {
      if (isClosed) error("used after close")
      return field
    }

  override fun close() {
    if (isClosed) return
    nativeHeap.free(impl)
    isClosed = true
  }

  val inputBytesAvailable: UInt
    get() = impl.avail_in

  @OptIn(UnsafeNumber::class)
  val totalBytesRead: ULong
    get() = impl.total_in.toULong()

  val outputBytesAvailable: UInt
    get() = impl.avail_out

  @OptIn(UnsafeNumber::class)
  val totalBytesWritten: ULong
    get() = impl.total_out.toULong()

  /** Must call this when inputBytesAvailable has dropped to zero. */
  fun setInput(buffer: Pinned<ByteArray>, offset: Int = 0, available: Int) {
    impl.next_in = buffer.addressOf(offset).reinterpret()
    impl.avail_in = available.toUInt()
  }

  /** Must call this when outputBytesAvailable has dropped to zero. */
  fun setOutput(buffer: Pinned<ByteArray>, offset: Int = 0, available: Int) {
    impl.next_out = buffer.addressOf(offset).reinterpret()
    impl.avail_out = available.toUInt()
  }

  fun deflateInit(
    level: Int = 6,
    windowBits: Int = 15,
    memLevel: Int = 8,
    strategy: Strategy = Strategy.Default,
  ): ResultCode.NonFatal {
    impl.zalloc = interpretCPointer(nativeNullPtr)
    impl.zfree = interpretCPointer(nativeNullPtr)
    impl.opaque = interpretCPointer(nativeNullPtr)
    return runChecking {
      platform.zlib.deflateInit2(
        strm = impl.ptr,
        level = level,
        method = Z_DEFLATED,
        windowBits = windowBits,
        memLevel = memLevel,
        strategy = strategy.value,
      )
    }
  }

  fun inflateInit(windowBits: Int = 15): ResultCode.NonFatal {
    impl.next_in = interpretCPointer(nativeNullPtr)
    impl.avail_in = 0u
    impl.zalloc = interpretCPointer(nativeNullPtr)
    impl.zfree = interpretCPointer(nativeNullPtr)
    impl.opaque = interpretCPointer(nativeNullPtr)
    return runChecking { platform.zlib.inflateInit2(strm = impl.ptr, windowBits = windowBits) }
  }

  fun deflate(mode: Flush) = runChecking { platform.zlib.deflate(impl.ptr, mode.value) }

  fun inflate(mode: Flush) = runChecking { platform.zlib.inflate(impl.ptr, mode.value) }

  fun deflateEnd() = runChecking { platform.zlib.deflateEnd(impl.ptr) }

  fun inflateEnd() = runChecking { platform.zlib.inflateEnd(impl.ptr) }

  fun deflateReset() = runChecking { platform.zlib.deflateReset(impl.ptr) }

  fun inflateReset() = runChecking { platform.zlib.inflateReset(impl.ptr) }

  private inline fun runChecking(block: () -> Int) = ResultCode.of(block()).nonFatalOrThrow()
}
