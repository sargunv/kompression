package dev.sargunv.kompression.zlib

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.io.Buffer
import kotlinx.io.readString

class RoundTripTest {
  private fun testcase(sample: SampleData) {
    val dest1 = Buffer()
    sample.original.asSource().deflateTo(dest1)

    val dest2 = Buffer()
    dest1.inflateTo(dest2)

    assertEquals(sample.original, dest2.readString())
  }

  @Test fun empty() = testcase(SampleData.empty)

  @Test fun quickBrownFox() = testcase(SampleData.quickBrownFox)

  @Test fun loremIpsum() = testcase(SampleData.loremIpsum)
}
