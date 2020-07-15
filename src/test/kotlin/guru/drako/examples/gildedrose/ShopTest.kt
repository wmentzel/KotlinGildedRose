package guru.drako.examples.gildedrose

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import kotlin.test.Test
import kotlin.test.assertEquals

@TestInstance(Lifecycle.PER_CLASS)
class ShopTest {

  data class TestCase(
    val description: String,
    val initialItem: Item,
    val expectedItem: Item
  )

  @TestFactory
  fun `usual items should change correctly`() =
    listOf(
      ItemName.DexterityVest.itemName,
      ItemName.ElixirOfTheMongoose.itemName
    ).flatMap { itemName ->
      listOf(
        TestCase(
          description = "$itemName should degrade correctly in 1 day",
          initialItem = Item(name = itemName, sellIn = 10, quality = 20),
          expectedItem = Item(name = itemName, sellIn = 9, quality = 19)
        ),
        TestCase(
          description = "quality of $itemName should not go below zero",
          initialItem = Item(name = itemName, sellIn = 1, quality = 0),
          expectedItem = Item(name = itemName, sellIn = 0, quality = 0)
        ),
        TestCase(
          description = "quality of $itemName should decrease by 2 (double as fast) if sell in is surpassed",
          initialItem = Item(name = itemName, sellIn = 0, quality = 10),
          expectedItem = Item(name = itemName, sellIn = -1, quality = 8)
        )
      )
    }.map(this::createDynamicTest)

  @TestFactory
  fun `aged brie should change correctly`(): List<DynamicTest> {

    val itemName = ItemName.AgedBrie.itemName
    return listOf(
      TestCase(
        description = "$itemName should increase in quality by 1 per day",
        initialItem = Item(name = itemName, sellIn = 2, quality = 0),
        expectedItem = Item(name = itemName, sellIn = 1, quality = 1)
      ),
      TestCase(
        description = "$itemName quality should not surpass 50",
        initialItem = Item(name = itemName, sellIn = 2, quality = 50),
        expectedItem = Item(name = itemName, sellIn = 1, quality = 50)
      ),
      TestCase(
        description = "$itemName quality should increase twice as fast when sell in is surpassed",
        initialItem = Item(name = itemName, sellIn = 0, quality = 39),
        expectedItem = Item(name = itemName, sellIn = -1, quality = 41)
      )
    ).map(this::createDynamicTest)
  }

  @Test
  fun `sulfuras' sell in value & quality should not change`() {

    val shop = Shop(listOf(Item(name = ItemName.Sulfuras.itemName, sellIn = 1, quality = 80)))

    shop.runForOneDay()

    assertEquals(
      Item(name = ItemName.Sulfuras.itemName, sellIn = 1, quality = 80),
      shop.items.first()
    )
  }

  @TestFactory
  fun `backstage passes should change correctly`(): List<DynamicTest> {
    val itemName = ItemName.BackstagePasses.itemName

    return listOf(
      TestCase(
        description = "$itemName passes should degrade correctly",
        initialItem = Item(name = itemName, sellIn = 15, quality = 20),
        expectedItem = Item(name = itemName, sellIn = 14, quality = 21)
      ),
      TestCase(
        description = "backstage passes should increase in quality by 2 if sell in is greater than 5 and less than or equal to 10",
        initialItem = Item(name = itemName, sellIn = 10, quality = 0),
        expectedItem = Item(name = itemName, sellIn = 9, quality = 2)
      ),
      TestCase(
        description = "backstage passes should increase in quality by 3 if sell in less than or equal to 5",
        initialItem = Item(name = itemName, sellIn = 5, quality = 0),
        expectedItem = Item(name = itemName, sellIn = 4, quality = 3)
      ),
      TestCase(
        description = "backstage passes quality should go to zero if the concert is over",
        initialItem = Item(name = itemName, sellIn = 0, quality = 1),
        expectedItem = Item(name = itemName, sellIn = -1, quality = 0)
      ),
      TestCase(
        description = "backstage passes quality should not go below zero",
        initialItem = Item(name = itemName, sellIn = 0, quality = 0),
        expectedItem = Item(name = itemName, sellIn = -1, quality = 0)
      ),
      TestCase(
        description = "backstage passes quality should not surpass 50",
        initialItem = Item(name = itemName, sellIn = 15, quality = 50),
        expectedItem = Item(name = itemName, sellIn = 14, quality = 50)
      )
    ).map(this::createDynamicTest)
  }

  @TestFactory
  fun `conjured items should change correctly`(): List<DynamicTest> {

    val itemName = ItemName.ConjuredManaCake.itemName

    return listOf(
      TestCase(
        description = "conjured items should degrade quality by 2 in one day",
        initialItem = Item(name = itemName, sellIn = 3, quality = 6),
        expectedItem = Item(name = itemName, sellIn = 2, quality = 4)
      ),
      TestCase(
        description = "quality of conjured items should be lowered by 4 (double as normal items) in one day when sell in has passed",
        initialItem = Item(name = itemName, sellIn = 0, quality = 6),
        expectedItem = Item(name = itemName, sellIn = -1, quality = 2)
      ),
      TestCase(
        description = "quality of conjured items should never go below zero",
        initialItem = Item(name = itemName, sellIn = 0, quality = 2),
        expectedItem = Item(name = itemName, sellIn = -1, quality = 0)
      )
    ).map(this::createDynamicTest)
  }

  private fun createDynamicTest(testCase: TestCase): DynamicTest {
    val shop = Shop(listOf(testCase.initialItem))
    shop.runForOneDay()

    return dynamicTest(testCase.description) {
      assertEquals(expected = testCase.expectedItem, actual = shop.items.first())
    }
  }
}
