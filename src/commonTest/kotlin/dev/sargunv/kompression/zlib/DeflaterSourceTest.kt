package dev.sargunv.kompression.zlib

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.io.Buffer
import kotlinx.io.buffered
import kotlinx.io.readString

class DeflaterSourceTest {
  private fun testcase(sample: SampleData) {
    val deflated = Buffer()
    deflated.use { DeflaterSource(sample.original.asSource()).buffered().transferTo(it) }
    assertEquals(sample.original, InflaterSource(deflated).buffered().readString())
  }

  @Test fun empty() = testcase(SampleData.empty)

  @Test fun quickBrownFox() = testcase(SampleData.quickBrownFox)

  @Test fun loremIpsum() = testcase(SampleData.loremIpsum)
}
