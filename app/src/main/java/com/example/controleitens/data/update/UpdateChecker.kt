package com.example.controleitens.data.update

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class UpdateChecker {

    companion object {
        private const val API_URL =
            "https://api.github.com/repos/lufelcamargo/controle-de-itens/releases/latest"
    }

    suspend fun verificarAtualizacao(
        versaoAtual: String
    ): GitHubRelease? = withContext(Dispatchers.IO) {

        try {
            val url = URL(API_URL)

            val connection = url.openConnection() as HttpURLConnection

            connection.requestMethod = "GET"
            connection.setRequestProperty(
                "Accept",
                "application/vnd.github+json"
            )
            connection.setRequestProperty(
                "X-GitHub-Api-Version",
                "2022-11-28"
            )

            connection.connectTimeout = 10_000
            connection.readTimeout = 10_000

            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                connection.disconnect()
                return@withContext null
            }

            val resposta = connection.inputStream
                .bufferedReader()
                .use { it.readText() }

            connection.disconnect()

            val json = JSONObject(resposta)

            val tagName = json.getString("tag_name")

            val versaoDisponivel = tagName
                .removePrefix("v")
                .trim()

            val releaseNotes = json.optString("body", "")

            val assets = json.optJSONArray("assets")

            var downloadUrl: String? = null

            if (assets != null) {
                for (i in 0 until assets.length()) {
                    val asset = assets.getJSONObject(i)

                    val nome = asset.getString("name")

                    if (nome.endsWith(".apk", ignoreCase = true)) {
                        downloadUrl = asset.getString("browser_download_url")
                        break
                    }
                }
            }

            if (versaoEhMaisNova(versaoDisponivel, versaoAtual)) {
                GitHubRelease(
                    versionName = versaoDisponivel,
                    releaseNotes = releaseNotes,
                    downloadUrl = downloadUrl
                )
            } else {
                null
            }

        } catch (e: Exception) {
            null
        }
    }

    private fun versaoEhMaisNova(
        versaoDisponivel: String,
        versaoAtual: String
    ): Boolean {

        val disponivel = versaoDisponivel
            .split(".")
            .map { it.toIntOrNull() ?: 0 }

        val atual = versaoAtual
            .split(".")
            .map { it.toIntOrNull() ?: 0 }

        val tamanho = maxOf(disponivel.size, atual.size)

        for (i in 0 until tamanho) {
            val numeroDisponivel = disponivel.getOrElse(i) { 0 }
            val numeroAtual = atual.getOrElse(i) { 0 }

            if (numeroDisponivel > numeroAtual) {
                return true
            }

            if (numeroDisponivel < numeroAtual) {
                return false
            }
        }

        return false
    }
}