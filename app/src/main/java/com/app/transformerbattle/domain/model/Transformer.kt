package com.app.transformerbattle.domain.model

data class Transformer(
     val id: String?,
     val name: String,
     val strength: Int,
     val intelligence: Int,
     val speed: Int,
     val endurance: Int,
     val rank: Int,
     val firepower: Int,
     val skill: Int,
     val courage: Int,
     val team: String,
     val team_icon: String?
)
