package za.co.varsitycollage.st10050487.knights

import android.os.Parcel
import android.os.Parcelable

data class PlayerProfileModel(
    val playerId: Int,
    val name: String,
    val surname: String,
    val nickname: String,
    val age: Int,
    val grade: String,
    val height: String,
    val position: String,
    val dateOfBirth: String,
    val ageGroup: String,
    val userId: Int,
    var selected: Boolean = false,
    val profilePicture: ByteArray?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.createByteArray()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(playerId)
        parcel.writeString(name)
        parcel.writeString(surname)
        parcel.writeString(nickname)
        parcel.writeInt(age)
        parcel.writeString(grade)
        parcel.writeString(height)
        parcel.writeString(position)
        parcel.writeString(dateOfBirth)
        parcel.writeString(ageGroup)
        parcel.writeInt(userId)
        parcel.writeByte(if (selected) 1 else 0)
        parcel.writeByteArray(profilePicture)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlayerProfileModel> {
        override fun createFromParcel(parcel: Parcel): PlayerProfileModel {
            return PlayerProfileModel(parcel)
        }

        override fun newArray(size: Int): Array<PlayerProfileModel?> {
            return arrayOfNulls(size)
        }
    }
}