package guru.drako.examples.gildedrose

const val AGE_BRIE = "Aged Brie"
const val BACKSTAGE_PASSES = "Backstage passes to a TAFKAL80ETC concert"
const val DEXTERITY_VEST = "+5 Dexterity Vest"
const val ELIXIR_OF_THE_MONGOOSE = "Elixir of the Mongoose"
const val SULFURAS = "Sulfuras, Hand of Ragnaros"
const val CONJURED_MANA_CAKE = "Conjured Mana Cake"

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
        SULFURAS -> {
        }
        ELIXIR_OF_THE_MONGOOSE, DEXTERITY_VEST -> {
          if (item.quality > 0) {
            --item.quality
          }

          if (--item.sellIn < 0 && item.quality > 0) {
            --item.quality
          }
        }
        CONJURED_MANA_CAKE -> {

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
