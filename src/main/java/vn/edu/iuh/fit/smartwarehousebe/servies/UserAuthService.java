package vn.edu.iuh.fit.smartwarehousebe.servies;

import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.smartwarehousebe.models.User;
import vn.edu.iuh.fit.smartwarehousebe.repositories.UserRepository;

@Service
public class UserAuthService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  @Cacheable(value = "token", key = "#username", unless = "#result == null")
  public User loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findUserByUserName(username)
        .orElseThrow(() -> new NoSuchElementException("User not found with name: " + username));
  }
}
