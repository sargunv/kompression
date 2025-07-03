package dev.sargunv.kompression.zlib

import kotlinx.io.RawSink
import kotlinx.io.Sink
import kotlinx.io.asOutputStream
import kotlinx.io.asSink
import java.util.zip.InflaterOutputStream

public actual class InflaterSink actual constructor(sink: Sink) :
  RawSink by InflaterOutputStream(sink.asOutputStream()).asSink()
