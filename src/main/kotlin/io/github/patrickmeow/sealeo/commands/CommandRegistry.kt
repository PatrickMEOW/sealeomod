package io.github.patrickmeow.sealeo.commands

import com.github.stivais.commodore.Commodore
import com.github.stivais.commodore.nodes.Executable
import com.github.stivais.commodore.nodes.LiteralNode
import com.github.stivais.commodore.utils.findCorrespondingNode
import com.github.stivais.commodore.utils.getArgumentsRequired
import com.github.stivais.commodore.utils.getRootNode

object CommandRegistry {

    private val commands: ArrayList<Commodore> = arrayListOf(

    )

    fun add(vararg commands: Commodore) {
        commands.forEach { commodore ->
            CommandRegistry.commands.add(commodore)
        }
    }


    fun register() {
        commands.forEach { commodore ->
            commodore.register { problem, cause ->
                val builder = StringBuilder()

                builder.append("§c$problem\n\n")
                builder.append("  Did you mean to run:\n\n")
                buildTreeString(cause, builder)

                findCorrespondingNode(getRootNode(cause), "help")?.let {
                    builder.append("\n  §7Run /${getArgumentsRequired(it).joinToString(" ")} for more help.")
                }

            }
        }
    }


    private fun buildTreeString(from: LiteralNode, builder: StringBuilder) {
        for (node in from.children) {
            when (node) {
                is LiteralNode -> buildTreeString(node, builder)
                is Executable -> {
                    builder.append("  /${getArgumentsRequired(from).joinToString(" ")}")
                    for (parser in node.parsers) {

                    }
                    builder.append("\n")
                }
            }
        }
    }
}