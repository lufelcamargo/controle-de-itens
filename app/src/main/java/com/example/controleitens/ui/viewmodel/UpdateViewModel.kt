package com.example.controleitens.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.controleitens.data.update.GitHubRelease
import com.example.controleitens.data.update.UpdateChecker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class UpdateViewModel : ViewModel() {
    private val _progressoDownload = MutableStateFlow(0f)
    val progressoDownload: StateFlow<Float> =
        _progressoDownload.asStateFlow()

    private val _baixandoAtualizacao = MutableStateFlow(false)
    val baixandoAtualizacao: StateFlow<Boolean> =
        _baixandoAtualizacao.asStateFlow()

    private val updateChecker = UpdateChecker()

    private val _atualizacaoDisponivel =
        MutableStateFlow<GitHubRelease?>(null)

    val atualizacaoDisponivel: StateFlow<GitHubRelease?> =
        _atualizacaoDisponivel.asStateFlow()

    fun verificarAtualizacao(versaoAtual: String) {
        viewModelScope.launch {
            val resultado =
                updateChecker.verificarAtualizacao(versaoAtual)

            Log.d(
                "UpdateChecker",
                "Versão encontrada: ${resultado?.versionName}"
            )

            Log.d(
                "UpdateChecker",
                "URL do APK: ${resultado?.downloadUrl}"
            )

            _atualizacaoDisponivel.value = resultado
        }
    }
    fun baixarAtualizacao(
        context: Context,
        release: GitHubRelease,
        onSuccess: (File) -> Unit = {},
        onError: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _baixandoAtualizacao.value = true
            _progressoDownload.value = 0f

            try {
                val arquivo = withContext(Dispatchers.IO) {
                    val url = release.downloadUrl
                        ?: throw IllegalStateException(
                            "URL do APK não encontrada."
                        )

                    val connection =
                        URL(url).openConnection() as HttpURLConnection

                    connection.requestMethod = "GET"
                    connection.connectTimeout = 10_000
                    connection.readTimeout = 30_000

                    if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                        throw IllegalStateException(
                            "Falha no download: ${connection.responseCode}"
                        )
                    }

                    val tamanhoTotal = connection.contentLengthLong

                    val arquivoDestino = File(
                        context.cacheDir,
                        "controle-itens-${release.versionName}.apk"
                    )

                    connection.inputStream.use { input ->
                        arquivoDestino.outputStream().use { output ->

                            val buffer = ByteArray(8 * 1024)
                            var totalBaixado = 0L
                            var bytesLidos: Int

                            while (input.read(buffer).also {
                                    bytesLidos = it
                                } != -1
                            ) {
                                output.write(buffer, 0, bytesLidos)
                                totalBaixado += bytesLidos

                                if (tamanhoTotal > 0) {
                                    _progressoDownload.value =
                                        totalBaixado.toFloat() /
                                                tamanhoTotal.toFloat()
                                }
                            }
                        }
                    }

                    connection.disconnect()

                    arquivoDestino
                }

                Log.d(
                    "UpdateChecker",
                    "APK baixado: ${arquivo.absolutePath}"
                )

                _progressoDownload.value = 1f
                _baixandoAtualizacao.value = false

                onSuccess(arquivo)

            } catch (e: Exception) {
                Log.e(
                    "UpdateChecker",
                    "Erro ao baixar atualização",
                    e
                )

                _baixandoAtualizacao.value = false
                _progressoDownload.value = 0f

                onError()
            }
        }
    }
    fun instalarAtualizacao(
        context: Context,
        arquivo: File
    ) {
        try {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                arquivo
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(
                    uri,
                    "application/vnd.android.package-archive"
                )
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(intent)

        } catch (e: Exception) {
            Log.e(
                "UpdateChecker",
                "Erro ao iniciar instalação",
                e
            )
        }
    }
}