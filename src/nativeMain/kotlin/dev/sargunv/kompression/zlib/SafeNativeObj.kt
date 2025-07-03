package dev.sargunv.kompression.zlib

import kotlinx.cinterop.CVariable
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.free
import kotlinx.cinterop.nativeHeap

@OptIn(ExperimentalForeignApi::class)
internal open class SafeNativeObj<T : CVariable>(self: T) : AutoCloseable {
  private var isClosed = false
  protected val self = self
    get() {
      if (isClosed) error("used after close")
      return field
    }

  override fun close() {
    if (isClosed) return
    nativeHeap.free(self)
    isClosed = true
  }
}
