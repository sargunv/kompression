package dev.sargunv.kompression.zlib

import kotlinx.io.buffered
import kotlinx.io.readString
import kotlin.test.Test
import kotlin.test.assertEquals

class InflaterSourceTest {
  private fun testcase(sample: SampleData) {
    val source = sample.deflated.asSource()
    assertEquals(sample.original, InflaterSource(source).buffered().readString())
  }

  @Test fun empty() = testcase(SampleData.empty)

  @Test fun quickBrownFox() = testcase(SampleData.quickBrownFox)

  @Test fun loremIpsum() = testcase(SampleData.loremIpsum)
}
