package com.reclycer.repertoire.ui

import com.reclycer.repertoire.data.Contact
import com.reclycer.repertoire.data.Message

data class MessageWrapper(val message: Message, val from: Contact?, val to: Contact?, val from_id_contact:String?)