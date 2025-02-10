package io.github.patrickmeow.sealeo.utils.apiutils

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonElement
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils

object GetData {

    fun getHypixelData(apiKey: String): JsonObject? {
        val client: CloseableHttpClient = HttpClients.createDefault()
        val request = HttpGet("https://api.hypixel.net/v2/skyblock/profile?key=$apiKey&uuid=cbd1dabd-cbca-4a1a-beb6-5a6a5559b817")
        client.execute(request).use { response ->
            val entity = response.entity
            val jsonResponse = EntityUtils.toString(entity)
            return JsonParser.parseString(jsonResponse).asJsonObject
        }
    }

    fun parseHypixelData(apiKey: String) {
        val data = getHypixelData(apiKey)
        data?.let {
            val collections = it.getAsJsonObject()

            // Example: Store some collection data in variables

            println("Carrots Collected: $collections")

        }

    }

    fun getPlayersUUID(playerName: String) {
        
    }


}