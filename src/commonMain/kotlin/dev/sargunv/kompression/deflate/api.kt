package dev.sargunv.kompression.deflate

import kotlinx.io.RawSink
import kotlinx.io.RawSource
import kotlinx.io.Sink
import kotlinx.io.Source

public class DeflaterSink(private val sink: Sink, private val deflater: Deflater = Deflater()) :
  RawSink by StreamingProcessorSink(sink, deflater)

public class InflaterSink(private val sink: Sink, private val inflater: Inflater = Inflater()) :
  RawSink by StreamingProcessorSink(sink, inflater) {}

public class DeflaterSource(
  private val source: Source,
  private val deflater: Deflater = Deflater(),
) : RawSource by StreamingProcessorSource(source, deflater)

public class InflaterSource(
  private val source: Source,
  private val inflater: Inflater = Inflater(),
) : RawSource by StreamingProcessorSource(source, inflater)

public fun Source.inflated(inflater: Inflater = Inflater()): InflaterSource =
  InflaterSource(this, inflater)

public fun Source.deflated(deflater: Deflater = Deflater()): DeflaterSource =
  DeflaterSource(this, deflater)

public fun Sink.inflated(inflater: Inflater = Inflater()): InflaterSink =
  InflaterSink(this, inflater)

public fun Sink.deflated(deflater: Deflater = Deflater()): DeflaterSink =
  DeflaterSink(this, deflater)
