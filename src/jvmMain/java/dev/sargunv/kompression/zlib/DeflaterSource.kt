package dev.sargunv.kompression.zlib

import java.util.zip.Deflater
import java.util.zip.DeflaterInputStream
import kotlinx.io.RawSource
import kotlinx.io.Source
import kotlinx.io.asInputStream
import kotlinx.io.asSource

public actual class DeflaterSource actual constructor(source: Source, level: Int) :
  RawSource by DeflaterInputStream(source.asInputStream(), Deflater(level)).asSource()
