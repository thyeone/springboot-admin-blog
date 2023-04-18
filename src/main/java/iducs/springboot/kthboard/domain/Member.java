package iducs.springboot.kthboard.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Member {
    private Long seq;
    private String id;
    private String pw;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String deny;
    private String level;
}
