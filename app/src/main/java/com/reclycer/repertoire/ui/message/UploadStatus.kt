package com.reclycer.repertoire.ui.message

open class UploadStatus {

    object Empty : UploadStatus()
    object InProgress : UploadStatus()
    object Sent : UploadStatus()
    object Error : UploadStatus()


}