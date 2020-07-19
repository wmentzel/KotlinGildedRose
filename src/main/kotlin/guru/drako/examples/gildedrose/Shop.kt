package guru.drako.examples.gildedrose

import kotlin.math.max
import kotlin.math.min

const val AGE_BRIE = "Aged Brie"
const val BACKSTAGE_PASSES = "Backstage passes to a TAFKAL80ETC concert"
const val SULFURAS = "Sulfuras, Hand of Ragnaros"

enum class ItemType {
  AgedBrie,
  BackstagePasses,
  ConjuredItems,
  UsualItems,
  Sulfuras
}

val Item.type
  get() = when {
    this.name == AGE_BRIE -> ItemType.AgedBrie
    this.name.startsWith("backstage passes", ignoreCase = true) -> ItemType.BackstagePasses
    this.name == SULFURAS -> ItemType.Sulfuras
    this.name.startsWith("conjured", ignoreCase = true) -> ItemType.ConjuredItems
    else -> ItemType.UsualItems
  }

class Shop(val items: List<Item>) {
  fun runForOneDay() {
    items.forEach(this::updateItem)
  }

  private fun updateItem(item: Item) {

    if (item.type == ItemType.Sulfuras) {
      return
    }

    --item.sellIn

    when (item.type) {
      ItemType.AgedBrie -> increaseQualityWithinBounds(item, if (item.sellIn < 0) 2 else 1)

      ItemType.BackstagePasses -> {

        val increaseBy = when {
          item.sellIn + 1 <= 5 -> 3
          item.sellIn + 1 <= 10 -> 2
          else -> 1
        }

        increaseQualityWithinBounds(item, increaseBy)

        if (item.sellIn < 0) {
          item.quality = 0
        }
      }
      ItemType.ConjuredItems -> decreaseQualityWithinBounds(item, if (item.sellIn < 0) 4 else 2)
      ItemType.UsualItems -> decreaseQualityWithinBounds(item, if (item.sellIn < 0) 2 else 1)
      ItemType.Sulfuras -> {
      }
    }
  }

  private fun decreaseQualityWithinBounds(item: Item, degradeBy: Int) {
    item.quality = max(item.quality - degradeBy, 0)
  }

  private fun increaseQualityWithinBounds(item: Item, increaseBy: Int) {
    item.quality = min(item.quality + increaseBy, 50)
  }
}
