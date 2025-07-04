package dev.sargunv.kompression.deflate

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.io.Buffer
import kotlinx.io.Source
import kotlinx.io.buffered
import kotlinx.io.readString

class RoundTripSourceTest {
  private fun testcase(sample: SampleData) {
    val dest1 = Buffer()
    sample.original.asSource().deflated().buffered().transferTo(dest1)

    val dest2 = Buffer()
    (dest1 as Source).inflated().buffered().transferTo(dest2)

    assertEquals(sample.original, dest2.readString())
  }

  @Test fun empty() = testcase(SampleData.empty)

  @Test fun quickBrownFox() = testcase(SampleData.quickBrownFox)

  @Test fun loremIpsum() = testcase(SampleData.loremIpsum)
}
