package top.iotequ.ewallet.utility.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * TODO: Add description Here.
 * Created by ao on 2019-07-29
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class UserTimeRequestDto {

    private Integer timeId;

    private Integer amountTime;

    private Integer costLimit;

    private Integer dayLimit;

    private String userNo;
}
