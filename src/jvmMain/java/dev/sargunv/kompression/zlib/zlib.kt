package dev.sargunv.kompression.zlib

import java.util.zip.Deflater
import java.util.zip.DeflaterInputStream
import java.util.zip.InflaterInputStream
import kotlinx.io.Sink
import kotlinx.io.Source
import kotlinx.io.asInputStream
import kotlinx.io.asOutputStream

public actual fun Source.inflateTo(sink: Sink) {
  InflaterInputStream(this.asInputStream()).transferTo(sink.asOutputStream())
}

public actual fun Source.deflateTo(sink: Sink, level: Int) {
  DeflaterInputStream(this.asInputStream(), Deflater(level)).transferTo(sink.asOutputStream())
}
