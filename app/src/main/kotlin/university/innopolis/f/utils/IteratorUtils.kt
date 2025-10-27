package university.innopolis.f.utils

object IteratorUtils {
    fun <T> Iterator<T>.nextOrNull(): T? {
        if (!this.hasNext()) {
            return null
        }
        return this.next()
    }
}
