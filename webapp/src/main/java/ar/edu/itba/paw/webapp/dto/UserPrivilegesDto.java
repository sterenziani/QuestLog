package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.entity.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserPrivilegesDto {
    private long id;

    @Size(min = 6, max = 100)
    @Pattern(regexp = "[a-zA-Z0-9]+")
    @NotNull
    private String username;

    private boolean isAdmin;

    public static UserPrivilegesDto fromUser(User user){
        final UserPrivilegesDto dto = new UserPrivilegesDto();
        dto.id       = user.getId();
        dto.username = user.getUsername();
        dto.isAdmin  = user.getAdminStatus();
        return dto;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
