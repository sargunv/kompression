package dev.sargunv.kompression.zlib

import kotlinx.io.Sink
import kotlinx.io.Source

public expect fun Source.inflateTo(sink: Sink)

public expect fun Source.deflateTo(sink: Sink, level: Int = 6)
