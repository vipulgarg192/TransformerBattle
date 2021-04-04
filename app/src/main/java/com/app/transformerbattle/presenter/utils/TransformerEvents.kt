package com.app.transformerbattle.presenter.utils

import com.app.transformerbattle.domain.model.Transformer

sealed class TransformerEvents {

    object UpdateTransformer : TransformerEvents()

    object GetTransformer : TransformerEvents()

    data class CreateTransformer(val transformer: Transformer) : TransformerEvents()

}