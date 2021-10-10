package top.iotequ.ewallet.utility.dto;

import lombok.*;

import java.util.Date;

/**
 * TODO: Add description Here.
 * Created by ao on 2019-07-25
 */
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class TransactionRequestDto {

    @NonNull
    private String userNo;

    private int costType;

    private int amount;

    private int projectId;

    private int operationType;

    @NonNull
    private String deviceNo;

    @NonNull
    private String shopId;

    @NonNull
    private String deviceStream;

    @NonNull
    private Date deviceDate;

    @NonNull
    private String tradeNo;

    @NonNull
    private String operatorNo;

    @NonNull
    private String batchNo;

    @NonNull
    private Integer loginId;
}
