package guru.drako.examples.gildedrose

import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ShopFuzzTest {
  companion object {
    private const val ITERATIONS = 100
  }

  private val nonLegendaryItemNames = listOf(
    "+5 Dexterity Vest", // regular item
    "Aged Brie", // gets better with age
    "Backstage passes to a TAFKAL80ETC concert", // gets better, but drops to 0 after sellIn
    "dummy" // some non existing and thus regular item
  )

  private val legendaryItemNames = listOf(
    "Sulfuras, Hand of Ragnaros"
  )

  @TestFactory
  fun `updateQualities on legenday goods should not break constraints`() = iterator {
    repeat(times = ITERATIONS) { iteration ->
      val numberOfDays = (1..101).random()
      val sellIn = (1..31).random() // in production this is actually always 0
      val quality = (1..51).random() // in production this is actually always 80
      val name = legendaryItemNames.random()

      val displayName =
        """#${iteration + 1}: numberOfDays=$numberOfDays, name="$name", sellIn=$sellIn, quality=$quality"""

      yield(
        dynamicTest(displayName) {
          val shop = Shop(items = listOf(Item(name = name, sellIn = sellIn, quality = quality)))
          repeat(times = numberOfDays) {
            shop.updateQuality()
          }

          // quality and sellIn stay unchanged for legendary items
          val result = shop.items.first()
          assertEquals(
            actual = result.quality,
            expected = quality
          )
          assertEquals(
            actual = result.sellIn,
            expected = sellIn
          )
        }
      )
    }
  }

  @TestFactory
  fun `updateQualities on non-legendary goods should not break constraints`() = iterator {
    repeat(times = ITERATIONS) { iteration ->
      val numberOfDays = (1..101).random()
      val sellIn = (1..31).random()
      val quality = (1..51).random()
      val name = nonLegendaryItemNames.random()

      val displayName =
        """#${iteration + 1}: numberOfDays=$numberOfDays, name="$name", sellIn=$sellIn, quality=$quality"""

      yield(
        dynamicTest(displayName) {
          val shop = Shop(items = listOf(Item(name = name, sellIn = sellIn, quality = quality)))
          repeat(times = numberOfDays) {
            shop.updateQuality()
          }

          val result = shop.items.first()
          assertTrue(
            actual = result.quality in (0..50),
            message = "quality must be in the range 0 to 50, but was ${result.quality}"
          )
          assertEquals(
            actual = result.sellIn,
            expected = sellIn - numberOfDays
          )
        }
      )
    }
  }
}