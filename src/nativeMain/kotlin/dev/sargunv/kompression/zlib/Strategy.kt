package dev.sargunv.kompression.zlib

import platform.zlib.Z_DEFAULT_STRATEGY
import platform.zlib.Z_FILTERED
import platform.zlib.Z_FIXED
import platform.zlib.Z_HUFFMAN_ONLY
import platform.zlib.Z_RLE

internal enum class Strategy(val value: Int) {
  Default(Z_DEFAULT_STRATEGY),
  Filtered(Z_FILTERED),
  Huffman(Z_HUFFMAN_ONLY),
  RLE(Z_RLE),
  Fixed(Z_FIXED),
}
