package guru.drako.examples.gildedrose

const val AGE_BRIE = "Aged Brie"
const val BACKSTAGE_PASSES = "Backstage passes to a TAFKAL80ETC concert"
const val SULFURAS = "Sulfuras, Hand of Ragnaros"

class Shop(val items: List<Item>) {
  fun runForOneDay() {
    for (item in items) {

      when (item.name) {
        AGE_BRIE -> {
          if (item.quality < 50) {
            ++item.quality
          }

          if (--item.sellIn < 0 && item.quality < 50) {
            ++item.quality
          }
        }
        BACKSTAGE_PASSES -> {
          if (item.quality < 50) {
            ++item.quality
          }

          if (item.sellIn <= 10 && item.quality < 50) {
            ++item.quality
          }

          if (item.sellIn <= 5 && item.quality < 50) {
            ++item.quality
          }

          if (--item.sellIn < 0) {
            item.quality -= item.quality
          }
        }
        SULFURAS -> { /* do nothing */
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
}
