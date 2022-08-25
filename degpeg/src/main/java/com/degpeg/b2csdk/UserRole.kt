package com.degpeg.b2csdk

import android.os.Parcel
import android.os.Parcelable
import com.degpeg.utility.TextUtil
import java.util.*

enum class UserRole(private val id: Int, val value: String) : Parcelable {
    PUBLISHER(1, "publisher"),
    PROVIDER(2, "provider");

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeInt(id)
        parcel.writeString(value)
    }

    inline fun <reified T : Any> typeCall(
        publisher: (UserRole?) -> T,
        provider: (UserRole?) -> T
    ) {
        when (this) {
            PUBLISHER -> publisher.invoke(this)
            PROVIDER -> provider.invoke(this)
        }
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<UserRole> = object : Parcelable.Creator<UserRole> {
            override fun createFromParcel(parcel: Parcel): UserRole {
                return values()[parcel.readInt()]
            }

            override fun newArray(size: Int): Array<UserRole?> {
                return arrayOfNulls(size)
            }
        }

        //Lookup table
        private val lookup: MutableMap<String, UserRole> = HashMap()

        //This method can be used for reverse lookup purpose
        operator fun get(value: String?): UserRole {
            return if (TextUtil.isNullOrEmpty(value)) PUBLISHER else lookup[value]
                ?: PUBLISHER
        }

        //Populate the lookup table on loading time
        init {
            for (type in values()) {
                lookup[type.value] = type
            }
        }
    }
}