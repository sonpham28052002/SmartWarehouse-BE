package vn.edu.iuh.fit.smartwarehousebe.servies;

import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.user.GetUserQuest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.user.UserImportRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.user.UserResponse;
import vn.edu.iuh.fit.smartwarehousebe.enums.Role;
import vn.edu.iuh.fit.smartwarehousebe.enums.UserStatus;
import vn.edu.iuh.fit.smartwarehousebe.exceptions.UserCodeNotValid;
import vn.edu.iuh.fit.smartwarehousebe.exceptions.UserNotFoundException;
import vn.edu.iuh.fit.smartwarehousebe.mappers.UserMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.User;
import vn.edu.iuh.fit.smartwarehousebe.repositories.UserRepository;
import vn.edu.iuh.fit.smartwarehousebe.specifications.UserSpecification;
import vn.edu.iuh.fit.smartwarehousebe.utils.helpers.UserCsvHelper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService extends CommonService<User> {

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  /**
   * get user by name
   *
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
   *
   * @param user
   * @return User
   */
  @CacheEvict(value = {"warehouse", "user"}, allEntries = true)
  public User createUser(User user) {
    user.setPassword(passwordEncoder.encode("11111"));
    return userRepository.save(user);
  }

  /**
   * update user
   *
   * @param user
   * @return User
   */
  @CacheEvict(value = {"warehouse", "user"}, allEntries = true)
  public User updateUser(User user) {
    User userOld = userRepository.findById(user.getId())
        .orElseThrow(() -> new NoSuchElementException());
    userOld.setUserName(user.getUsername());
    userOld.setEmail(user.getEmail());
    userOld.setFullName(user.getFullName());
    userOld.setPhoneNumber(user.getPhoneNumber());
    userOld.setAddress(user.getAddress());
    userOld.setRole(user.getRole());
    userOld.setDateOfBirth(user.getDateOfBirth());
    userOld.setSex(user.isSex());
    userOld.setCode(user.getCode());
    userOld.setProfilePicture(user.getProfilePicture());
    return userRepository.save(userOld);
  }

  /**
   * delete user by id
   *
   * @param id
   */
  @CacheEvict(value = "user", allEntries = true)
  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }

  /**
   * get list user
   *
   * @param pageRequest
   * @param userQuest
   * @return Page<User>
   */
  @Cacheable(value = "user", key = "#userQuest + '_' + #pageRequest.pageNumber + '_' + #pageRequest.pageSize")
  public Page<UserResponse> getUsers(PageRequest pageRequest, GetUserQuest userQuest) {
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

    boolean includeDeleted =
        userQuest.getStatus() == UserStatus.DELETED || userQuest.getStatus() == null ? true
            : false;

    return userRepository.findAllUsers(spec, pageRequest, includeDeleted)
        .map(i -> UserMapper.INSTANCE.toDto(i));
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

    if (userQuest.getStatus() == UserStatus.DELETED || userQuest.getStatus() == null ? true
        : false) {
      spec = spec.and((root, query, cb) -> cb.equal(root.get("deleted"), false));
    }

    return userRepository.findAll(spec);
  }

  @Cacheable(value = "user", key = "'getUsersManagerNotInWarehouse'")
  public List<User> getUsersManagerNotInWarehouse() {
    return userRepository.findUsersManagerNotInWarehouse();
  }

  @Cacheable(value = "user", key = "'getAllUserStaff'")
  public List<User> getAllUserStaff() {
    List<Integer> roles = Arrays.asList(Role.USER.getRole(), Role.SUPERVISOR.getRole());
    Specification<User> specification = UserSpecification.hasRoles(roles);
    specification = specification.and(UserSpecification.hasWareHouseIsNull());

    List<User> usersWithRoles = userRepository.getAllUser(specification, true);
    return usersWithRoles;
  }

  /**
   * Imports users from a CSV file.
   *
   * @param file the CSV file containing user data
   * @return the number of users successfully imported
   * @throws IllegalArgumentException if there is an error reading the file or if any user code is
   *                                  not valid
   */
  public Integer importUser(MultipartFile file) {
    try {
      List<UserImportRequest> userRequests = UserCsvHelper.csvToUserRequest(file.getInputStream());
      boolean notValid = userRequests.stream()
          .anyMatch(userRequest -> !validateCode(userRequest.getCode()));
      if (notValid) {
        throw new UserCodeNotValid();
      }
      return userRepository.saveAll(
          userRequests.stream().map(UserMapper.INSTANCE::toEntity).toList()).size();
    } catch (IOException | UserCodeNotValid e) {
      throw new IllegalArgumentException(e);
    }
  }

  private boolean validateCode(String code) {
    String regex = "USER-\\d{5}";
    return code.matches(regex);
  }

  /**
   * Get the current authenticated user's ID or a default admin ID if no user is authenticated
   *
   * @return the ID of the current authenticated user or a default admin ID
   */
  public Long getCurrentUserId() {
    try {
      // Try to get the current authenticated user
      Authentication authentication =
          SecurityContextHolder.getContext().getAuthentication();

      if (authentication != null && authentication.isAuthenticated() &&
          authentication.getPrincipal() instanceof User user) {
        return user.getId();
      }
      throw new UserNotFoundException();
    } catch (Exception e) {
      throw new UserNotFoundException();
    }
  }

  public UserResponse getUserByCode(String userCode) {
    return userMapper.toDto(userRepository.findByCode(userCode)
        .orElseThrow(UserNotFoundException::new));
  }

  @PostConstruct
  public void init() {
    User user = userRepository.findById(1L).orElse(null);
    if (user != null) {
      return;
    }

    User newUser = User.builder()
        .sex(false)
        .status(UserStatus.ACTIVE)
        .userName("admin")
        .password(passwordEncoder.encode("admin2025"))
        .role(Role.ADMIN)
        .code("USER-00001")
        .phoneNumber("0346676956")
        .email("sonpham28052002@gmail.com")
        .dateOfBirth(LocalDate.of(2002, 5, 28).atStartOfDay())
        .build();

    userRepository.save(newUser);
  }

}