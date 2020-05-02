package intellij.music.core

fun <T> selectRandom(items: List<T>, probabilities: List<Double>): T {
    val p = Math.random()
    var cumulativeProbability = 0.0
    for ((item, itemProbability) in (items zip probabilities)) {
        cumulativeProbability += itemProbability
        if (p <= cumulativeProbability) {
            return item
        }
    }
    return items.last()
}
