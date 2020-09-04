package top.iotequ.ewallet.utility.dto;

import java.util.List;
import lombok.Data;

/**
 * Data Transfer Object for balance-queries.
 * Created by ao on 2019-07-25
 */
@Data
public class BalanceResponseDto {

    private String userNo;

    private int amount;

    private int bonus;

    private List<TimeResponseDto> times;

    private List<CountResponseDto> counts;
}
