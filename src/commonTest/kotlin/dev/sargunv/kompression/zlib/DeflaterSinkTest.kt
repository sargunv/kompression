package dev.sargunv.kompression.zlib

import kotlinx.io.Buffer
import kotlinx.io.buffered
import kotlinx.io.readString
import kotlin.test.Test
import kotlin.test.assertEquals

class DeflaterSinkTest {
  private fun testcase(sample: SampleData) {
    val deflated = Buffer()
    DeflaterSink(deflated).use { sample.original.asSource().transferTo(it) }
    assertEquals(sample.original, InflaterSource(deflated).buffered().readString())
  }

  @Test fun empty() = testcase(SampleData.empty)

  @Test fun quickBrownFox() = testcase(SampleData.quickBrownFox)

  @Test fun loremIpsum() = testcase(SampleData.loremIpsum)
}
