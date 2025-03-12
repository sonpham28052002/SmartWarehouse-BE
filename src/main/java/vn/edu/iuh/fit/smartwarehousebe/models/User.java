package vn.edu.iuh.fit.smartwarehousebe.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.edu.iuh.fit.smartwarehousebe.enums.Role;
import vn.edu.iuh.fit.smartwarehousebe.enums.UserStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE user SET deleted = true, status = 'DELETED'  WHERE id = ?")
public class User extends Auditable implements UserDetails, Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "code", nullable = false, length = 12)
  private String code;

  @Column(name = "username", nullable = false, length = 255)
  private String userName;

  @Column(name = "password", nullable = false, length = 255)
  private String password;

  @Column(name = "email", length = 255)
  private String email;

  @Column(name = "phone_number", length = 20)
  private String phoneNumber;

  @Column(name = "full_name", length = 255)
  private String fullName;

  @Column(name = "address", columnDefinition = "TEXT")
  private String address;

  @Column(name = "date_of_birth")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDateTime dateOfBirth;

  @Column(name = "profile_picture", length = 255)
  private String profilePicture;

  private boolean sex;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private UserStatus status;

  @Enumerated(EnumType.ORDINAL)
  private Role role;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "warehouse_id")
  private Warehouse warehouse;

  @OneToOne(mappedBy = "manager")
  private Warehouse warehouseManager;

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(this.role.name()));
  }

  @Override
  public String getUsername() {
    return this.userName;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @PrePersist
  public void setDefault() {
    if (this.status == null) {
      this.status = UserStatus.ACTIVE;
    }

    if (this.profilePicture == null) {
      this.profilePicture = "https://www.shutterstock.com/image-vector/default-profile-picture-avatar-photo-600nw-1681253560.jpg";
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        '}';
  }
}

