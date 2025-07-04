package dev.sargunv.kompression.deflate

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.io.Buffer
import kotlinx.io.Sink
import kotlinx.io.readString

class RoundTripSinkTest {
  private fun testcase(sample: SampleData) {
    val dest1 = Buffer()
    sample.original.asSource().transferTo((dest1 as Sink).deflated())

    val dest2 = Buffer()
    dest1.transferTo((dest2 as Sink).inflated())

    assertEquals(sample.original, dest2.readString())
  }

  @Test fun empty() = testcase(SampleData.empty)

  @Test fun quickBrownFox() = testcase(SampleData.quickBrownFox)

  @Test fun loremIpsum() = testcase(SampleData.loremIpsum)
}
