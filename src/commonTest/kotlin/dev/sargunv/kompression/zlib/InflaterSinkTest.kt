package dev.sargunv.kompression.zlib

import kotlinx.io.Buffer
import kotlinx.io.readString
import kotlin.test.Test
import kotlin.test.assertEquals

class InflaterSinkTest {
  private fun testcase(sample: SampleData) {
    val inflated = Buffer()
    inflated.use { sample.deflated.asSource().transferTo(InflaterSink(it)) }
    assertEquals(sample.original, inflated.readString())
  }

  @Test fun empty() = testcase(SampleData.empty)

  @Test fun quickBrownFox() = testcase(SampleData.quickBrownFox)

  @Test fun loremIpsum() = testcase(SampleData.loremIpsum)
}
