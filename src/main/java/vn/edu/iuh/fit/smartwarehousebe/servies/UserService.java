package vn.edu.iuh.fit.smartwarehousebe.servies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.smartwarehousebe.repositories.UserRepository;
import vn.edu.iuh.fit.smartwarehousebe.models.User;

@Service
public class UserService extends SoftDeleteService<User> implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByName(username).orElseThrow();
    }

    @Autowired
    private Environment environment;

    // Sử dụng @Cacheable để cache kết quả khi tìm user
    @Cacheable(value = "user", key = "#name", unless = "#result == null")
    public User getUserByName(String name) {
        return userRepository.findByName(name).get();
    }

    // Sử dụng @CachePut để cập nhật cache khi thêm mới user
    @CachePut(value = "user", key = "#user.id")
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Sử dụng @CacheEvict để xóa cache khi xóa user
    @CacheEvict(value = "user", key = "#id")
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}