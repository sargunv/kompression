package dev.sargunv.kompression.zlib

import java.util.zip.Deflater
import java.util.zip.DeflaterOutputStream
import kotlinx.io.RawSink
import kotlinx.io.Sink
import kotlinx.io.asOutputStream
import kotlinx.io.asSink

public actual class DeflaterSink actual constructor(sink: Sink, level: Int) :
  RawSink by DeflaterOutputStream(sink.asOutputStream(), Deflater(level)).asSink()
