package com.app.transformerbattle.network.model

import com.google.gson.annotations.SerializedName

data class TransformerListDto(

    @SerializedName("transformers")
    var transformer: ArrayList<TransformerDto>? = null

)
