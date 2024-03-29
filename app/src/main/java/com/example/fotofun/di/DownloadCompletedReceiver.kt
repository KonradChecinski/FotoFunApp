package com.example.fotofun.di

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class DownloadCompletedReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == "android.intent.action.DOWNLOAD_COMPLETE"){
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)

            if(id != -1L){
                Log.d("gromzi", "Download with ID $id finished!")
            }
        }
    }
}