package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Entitya;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Entitya entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntityaRepository extends JpaRepository<Entitya, Long> {

}
