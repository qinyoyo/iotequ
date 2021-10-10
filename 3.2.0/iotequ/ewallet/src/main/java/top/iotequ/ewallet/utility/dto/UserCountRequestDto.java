package top.iotequ.ewallet.utility.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

/**
 * TODO: Add description Here.
 * Created by ao on 2019-07-29
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class UserCountRequestDto {

    @NonNull
    private Integer countId;

    @NonNull
    private Integer amountCount;

    @NonNull
    private Integer costLimit;

    @NonNull
    private Integer dayLimit;

    @NonNull
    private String userNo;
}
