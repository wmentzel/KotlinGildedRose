package guru.drako.examples.gildedrose

const val AGED_BRIE = "Aged Brie"
const val SULFURAS = "Sulfuras, Hand of Ragnaros"

class Shop(val items: List<Item>) {

  private enum class ItemType {
    AgedBrie, BackstagePasses, Conjured,
    UsualItem, SulfurasHandOfRagnaros
  }

  private val Item.type
    get() = when {
      this.name == AGED_BRIE -> ItemType.AgedBrie
      this.name == SULFURAS -> ItemType.SulfurasHandOfRagnaros
      this.name.startsWith("backstage passes", ignoreCase = true) -> ItemType.BackstagePasses
      this.name.startsWith("conjured", ignoreCase = true) -> ItemType.Conjured
      else -> ItemType.UsualItem
    }

  private fun Item.changeQualityBy(delta: Int) {
    quality = (quality + delta).coerceIn(0, 50)
  }

  fun runForOneDay() {
    items.forEach { item ->
      // quality
      when (item.type) {
        ItemType.AgedBrie -> item.changeQualityBy(if (item.sellIn > 0) 1 else 2)

        ItemType.BackstagePasses -> {
          item.changeQualityBy(
            when {
              item.sellIn > 10 -> 1
              item.sellIn > 5 -> 2
              item.sellIn > 0 -> 3
              else -> -item.quality
            }
          )
        }
        ItemType.Conjured -> item.changeQualityBy(if (item.sellIn > 0) -2 else -4)
        ItemType.UsualItem -> item.changeQualityBy(if (item.sellIn > 0) -1 else -2)
        ItemType.SulfurasHandOfRagnaros -> {
        }
      }

      // sell in
      if (item.type != ItemType.SulfurasHandOfRagnaros) {
        --item.sellIn
      }
    }
  }
}
