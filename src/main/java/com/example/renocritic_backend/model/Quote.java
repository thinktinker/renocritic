package com.example.renocritic_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "quote")
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;                                                                 // Integer Id (Auto-increment)

    @Column(nullable = false)
    @NotNull(message = "Submission id must not be null.")
    @Min(value = 1, message = "Submission id cannot be negative.")
    Integer submission_id;                                                      // Integer submission_id

    @Column(nullable = false)
    @NotBlank(message = "Work description must not be empty.")
    @Size(min = 5, message = "Minimum 5 characters for work description.")
    String work_description;                                                    // String work_description

    @Column(nullable = false)
    @NotNull(message = "Quantity must not be empty.")
    Integer quantity;                                                           // Integer quantity

    @Column(nullable = false)
    @NotNull(message = "Unit rate must not be empty.")
    Double unitRate;                                                            // Double unitRate

    @Column(nullable = false)                                                   // Double price
    @NotNull(message = "Price must not be empty.")
    Double price;

    @Column(nullable = false)                                                   // EnumVerdict
    @Enumerated(EnumType.STRING)
    @Builder.Default
    EnumVerdict verdict = EnumVerdict.Fair;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)                        // relationship between Quotes and User
    @JoinColumn(name = "user_id", nullable = false)                             // Many Quotes come from a User
    @OnDelete(action = OnDeleteAction.CASCADE)                                  // manages the foreign constraint of parent entity (customer)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})              // resolve BeanSerialization issue
    User user;

}
