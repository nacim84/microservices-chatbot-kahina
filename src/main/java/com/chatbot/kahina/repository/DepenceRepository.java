package com.chatbot.kahina.repository;

import com.chatbot.kahina.domain.Depence;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Depence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepenceRepository extends JpaRepository<Depence, Long> {

}
