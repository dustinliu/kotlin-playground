package org.dustinl.kotlin.dsl

class PokerConfiguration {
    private val containers = mutableListOf<Container>()

    fun container(init: Container.() -> Unit) {
        containers.add(Container().apply(init))
    }
}

class Container {
    var name: String = ""
    var type: Type = Type.JETTY

    enum class Type {
        JETTY
    }
}
