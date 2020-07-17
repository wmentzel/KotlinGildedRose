package guru.drako.examples.gildedrose

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import kotlin.test.Test
import kotlin.test.assertEquals

@TestInstance(Lifecycle.PER_CLASS)
class ShopTest {

  @Nested
  inner class SellInTests {
    @TestFactory
    fun `'sell in' of items (except sulfuras) should decrease by 1 per day`() = listOf(
      AGE_BRIE,
      BACKSTAGE_PASSES,
      DEXTERITY_VEST,
      ELIXIR_OF_THE_MONGOOSE,
      CONJURED_MANA_CAKE
    ).flatMap { itemName ->

      val qualityValues = 0..50 step 10
      val sellInValues = -5..5

      qualityValues.flatMap { qualityValue ->
        sellInValues.map { sellInValue ->
          sellInValue to qualityValue
        }
      }.map { (sellInValue, qualityValue) ->

        val shop = Shop(listOf(Item(itemName, sellIn = sellInValue, quality = qualityValue)))

        shop.runForOneDay()

        dynamicTest("'sell in' should decrease correctly if quality is $qualityValue") {
          assertEquals(
            expected = sellInValue - 1,
            actual = shop.items.first().sellIn,
            message = "quality was $qualityValue"
          )
        }
      }
    }

    @TestFactory
    fun `'sell in' of sulfuras should not change`() = (-5..5).map { sellIn ->

      val shop = Shop(listOf(Item(name = SULFURAS, sellIn = sellIn, quality = 80)))

      shop.runForOneDay()

      dynamicTest("'sell in' of sulfuras should not change if it is $sellIn") {
        assertEquals(expected = sellIn, actual = shop.items.first().sellIn)
      }
    }
  }

  data class QualityTestCase(
    val description: String,
    val initialItem: Item,
    val expectedQuality: Int
  )

  @Nested
  inner class QualityTests {

    @Nested
    inner class UsualItems {

      @TestFactory
      fun `quality of usual items should change correctly if 'sell in' is greater than 0`() =
        listOf(
          DEXTERITY_VEST,
          ELIXIR_OF_THE_MONGOOSE
        ).flatMap { itemName ->
          (1..5).map {
            QualityTestCase(
              description = "quality of $itemName should change correctly if 'sell in' is $it",
              initialItem = Item(name = itemName, sellIn = it, quality = 20),
              expectedQuality = 19
            )
          }
        }.map(::createDynamicTest)

      @TestFactory
      fun `quality of usual items should not go below zero if 'sell in' is greater than 0`() =
        listOf(
          DEXTERITY_VEST,
          ELIXIR_OF_THE_MONGOOSE
        ).flatMap { itemName ->
          (1..5).map {
            QualityTestCase(
              description = "quality of $itemName should not go below zero if 'sell in' is $it",
              initialItem = Item(name = itemName, sellIn = it, quality = 0),
              expectedQuality = 0
            )
          }
        }.map(::createDynamicTest)

      @TestFactory
      fun `quality of usual items should decrease by 2 (double as fast) if 'sell in' is less than or equal to 0`() =
        listOf(
          DEXTERITY_VEST,
          ELIXIR_OF_THE_MONGOOSE
        ).flatMap { itemName ->
          (-5..0).map {
            QualityTestCase(
              description = "quality of $itemName should decrease by 2 (double as fast) if 'sell in' is $it",
              initialItem = Item(name = itemName, sellIn = it, quality = 10),
              expectedQuality = 8
            )
          }
        }.map(::createDynamicTest)
    }

    @Nested
    inner class AgedBrie {
      @TestFactory
      fun `quality of aged brie should increase in quality by 1 if 'sell in' is greater than zero`() = (1..5).map {
        QualityTestCase(
          description = "quality of $AGE_BRIE should increase in quality by 1 if 'sell in' is $it",
          initialItem = Item(name = AGE_BRIE, sellIn = it, quality = 0),
          expectedQuality = 1
        )
      }.map(::createDynamicTest)

      @TestFactory
      fun `quality of aged brie should not surpass 50 if 'sell in' is greater than zero`() = (1..5).map {
        QualityTestCase(
          description = "quality of $AGE_BRIE should not surpass 50 if 'sell in' is $it",
          initialItem = Item(name = AGE_BRIE, sellIn = it, quality = 50),
          expectedQuality = 50
        )
      }.map(::createDynamicTest)

      @TestFactory
      fun `quality of aged brie should increase by 2 when 'sell in' is lower or equal to zero`() = (-5..0).map {
        QualityTestCase(
          description = "quality of $AGE_BRIE should increase by 2 when 'sell in' is $it",
          initialItem = Item(name = AGE_BRIE, sellIn = it, quality = 39),
          expectedQuality = 41
        )
      }.map(::createDynamicTest)
    }

    @Test
    fun `quality of sulfuras should not change`() = (-5..5).map {
      val shop = Shop(listOf(Item(name = SULFURAS, sellIn = 1, quality = 80)))

      shop.runForOneDay()

      dynamicTest("quality of sulfuras should not change when 'sell in' is $it") {
        assertEquals(expected = 80, actual = shop.items.first().quality)
      }
    }

    @Nested
    inner class BackstagePasses {

      @TestFactory
      fun `quality of backstage passes should change correctly`() = (11..15).map {
        QualityTestCase(
          description = "quality of $BACKSTAGE_PASSES should change correctly if 'sell in' is $it",
          initialItem = Item(name = BACKSTAGE_PASSES, sellIn = it, quality = 20),
          expectedQuality = 21
        )
      }.map(::createDynamicTest)

      @TestFactory
      fun `quality of backstage passes should increase by 2 if 'sell in' is greater than 5 and less than or equal to 10`() =
        (6..10).map {
          QualityTestCase(
            description = "quality of $BACKSTAGE_PASSES should increase by 2 if 'sell in' is $it",
            initialItem = Item(name = BACKSTAGE_PASSES, sellIn = it, quality = 0),
            expectedQuality = 2
          )
        }.map(::createDynamicTest)

      @TestFactory
      fun `quality of backstage passes should increase by 3 if 'sell in' is less than or equal to 5`() = (1..5).map {
        QualityTestCase(
          description = "quality of $BACKSTAGE_PASSES should increase by 3 if 'sell in' is less than or equal to 5",
          initialItem = Item(name = BACKSTAGE_PASSES, sellIn = it, quality = 0),
          expectedQuality = 3
        )
      }.map(::createDynamicTest)


      @TestFactory
      fun `quality of backstage passes should go to zero if the concert is over`() = (5..10).map {
        QualityTestCase(
          description = "quality of $BACKSTAGE_PASSES should go to zero if the concert is over",
          initialItem = Item(name = BACKSTAGE_PASSES, sellIn = 0, quality = it),
          expectedQuality = 0
        )
      }.map(::createDynamicTest)

      @TestFactory
      fun `quality of backstage passes should not go below zero`() = (-5..0).map {
        QualityTestCase(
          description = "quality of $BACKSTAGE_PASSES should not go below zero",
          initialItem = Item(name = BACKSTAGE_PASSES, sellIn = it, quality = 0),
          expectedQuality = 0
        )
      }.map(::createDynamicTest)

      @TestFactory
      fun `quality of backstage passes should not surpass 50`() = (1..10).map {
        QualityTestCase(
          description = "quality of $BACKSTAGE_PASSES should not surpass 50",
          initialItem = Item(name = BACKSTAGE_PASSES, sellIn = it, quality = 50),
          expectedQuality = 50
        )
      }.map(::createDynamicTest)

      @Nested
      inner class ConjuredMana {

        @TestFactory
        fun `conjured mana cake should change correctly in one day`() = (1..5).map {
          QualityTestCase(
            description = "quality of conjured items should decrease by 2 when sell in is $it",
            initialItem = Item(name = CONJURED_MANA_CAKE, sellIn = it, quality = 6),
            expectedQuality = 4
          )
        }.map(::createDynamicTest)

        @TestFactory
        fun `quality of $temName should decrease by 4 (double as normal items) when 'sell in' is $it`() = (-5..0).map {
          QualityTestCase(
            description = "quality of conjured items should decrease by 4 (double as normal items) when sell in is lower or equal to zero",
            initialItem = Item(name = CONJURED_MANA_CAKE, sellIn = it, quality = 6),
            expectedQuality = 2
          )
        }.map(::createDynamicTest)

        @TestFactory
        fun `quality of conjured mana should never go below zero`() = (-5..0).map {
          QualityTestCase(
            description = "quality of conjured items should never go below zero",
            initialItem = Item(name = CONJURED_MANA_CAKE, sellIn = it, quality = 2),
            expectedQuality = 0
          )
        }.map(::createDynamicTest)
      }
    }
  }
}

private fun createDynamicTest(qualityTestCase: ShopTest.QualityTestCase): DynamicTest {
  val shop = Shop(listOf(qualityTestCase.initialItem))
  shop.runForOneDay()

  return dynamicTest(qualityTestCase.description) {
    assertEquals(expected = qualityTestCase.expectedQuality, actual = shop.items.first().quality)
  }
}
