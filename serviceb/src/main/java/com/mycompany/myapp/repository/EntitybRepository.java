package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Entityb;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Entityb entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntitybRepository extends JpaRepository<Entityb, Long> {

}
