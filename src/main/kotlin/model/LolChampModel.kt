package chistosito.model

import com.google.gson.annotations.SerializedName

data class LolChampModel(

    @SerializedName("id") var id: String? = null,
    @SerializedName("key") var key: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("lore") var lore: String? = null,
    @SerializedName("blurb") var blurb: String? = null,
    @SerializedName("allytips") var allytips: ArrayList<String> = arrayListOf(),
    @SerializedName("enemytips") var enemytips: ArrayList<String> = arrayListOf(),
    @SerializedName("tags") var tags: ArrayList<String> = arrayListOf(),
    @SerializedName("partype") var partype: String? = null,
    @SerializedName("recommended") var recommended: ArrayList<String> = arrayListOf()

)