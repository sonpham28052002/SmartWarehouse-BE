package vn.edu.iuh.fit.smartwarehousebe.servies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.user.GetUserQuest;
import vn.edu.iuh.fit.smartwarehousebe.enums.Role;
import vn.edu.iuh.fit.smartwarehousebe.enums.UserStatus;
import vn.edu.iuh.fit.smartwarehousebe.repositories.UserRepository;
import vn.edu.iuh.fit.smartwarehousebe.models.User;
import vn.edu.iuh.fit.smartwarehousebe.specifications.UserSpecification;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class  UserService extends SoftDeleteService<User> implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Cacheable(value = "token", key = "#username", unless = "#result == null")
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUserName(username).orElseThrow(() -> new NoSuchElementException("User not found with name: " + username));
    }

    /**
     * get user by name
     * @param name
     * @return User
     */
    @Cacheable(value = "user", key = "#name", unless = "#result == null")
    public User getUserByName(String name) {
        return userRepository.findUserByUserName(name).orElseThrow(() -> new NoSuchElementException());
    }

    @Cacheable(value = "user", key = "#id", unless = "#result == null")
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
    }

    /**
     * create new user
     * @param user
     * @return User
     */
    @Cacheable(value = "user", unless = "#result == null")
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * update user
     * @param user
     * @return User
     */
    @CacheEvict(value = "user", allEntries = true)
    public User updateUser(User user) {
        User userOld = userRepository.findById(user.getId()).orElseThrow(() -> new NoSuchElementException());
        userOld.setUserName(user.getUsername());
        userOld.setEmail(user.getEmail());
        userOld.setFullName(user.getFullName());
        userOld.setPhoneNumber(user.getPhoneNumber());
        userOld.setAddress(user.getAddress());
        userOld.setRole(user.getRole());
        userOld.setDateOfBirth(user.getDateOfBirth());
        userOld.setSex(user.isSex());
        userOld.setProfilePicture(user.getProfilePicture());
        return userRepository.save(userOld);
    }

    /**
     * delete user by id
     * @param id
     */
    @CacheEvict(value = "users", allEntries = true)
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * get list user
     * @param pageRequest
     * @param userQuest
     * @return Page<User>
     */
    @Cacheable(value = "users", key = "#userQuest + '_' + #pageRequest.pageNumber + '_' + #pageRequest.pageSize")
    public Page<User> getUsers(PageRequest pageRequest, GetUserQuest userQuest) {
        Specification<User> spec = Specification.where(null);
        if (userQuest.getCode() != null) {
            spec = spec.and(UserSpecification.hasCode(userQuest.getCode()));
        }

        if (userQuest.getFullName() != null) {
            spec = spec.and(UserSpecification.hasName(userQuest.getFullName()));
        }

        if (userQuest.getStatus() != null) {
            spec = spec.and(UserSpecification.hasStatus(userQuest.getStatus().name()));
        }

        boolean includeDeleted = userQuest.getStatus() == UserStatus.DELETED || userQuest.getStatus() == null ? true : false;

        return userRepository.findAllUsers(spec, pageRequest, includeDeleted );
    }

    /**
     * get list user
     *
     * @param userQuest
     * @return Page<User>
     */
    @Cacheable(value = "user", key = "#userQuest")
    public List<User> getAllUser(GetUserQuest userQuest) {
        Specification<User> spec = Specification.where(null);
        if (userQuest.getCode() != null) {
            spec = spec.and(UserSpecification.hasCode(userQuest.getCode()));
        }

        if (userQuest.getFullName() != null) {
            spec = spec.and(UserSpecification.hasName(userQuest.getFullName()));
        }

        if (userQuest.getStatus() != null) {
            spec = spec.and(UserSpecification.hasStatus(userQuest.getStatus().name()));
        }

        if (userQuest.getStatus() == UserStatus.DELETED || userQuest.getStatus() == null ? true : false) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("deleted"), false));
        }

        return userRepository.findAll(spec);
    }

    @Cacheable(value = "users", key = "'getUsersManagerNotInWarehouse'")
    public List<User> getUsersManagerNotInWarehouse(){
        return userRepository.findUsersManagerNotInWarehouse();
    }

    @Cacheable(value = "users", key = "'getAllUserStaff'")
    public List<User> getAllUserStaff(){
        List<Integer> roles = Arrays.asList(Role.USER.getRole(), Role.SUPERVISOR.getRole());
        Specification<User> specification = UserSpecification.hasRoles(roles);
        specification = specification.and(UserSpecification.hasWareHouseIsNull());

        List<User> usersWithRoles = userRepository.getAllUser(specification, true);
        return usersWithRoles;
    }
}