package top.iotequ.ewallet.utility.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * TODO: Add description Here.
 * Created by ao on 2019-07-25
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class TransactionResponseDto {

    private String billNo;

    private int bonus;
}
