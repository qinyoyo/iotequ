package top.iotequ.ewallet.utility.entity;

/**
 * Enum for three types of the transaction operation.
 * MONEY_COST: Cost/Charge by consuming/adding actual balance (money).
 * COUNT_COST: Cost/Charge by consuming/adding counts.
 * TIME_COST: Cost/Charge by consuming/adding time units.
 * Created by ao on 2019-07-24
 */
public enum TransactionType {
    MONEY_COST, COUNT_COST, TIME_COST
}
