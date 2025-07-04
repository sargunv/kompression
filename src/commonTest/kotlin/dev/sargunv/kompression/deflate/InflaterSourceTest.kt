package dev.sargunv.kompression.deflate

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.io.Buffer
import kotlinx.io.buffered
import kotlinx.io.readString

class InflaterSourceTest {
  private fun testcase(sample: SampleData) {
    val dest = Buffer()
    sample.deflated.asSource().inflated().buffered().transferTo(dest)
    assertEquals(sample.original, dest.readString())
  }

  @Test fun empty() = testcase(SampleData.empty)

  @Test fun quickBrownFox() = testcase(SampleData.quickBrownFox)

  @Test fun loremIpsum() = testcase(SampleData.loremIpsum)
}
