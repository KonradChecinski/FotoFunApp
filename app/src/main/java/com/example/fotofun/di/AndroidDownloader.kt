package com.example.fotofun.di

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri

class AndroidDownloader (private val context: Context):Downloader{
    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    override fun downloadFile(url: String): Long {
        val request = DownloadManager.Request(url.toUri())
            .setMimeType("application/pdf")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle("FotoPaper.pdf")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "FotoPaper.pdf")
        return downloadManager.enqueue(request)
    }
}