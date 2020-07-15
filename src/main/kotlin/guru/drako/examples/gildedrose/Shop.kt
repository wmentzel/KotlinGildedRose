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
          --item.sellIn
        }
        ItemNames.BackstagePasses -> {
          if (item.quality < 50) {
            ++item.quality
          }

          if (item.sellIn < 11) {
            if (item.quality < 50) {
              ++item.quality
            }
          }

          if (item.sellIn < 6) {
            if (item.quality < 50) {
              ++item.quality
            }
          }

          --item.sellIn
        }
        ItemNames.Sulfuras -> {
          degrateQuality(item)
        }
        ItemNames.DexterityVest -> {
          degrateQuality(item)
          --item.sellIn
        }
        ItemNames.ElixirOfTheMongoose -> {
          degrateQuality(item)
          --item.sellIn
        }
        ItemNames.ConjuredManaCake -> {
          degrateQuality(item)
          --item.sellIn
        }
      }

      if (item.sellIn < 0) {
        if (item.name != "Aged Brie") {
          if (item.name != "Backstage passes to a TAFKAL80ETC concert") {
            degrateQuality(item)
          } else {
            item.quality -= item.quality
          }
        } else {
          if (item.quality < 50) {
            ++item.quality
          }
        }
      }
    }
  }

  private fun degrateQuality(item: Item) {
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
