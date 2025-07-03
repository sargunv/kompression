package dev.sargunv.kompression.zlib

import java.util.zip.InflaterOutputStream
import kotlinx.io.RawSink
import kotlinx.io.Sink
import kotlinx.io.asOutputStream
import kotlinx.io.asSink

public actual class InflaterSink actual constructor(sink: Sink) :
  RawSink by InflaterOutputStream(sink.asOutputStream()).asSink()
