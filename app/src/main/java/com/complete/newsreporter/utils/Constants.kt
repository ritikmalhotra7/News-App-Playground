package com.complete.newsreporter.utils

import android.app.Activity
import com.complete.newsreporter.model.Situations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

object Constants {
        const val API_KEY = "d1bd835912d245aaa3bbe7a8af36fdd7"
        const val BASE_URL = "https://newsapi.org"
        const val QUERY_SIZE = 20
        const val SHARED = "shared"

        const val ABP_NEWS = "https://www.youtube.com/watch?v=nyd-xznCpJc&ab_channel=ABPNEWS"
        const val NDTV_NEWS = "https://www.youtube.com/watch?v=WB-y7_ymPJ4&ab_channel=NDTV"
        const val INDIA_TODAY_NEWS = "https://www.indiatoday.in/livetv"
        const val AAJ_TAK = "https://youtu.be/Cy_6-_XUW-c"
        const val GNT_NEWS = "https://www.indiatoday.in/gnt-livetv"
        const val CNN = "https://www.youtube.com/watch?v=kHSdkpoHXBw&ab_channel=CNN-News18"

        val arr = arrayListOf<String>("in","us","ru","ua","au","cn","es","pk","np","lk")
        var REGION = arr[0]
        var pos = 0
        var urlStorage = "https://firebasestorage.googleapis.com/v0/b"
        var firebaseAuth = FirebaseAuth.getInstance()
        var dbReferenceUsers = FirebaseDatabase.getInstance("https://pocket-news-298cf-default-rtdb.asia-southeast1.firebasedatabase.app/").reference.child("users")
        var storageReference = FirebaseStorage.getInstance("gs://pocket-news-298cf.appspot.com/")


        fun setRegion(ctx: Activity,position: Int) {
            ctx.save("region", arr[position])
            ctx.save("pos", position.toString())
            pos = position
            REGION = ctx.read("region")
        }
        val medicalSituations = arrayListOf<Situations>(
            Situations("Bleeding","You can start bleeding from even the smallest cut and bruises. But this is not something worth panicking about at that moment. The emergency case arises when it turns into deep cuts and severe bruises that require immediate attention. The condition in which you should seek emergency are:\n" +
                    "\n" +
                    "You cannot control the bleeding even with proper first aid treatment.\n" +
                    "An object that pierced through your kin, and you cannot get it out because it’s too deep.\n" +
                    "You can see your bone or tissue.\n" +
                    "If you delay the process, then the excess loss of blood may lead to dizziness, unwell feeling, pale face, and in some cases, you lose your consciousness. However, if this happens, it is a matter of urgency.")
        )
        fun getDate():String{
            val c = Calendar.getInstance()
            val date = c.get(Calendar.DATE)
            val month = c.get(Calendar.MONTH)+1
            val year = c.get(Calendar.YEAR)


            return "$date/$month/$year"
        }
        fun getTime():String {
            val c = Calendar.getInstance()
            var hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)
            val seconds = c.get(Calendar.SECOND)
            var timeStamp = ""
            var am = "am"
            if (minute < 10) {
                if (hour == 0) {
                    hour = 12
                }
                if (hour > 12) {
                    hour -= 12
                    am = "pm"
                }
                timeStamp = "$hour:0$minute $am"
            } else {
                if (hour == 0) {
                    hour = 12
                }
                if (hour > 12) {
                    hour -= 12
                    am = "pm"
                }
                timeStamp = "$hour:$minute $am"
            }
            return timeStamp;
        }
}