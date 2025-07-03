package dev.sargunv.kompression.zlib

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.io.buffered
import kotlinx.io.readString

class InflaterSourceTest {
  private fun testcase(sample: SampleData) {
    val source = sample.deflated.asSource()
    assertEquals(sample.original, InflaterSource(source).buffered().readString())
  }

  @Test fun empty() = testcase(SampleData.empty)

  @Test fun quickBrownFox() = testcase(SampleData.quickBrownFox)

  @Test fun loremIpsum() = testcase(SampleData.loremIpsum)
}
