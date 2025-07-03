package dev.sargunv.kompression.zlib

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.io.Buffer
import kotlinx.io.readString

class InflaterTest {
  private fun testcase(sample: SampleData) {
    val dest = Buffer()
    sample.deflated.asSource().inflateTo(dest)
    assertEquals(sample.original, dest.readString())
  }

  @Test fun empty() = testcase(SampleData.empty)

  @Test fun quickBrownFox() = testcase(SampleData.quickBrownFox)

  @Test fun loremIpsum() = testcase(SampleData.loremIpsum)
}
