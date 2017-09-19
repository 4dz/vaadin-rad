package demo.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
//import org.springframework.domain.rest.core.annotation.RepositoryRestResource;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {

}

