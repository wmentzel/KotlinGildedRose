package guru.drako.examples.gildedrose

enum class ItemNames(val itemName: String) {
  AgedBrie("Aged Brie"),
  BackstagePasses("Backstage passes to a TAFKAL80ETC concert"),
  DexterityVest("+5 Dexterity Vest"),
  ElixirOfTheMongoose("Elixir of the Mongoose"),
  Sulfuras("Sulfuras, Hand of Ragnaros"),
  ConjuredManaCake("Conjured Mana Cake")
}

class Shop(val items: List<Item>) {
  fun runForOneDay() {
    for (item in items) {

      when (ItemNames.values().find { it.itemName == item.name }) {
        ItemNames.AgedBrie -> {

          if (item.quality < 50) {
            ++item.quality
          }

          if (--item.sellIn < 0 && item.quality < 50) {
            ++item.quality
          }
        }
        ItemNames.BackstagePasses -> {
          if (item.quality < 50) {
            ++item.quality
          }

          if (item.sellIn < 11 && item.quality < 50) {
            ++item.quality
          }

          if (item.sellIn < 6 && item.quality < 50) {
            ++item.quality
          }

          if (--item.sellIn < 0) {
            item.quality -= item.quality
          }
        }
        ItemNames.Sulfuras -> {
        }
        ItemNames.DexterityVest -> {
          if (item.quality > 0) {
            --item.quality
          }

          if (--item.sellIn < 0 && item.quality > 0) {
            --item.quality
          }
        }
        ItemNames.ElixirOfTheMongoose -> {
          if (item.quality > 0) {
            if (item.name != "Sulfuras, Hand of Ragnaros" && !item.name.contains("conjured", ignoreCase = true)) {
              --item.quality
            }

            if (item.name.contains("conjured", ignoreCase = true)) {
              item.quality -= 2
            }
          }
          --item.sellIn

          if (item.sellIn < 0) {
            if (item.quality > 0) {
              if (item.name != "Sulfuras, Hand of Ragnaros" && !item.name.contains("conjured", ignoreCase = true)) {
                --item.quality
              }

              if (item.name.contains("conjured", ignoreCase = true)) {
                item.quality -= 2
              }
            }
          }
        }
        ItemNames.ConjuredManaCake -> {
          if (item.quality > 0) {
            if (item.name != "Sulfuras, Hand of Ragnaros" && !item.name.contains("conjured", ignoreCase = true)) {
              --item.quality
            }

            if (item.name.contains("conjured", ignoreCase = true)) {
              item.quality -= 2
            }
          }
          --item.sellIn

          if (item.sellIn < 0) {
            if (item.quality > 0) {
              if (item.name != "Sulfuras, Hand of Ragnaros" && !item.name.contains("conjured", ignoreCase = true)) {
                --item.quality
              }

              if (item.name.contains("conjured", ignoreCase = true)) {
                item.quality -= 2
              }
            }
          }
        }
      }
    }
  }
}
