@file:JvmName("Main")

package io.github.changwook987

import com.github.hanshsieh.pixivj.api.PixivApiClient
import com.github.hanshsieh.pixivj.exception.APIException
import dev.minn.jda.ktx.events.onCommand
import dev.minn.jda.ktx.interactions.commands.option
import dev.minn.jda.ktx.interactions.commands.slash
import dev.minn.jda.ktx.interactions.commands.updateCommands
import dev.minn.jda.ktx.jdabuilder.light
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import org.slf4j.LoggerFactory
import kotlin.system.exitProcess
import kotlin.time.Duration.Companion.minutes

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val token = if (args.isNotEmpty()) args[0]
        else System.getenv("TOKEN") ?: run {
            System.err.println("토큰이 누락되었습니다")
            exitProcess(-1)
        }

        val jda = light(token)
        var tokenProvider = TokenProvider()

        var client = PixivApiClient.Builder()
            .setTokenProvider(tokenProvider).build()

        val log = LoggerFactory.getLogger(this::class.java)

        jda.updateCommands {
            slash("검색", "픽시브에서 그림을 검색합니다") {
                option<String>("검색어", "그림을 검색할 검색어", required = true)
                option<Int>("개수", "검색할 그림의 개수", required = false)
            }
        }.queue()

        jda.onCommand("검색", timeout = 2.minutes) { commandEvent ->
            val guild = commandEvent.guild!!

            if (commandEvent.messageChannel.name != "씹덕짤") {
                val channel: TextChannel =
                    guild.textChannels.find { it.name == "씹덕짤" } ?: guild.createTextChannel("씹덕짤").complete()

                commandEvent.reply("검색은 ${channel.asMention}에서만 할 수 있어요").queue()
                return@onCommand
            }

            val query = commandEvent.getOption("검색어")!!.asString
            val count = commandEvent.getOption("개수")?.asInt ?: 1

            log.info("[{}] {}", commandEvent.user.asTag, query)

            val illustrations = try {
                client.search(query)
            } catch (e: APIException) {
                tokenProvider = TokenProvider(tokenProvider.getRefresherToken())
                client = PixivApiClient.Builder()
                    .setTokenProvider(tokenProvider).build()
                client.search(query)
            }

            if (illustrations.isEmpty()) commandEvent.reply("검색결과 없음").queue()
            else {
                commandEvent.reply("검색결과 입니다").queue()

                var i = 0
                val iter = illustrations.iterator()

                while (i++ < count && iter.hasNext()) {
                    val illustration = iter.next()
                    commandEvent.messageChannel.sendFiles(client.download(illustration).asSpoiler()).queue()
                }
            }
        }
    }
}