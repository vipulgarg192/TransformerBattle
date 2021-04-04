package com.app.transformerbattle.network.model

import com.app.transformerbattle.domain.model.Transformer
import com.app.transformerbattle.domain.model.TransformerList
import com.app.transformerbattle.domain.util.DomainMapper

class TransformerDtoMapper : DomainMapper<TransformerDto, Transformer> {

    override fun mapToDomainModel(model: TransformerDto): Transformer {
        return Transformer(
            id = model.id,
            name = model.name,
            strength = model.strength,
            intelligence = model.intelligence,
            speed = model.speed,
            endurance = model.endurance,
            rank = model.rank,
            firepower = model.firepower,
            skill = model.skill,
            courage = model.courage,
            team = model.team,
            team_icon = model.team_icon
        )
    }

    fun mapToDomainModel(model: TransformerListDto): TransformerList {
        return TransformerList(
            transformerList = model.transformer as List<Transformer>
        )
    }

    override fun mapFromDomainModel(domainModel: Transformer): TransformerDto {
        return TransformerDto(
            id = domainModel.id!!,
            name = domainModel.name,
            strength = domainModel.strength,
            intelligence = domainModel.intelligence,
            speed = domainModel.speed,
            endurance = domainModel.endurance,
            rank = domainModel.rank,
            courage = domainModel.courage,
            firepower = domainModel.firepower,
            skill = domainModel.skill,
            team = domainModel.team,
            team_icon = domainModel.team_icon!!
        )
    }

    fun toDomainList(initial: List<TransformerDto>): List<Transformer>{
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Transformer>): List<TransformerDto>{
        return initial.map { mapFromDomainModel(it) }
    }
}