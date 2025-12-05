package me.mozarez.aurora.mzlib.util.other

object ObjectUtil {
    fun <T> T.changeToIfNot(needToEqual: T, elseVal: T): T {
        return if (this != needToEqual) {
            elseVal
        } else {
            this
        }
    }

    fun <T> T.ifEqualException(needToEqual: T): T {
        return if (this != needToEqual) {
            throw Exception()
        } else {
            this
        }
    }

    fun <T> T.toIntOr(or: Int) : Int {
        return this.toString().toIntOrNull() ?: or
    }

}