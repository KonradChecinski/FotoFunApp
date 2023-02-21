package com.example.fotofun.di

interface Downloader {
    fun downloadFile(url: String): Long
}