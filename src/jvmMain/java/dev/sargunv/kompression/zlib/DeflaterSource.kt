package dev.sargunv.kompression.zlib

import kotlinx.io.RawSource
import kotlinx.io.Source
import kotlinx.io.asInputStream
import kotlinx.io.asSource
import java.util.zip.Deflater
import java.util.zip.DeflaterInputStream

public actual class DeflaterSource actual constructor(source: Source, level: Int) :
  RawSource by DeflaterInputStream(source.asInputStream(), Deflater(level)).asSource()
