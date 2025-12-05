package me.mozarez.aurora.mzlib.util.adventure

import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import java.time.Duration

class TitleBuilder {

    private var title : Component = Component.text("")
    private var subtitle : Component = Component.text("")
    private var times = Title.Times.times(Duration.ofMillis(500),Duration.ofMillis(500), Duration.ofMillis(500))

    fun getTitle() : Component = this.title
    fun getSubtitle() : Component = this.subtitle

    fun title(component: Component) : TitleBuilder {
        title = component
        return this
    }

    fun subtitle(component: Component) : TitleBuilder {
        subtitle = component
        return this
    }

    fun times(fadeInMillis: Long, stayMillis: Long, fadeOutMillis: Long) : TitleBuilder {
        times = Title.Times.times(Duration.ofMillis(fadeInMillis), Duration.ofMillis(stayMillis), Duration.ofMillis(fadeOutMillis))
        return this
    }

    fun build() : Title {
        return Title.title(title, subtitle)
    }

}