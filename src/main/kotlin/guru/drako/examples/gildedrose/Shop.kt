package guru.drako.examples.gildedrose

const val AGE_BRIE = "Aged Brie"
const val BACKSTAGE_PASSES = "Backstage passes to a TAFKAL80ETC concert"
const val SULFURAS = "Sulfuras, Hand of Ragnaros"

class Shop(val items: List<Item>) {
  fun runForOneDay() {
    items.forEach(this::updateItem)
  }

  private fun updateItem(item: Item) {

    if (item.name == SULFURAS) {
      return
    }

    --item.sellIn

    if (item.name in listOf(AGE_BRIE, BACKSTAGE_PASSES)) {
      increaseQualityWithinBounds(item)
    }

    when (item.name) {
      AGE_BRIE -> {
        if (item.sellIn < 0) {
          increaseQualityWithinBounds(item)
        }
      }
      BACKSTAGE_PASSES -> {

        listOf(10, 5).forEach {
          if (item.sellIn + 1 <= it) {
            increaseQualityWithinBounds(item)
          }
        }

        if (item.sellIn < 0) {
          item.quality = 0
        }
      }
      else -> {

        val degradeBy = if (item.name.contains("conjured", ignoreCase = true)) 2 else 1

        if (item.quality > 0) {
          item.quality -= degradeBy
        }

        if (item.sellIn < 0 && item.quality > 0) {
          item.quality -= degradeBy
        }
      }
    }
  }

  private fun increaseQualityWithinBounds(item: Item) {
    if (item.quality < 50) {
      ++item.quality
    }
  }
}
