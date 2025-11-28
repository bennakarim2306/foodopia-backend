package com.foodopia.backend.data.user;

import com.foodopia.backend.data.item.Address;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "user-details")
@Table(schema = "user_details")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String firstName;

    private String lastName;

    private Integer age;

    private Date birthDay;

    private String email;

    private String password;

    private boolean consentAllowed;

    private String ContactsList;

    private String ContactsRequestList;

    @Transient
    @Getter
    @Setter
    private List<String> ContactsIdsList;

    @Transient
    @Getter
    @Setter
    private List<String> ContactsRequestsIdsList;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "address_street")),
            @AttributeOverride(name = "city", column = @Column(name = "address_city")),
            @AttributeOverride(name = "zip", column = @Column(name = "address_zip")),
            @AttributeOverride(name = "lat", column = @Column(name = "address_lat")),
            @AttributeOverride(name = "lng", column = @Column(name = "address_lng"))
    })
    private Address address;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
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

    @PostLoad
    private void parseContactsListIds () {
        if(this.ContactsList != null) {
            this.ContactsIdsList = Arrays.stream(this.getContactsList().split("\\|")).toList();
        }
        if(this.ContactsRequestList != null) {
            this.ContactsRequestsIdsList = Arrays.stream(this.getContactsRequestList().split("\\|")).toList();
        }
    }
}
