package com.rishabh.chatapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {
    @Id
    private UUID userId; // at time of signup it's generated.

    @Column(nullable = false)
    private String name;

    @Column(unique = true, length = 100, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @JsonManagedReference
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_contacts",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_id")
    )
    private Set<User> contacts = new HashSet<>();

    @JsonBackReference
    @ManyToMany(mappedBy = "contacts")
    private Set<User> connectedUsers = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // Crucial: Use instanceOf check, and ensure 'id' is not null for comparison
        // Only compare IDs if both objects are persisted and have IDs
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId != null && Objects.equals(userId, user.userId); // Use Objects.equals for null-safety
    }

    @Override
    public int hashCode() {
        // If the ID is null (new entity), return a constant hash code.
        // This means all new entities will initially go into the same hash bucket.
        // Once persisted and an ID is assigned, the hash code changes,
        // which means you should *not* put unpersisted entities into sets
        // that are then managed by JPA, without careful consideration.
        // The safest approach: don't put unpersisted entities with null IDs into
        // Sets that participate in collection mappings unless you explicitly
        // re-add them after persistence.
        return userId == null ? 31 : userId.hashCode(); // A common practice, ensures stability after persistence
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
}