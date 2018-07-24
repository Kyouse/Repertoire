package com.reclycer.repertoire.data

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable


@DatabaseTable(tableName = "T_Message")
class Message {

    @DatabaseField(columnName = "idMessage", generatedId = true)
    var local_id: Int = 0
    @DatabaseField
    var from_id: String? = null
    @DatabaseField
    var to_id: String? = null
    @DatabaseField
    var body: String? = null
    @DatabaseField
    var date: String? = null
    @DatabaseField(columnName = Message.COLUMN_NAME_SYNC_ID)
    var sync_id: String? = null



    constructor(from_id: String, to_id: String, body: String?, date: String?, sync_id: String?) {
        this.from_id = from_id
        this.to_id = to_id
        this.body = body
        this.date = date
        this.sync_id = sync_id
    }

    constructor() {}

    companion object {
        const val COLUMN_NAME_SYNC_ID="SYNC_ID"
    }

}