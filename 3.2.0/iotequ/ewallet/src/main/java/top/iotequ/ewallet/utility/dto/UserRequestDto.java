package top.iotequ.ewallet.utility.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.util.Date;


/**
 * TODO: Add description Here.
 * Created by ao on 2019-07-29
 */

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class UserRequestDto {

    @NonNull
    private String userNo;

    @NonNull
    private String name;

    @NonNull
    private Integer gender;


    private Date birthDate;


    private String mobilePhone;


    private String email;

    private String weChatId;


    private String memberGroup;

    @NonNull
    private Integer costLimit;
    @NonNull
    private Integer dayLimit;
    @NonNull
    private Integer idType;
    @NonNull
    private String idNo;

    private Date activeSince;
    private Date expireAt;


}
