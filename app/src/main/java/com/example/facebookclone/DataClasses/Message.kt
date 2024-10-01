package com.example.facebookclone.DataClasses

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Message(
    val senderId: String = "",
    val receiverId: String = "",
    val text: String = "",
    val timestamp: Long = 0L,
    val type: String = "text",
    val isRead: Boolean = false,
    val isEdited: Boolean = false,
    val isDeleted: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(senderId)
        parcel.writeString(receiverId)
        parcel.writeString(text)
        parcel.writeLong(timestamp)
        parcel.writeString(type)
        parcel.writeByte(if (isRead) 1 else 0)
        parcel.writeByte(if (isEdited) 1 else 0)
        parcel.writeByte(if (isDeleted) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Message> {
        override fun createFromParcel(parcel: Parcel): Message {
            return Message(parcel)
        }

        override fun newArray(size: Int): Array<Message?> {
            return arrayOfNulls(size)
        }
    }
}