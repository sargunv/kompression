package dev.sargunv.kompression.zlib

import platform.zlib.Z_BLOCK
import platform.zlib.Z_FINISH
import platform.zlib.Z_FULL_FLUSH
import platform.zlib.Z_NO_FLUSH
import platform.zlib.Z_PARTIAL_FLUSH
import platform.zlib.Z_SYNC_FLUSH
import platform.zlib.Z_TREES

internal enum class Flush(val value: Int) {
  None(Z_NO_FLUSH),
  Partial(Z_PARTIAL_FLUSH),
  Sync(Z_SYNC_FLUSH),
  Full(Z_FULL_FLUSH),
  Finish(Z_FINISH),
  Block(Z_BLOCK),
  Trees(Z_TREES),
}
