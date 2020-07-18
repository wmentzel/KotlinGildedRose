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

    if (item.name in listOf(AGE_BRIE, BACKSTAGE_PASSES)) {
      if (item.quality < 50) {
        ++item.quality
      }
    }

    when (item.name) {
      AGE_BRIE -> {
        if (--item.sellIn < 0 && item.quality < 50) {
          ++item.quality
        }
      }
      BACKSTAGE_PASSES -> {

        listOf(10, 5).forEach {
          if (item.sellIn <= it && item.quality < 50) {
            ++item.quality
          }
        }

        if (--item.sellIn < 0) {
          item.quality = 0
        }
      }
      else -> {

        val degradeBy = if (item.name.contains("conjured", ignoreCase = true)) 2 else 1

        if (item.quality > 0) {
          item.quality -= degradeBy
        }

        if (--item.sellIn < 0 && item.quality > 0) {
          item.quality -= degradeBy
        }
      }
    }
  }
}
