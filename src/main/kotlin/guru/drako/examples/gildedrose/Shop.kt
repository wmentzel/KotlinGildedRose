package guru.drako.examples.gildedrose

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
      ItemType.AgedBrie -> {
        increaseQualityWithinBounds(item)
        if (item.sellIn < 0) {
          increaseQualityWithinBounds(item)
        }
      }
      ItemType.BackstagePasses -> {

        increaseQualityWithinBounds(item)

        listOf(10, 5).forEach {
          if (item.sellIn + 1 <= it) {
            increaseQualityWithinBounds(item)
          }
        }

        if (item.sellIn < 0) {
          item.quality = 0
        }
      }
      ItemType.ConjuredItems -> {
        decreaseQualityWithinBounds(item, 2)

        if (item.sellIn < 0) {
          decreaseQualityWithinBounds(item, 2)
        }
      }
      ItemType.UsualItems -> {
        decreaseQualityWithinBounds(item, 1)

        if (item.sellIn < 0) {
          decreaseQualityWithinBounds(item, 1)
        }
      }
      ItemType.Sulfuras -> {
      }
    }
  }

  private fun decreaseQualityWithinBounds(item: Item, degradeBy: Int) {
    if (item.quality > 0) {
      item.quality -= degradeBy
    }
  }

  private fun increaseQualityWithinBounds(item: Item) {
    if (item.quality < 50) {
      ++item.quality
    }
  }
}
