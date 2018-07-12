package com.chatbot.kahina.service.mapper;

import com.chatbot.kahina.domain.*;
import com.chatbot.kahina.service.dto.DepenceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Depence and its DTO DepenceDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DepenceMapper extends EntityMapper<DepenceDTO, Depence> {



    default Depence fromId(Long id) {
        if (id == null) {
            return null;
        }
        Depence depence = new Depence();
        depence.setId(id);
        return depence;
    }
}
