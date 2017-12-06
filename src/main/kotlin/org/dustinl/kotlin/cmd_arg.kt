package org.dustinl.kotlin

import com.xenomachina.argparser.ArgParser

class MyArgs (parser: ArgParser){
    val command by parser.positionalList("COMMAND", 0..1)
    val silent by parser.flagging("-s", help = "silent")
    val verbose by parser.flagging("-v", help = "verbose")
}

fun main(args: Array<String>) {
    val args = MyArgs(ArgParser(args))
    println(args.command)
    println(args.silent)
    println(args.verbose)
}