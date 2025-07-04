package dev.sargunv.kompression.deflate

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.io.Buffer
import kotlinx.io.Sink
import kotlinx.io.readString

class InflaterSinkTest {
  private fun testcase(sample: SampleData) {
    val dest = Buffer()
    sample.deflated.asSource().transferTo((dest as Sink).inflated())
    assertEquals(sample.original, dest.readString())
  }

  @Test fun empty() = testcase(SampleData.empty)

  @Test fun quickBrownFox() = testcase(SampleData.quickBrownFox)

  @Test fun loremIpsum() = testcase(SampleData.loremIpsum)
}
