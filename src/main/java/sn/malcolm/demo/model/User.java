package sn.malcolm.demo.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sn.malcolm.demo.model.enums.UserRole;
import sn.malcolm.demo.view.UserView;

import java.io.Serial;
import java.util.Date;

@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "user")
@EqualsAndHashCode(callSuper = false)
@Setter
public class User extends BaseEntity implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonView({UserView.UserRead.class,  UserView.UserReadDetail.class})
    @Column(name = "kc_id", unique = true)
    private String kcId;

    @JsonView({UserView.UserRead.class, UserView.UserWrite.class, UserView.UserReadDetail.class})
    private String email;

    @JsonView({UserView.UserRead.class, UserView.UserWrite.class, UserView.UserReadDetail.class})
    private String firstName;

    @JsonView({UserView.UserRead.class, UserView.UserWrite.class, UserView.UserReadDetail.class})
    private String lastName;

    @JsonView({UserView.UserRead.class, UserView.UserWrite.class, UserView.UserReadDetail.class})
    @Column(name = "username", nullable = false)
    private String username;


    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @JsonView({})
    @Column(name = "activate", nullable = false)
    private boolean activate = true;

    @JsonView({})
    @Column(name = "account_locked", nullable = false)
    private Boolean accountLocked = false;

    @JsonView({})
    @Column(name = "account_locked_date")
    private Date accountLockedDate;

    @JsonView({})
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.ADMIN;

    @JsonView({})
    public boolean getIsBoUser() {
        return UserRole.ADMIN.equals(this.role);
    }

    @JsonView({})
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}
