package guru.drako.examples.gildedrose

enum class ItemName(val itemName: String) {
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
      when (ItemName.values().find { it.itemName == item.name }) {
        ItemName.AgedBrie -> {

          if (item.quality < 50) {
            ++item.quality
          }

          if (--item.sellIn < 0 && item.quality < 50) {
            ++item.quality
          }
        }
        ItemName.BackstagePasses -> {
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
        ItemName.Sulfuras -> {
        }
        ItemName.ElixirOfTheMongoose, ItemName.DexterityVest -> {
          if (item.quality > 0) {
            --item.quality
          }

          if (--item.sellIn < 0 && item.quality > 0) {
            --item.quality
          }
        }
        ItemName.ConjuredManaCake -> {

          if (item.quality > 0) {
            item.quality -= 2
          }

          if (--item.sellIn < 0 && item.quality > 0) {
            item.quality -= 2
          }
        }
      }
    }
  }
}
