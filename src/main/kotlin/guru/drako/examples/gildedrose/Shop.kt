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
      name == AGED_BRIE -> ItemType.AgedBrie
      name == SULFURAS -> ItemType.SulfurasHandOfRagnaros
      name.startsWith("backstage passes", ignoreCase = true) -> ItemType.BackstagePasses
      name.startsWith("conjured", ignoreCase = true) -> ItemType.Conjured
      else -> ItemType.UsualItem
    }

  private fun Item.ageOneDay() {

    fun Item.changeQualityBy(delta: Int) {
      quality = (quality + delta).coerceIn(0, 50)
    }

    when (type) {
      ItemType.AgedBrie -> changeQualityBy(if (sellIn > 0) 1 else 2)

      ItemType.BackstagePasses -> {
        changeQualityBy(
          when {
            sellIn > 10 -> 1
            sellIn > 5 -> 2
            sellIn > 0 -> 3
            else -> -quality
          }
        )
      }
      ItemType.Conjured -> changeQualityBy(if (sellIn > 0) -2 else -4)
      ItemType.UsualItem -> changeQualityBy(if (sellIn > 0) -1 else -2)
      ItemType.SulfurasHandOfRagnaros -> {
      }
    }

    if (type != ItemType.SulfurasHandOfRagnaros) {
      --sellIn
    }
  }

  fun runForOneDay() {
    items.forEach { it.ageOneDay() }
  }
}
