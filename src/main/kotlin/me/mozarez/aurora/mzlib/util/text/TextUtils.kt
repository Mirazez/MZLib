package me.mozarez.aurora.mzlib.util.text

object TextUtils {

    fun String.replaceLast(oldValue: String, newValue: String, ignoreCase: Boolean = false): String {
        val lastIndex = this.lastIndexOf(oldValue, ignoreCase = ignoreCase)
        return if (lastIndex == -1) {
            this
        } else {
            this.substring(0, lastIndex) + newValue + this.substring(lastIndex + oldValue.length)
        }
    }

}