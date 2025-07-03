package dev.sargunv.kompression.zlib

import kotlinx.io.Sink
import kotlinx.io.Source

public fun Source.inflated(): InflaterSource = InflaterSource(this)

public fun Source.deflated(level: Int = 6): DeflaterSource = DeflaterSource(this, level)

public fun Sink.inflated(): InflaterSink = InflaterSink(this)

public fun Sink.deflated(level: Int = 6): DeflaterSink = DeflaterSink(this, level)
