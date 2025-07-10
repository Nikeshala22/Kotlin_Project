package com.example.fintrack

import android.os.Parcel
import android.os.Parcelable

data class Transaction(
    val id: Int,
    val title: String,
    val category: String,
    val amount: Double,
    val date: String,
    val isExpense: Boolean,
    val note: String,
    val account: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),                         // id
        parcel.readString() ?: "",                // title
        parcel.readString() ?: "",                // category
        parcel.readDouble(),                      // amount
        parcel.readString() ?: "",                // date
        parcel.readByte() != 0.toByte(),          // isExpense
        parcel.readString() ?: "",                // note
        parcel.readString() ?: ""                 // account
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(category)
        parcel.writeDouble(amount)
        parcel.writeString(date)
        parcel.writeByte(if (isExpense) 1 else 0)
        parcel.writeString(note)
        parcel.writeString(account)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Transaction> {
        override fun createFromParcel(parcel: Parcel): Transaction = Transaction(parcel)
        override fun newArray(size: Int): Array<Transaction?> = arrayOfNulls(size)
    }
}
