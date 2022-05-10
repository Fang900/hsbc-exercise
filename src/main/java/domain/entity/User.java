package domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class User {
    private Long id;
    private List<Role> role;
    private String token;
    private long tokenExpireTime;
    private String userName;
    private String password;
    private Date createdDate;

    public void updateRoles(List<Role> role){
        if(this.role == null){
            this.role = new ArrayList<>(role);
        }
        else this.role.addAll(role);
    }
}
