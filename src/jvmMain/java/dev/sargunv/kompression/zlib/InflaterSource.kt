package dev.sargunv.kompression.zlib

import kotlinx.io.RawSource
import kotlinx.io.Source
import kotlinx.io.asInputStream
import kotlinx.io.asSource
import java.util.zip.InflaterInputStream

public actual class InflaterSource actual constructor(source: Source) :
  RawSource by InflaterInputStream(source.asInputStream()).asSource()
