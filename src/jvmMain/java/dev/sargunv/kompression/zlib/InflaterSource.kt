package dev.sargunv.kompression.zlib

import java.util.zip.InflaterInputStream
import kotlinx.io.RawSource
import kotlinx.io.Source
import kotlinx.io.asInputStream
import kotlinx.io.asSource

public actual class InflaterSource actual constructor(source: Source) :
  RawSource by InflaterInputStream(source.asInputStream()).asSource()
