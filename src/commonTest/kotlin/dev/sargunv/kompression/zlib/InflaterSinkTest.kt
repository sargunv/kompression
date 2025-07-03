package dev.sargunv.kompression.zlib

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.io.Buffer
import kotlinx.io.readString

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
