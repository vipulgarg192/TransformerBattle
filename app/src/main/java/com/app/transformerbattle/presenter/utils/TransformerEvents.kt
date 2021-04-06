package com.app.transformerbattle.presenter.utils

import com.app.transformerbattle.domain.model.Transformer
import com.app.transformerbattle.network.model.TransformerDto

sealed class TransformerEvents {

    object UpdateTransformer : TransformerEvents()

    object GetTransformer : TransformerEvents()

    data class CreateTransformer(val transformer: Transformer) : TransformerEvents()

    object GetAutobotsTransformer : TransformerEvents()

    object GetDecepticonsTransformer : TransformerEvents()

    data class LetBattleTransformer(val autobots: TransformerDto, val decepticons: TransformerDto) : TransformerEvents()
}