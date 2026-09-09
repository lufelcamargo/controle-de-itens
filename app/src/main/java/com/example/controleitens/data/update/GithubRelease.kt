package com.example.controleitens.data.update

data class GitHubRelease(
    val versionName: String,
    val releaseNotes: String,
    val downloadUrl: String?
)