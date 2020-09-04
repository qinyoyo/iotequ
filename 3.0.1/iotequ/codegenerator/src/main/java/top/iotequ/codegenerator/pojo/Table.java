package top.iotequ.codegenerator.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class Table {
    private String tableCatalog;

    private String tableSchema;

    private String tableName;

    private String tableType;

    private String engine;

    private Long version;

    private String rowFormat;

    private Long tableRows;

    private Long avgRowLength;

    private Long dataLength;

    private Long maxDataLength;

    private Long indexLength;

    private Long dataFree;

    private Long autoIncrement;

    private Date createTime;

    private Date updateTime;

    private Date checkTime;

    private String tableCollation;

    private Long checksum;

    private String createOptions;

    private String tableComment;

}