package br.com.athoosz.events.repositories;

import br.com.athoosz.events.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository  extends CrudRepository<User, Integer> {
    public User findByEmail(String email);
}
