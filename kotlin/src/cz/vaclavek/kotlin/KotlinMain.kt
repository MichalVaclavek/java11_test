@file:JvmName("DemoFile")
package  cz.vaclavek.kotlin

class KotlinMain {

    fun input(args: Array<String>) {
        println("What is your name?")
        val name = readLine()
        println("Hello, $ (name)")
    }
}