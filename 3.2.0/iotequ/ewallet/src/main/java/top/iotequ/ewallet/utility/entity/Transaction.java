package top.iotequ.ewallet.utility.entity;

import org.springframework.lang.NonNull;

/**
 * Represents a transaction, either a cost or charge.
 * Created by ao on 2019-07-24
 */
public class Transaction {

    public Transaction(TransactionType type, int value, int itemId) {
        this.type = type;
        this.value = value;
        this.itemId = itemId;
    }

    public Transaction(int value) {
        this.type = TransactionType.MONEY_COST;
        this.value = value;
    }

    @NonNull
    private TransactionType type;

    @NonNull
    private int value;

    /**
     * Depending on the CostType:
     * MONEY_COST: null
     * COUNT_COST: number of count units
     * TIME_COST: number of time units
     */

    private int itemId;

    public TransactionType getType() {
        return type;
    }


    public int getValue() {
        return value;
    }


    public int getItemId() {
        return itemId;
    }


}
