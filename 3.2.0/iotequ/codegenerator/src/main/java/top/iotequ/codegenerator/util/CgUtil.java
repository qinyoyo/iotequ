package top.iotequ.codegenerator.util;

import java.util.Arrays;

public class CgUtil {
	static public final String[] mysqlReservedWords = {
			"ADD","ALL","ALTER","ANALYZE","AND","AS","ASC","AUTO_INCREMENT","BDB","BEFORE","BERKELEYDB","BETWEEN","BIGINT","BINARY","BLOB","BOTH","BTREE","BY",
			"CASCADE","CASE","CHANGE","CHAR","CHARACTER","CHECK","COLLATE","COLUMN","COLUMNS","CONSTRAINT","CREATE","CROSS","CURRENT_DATE","CURRENT_TIME",
			"CURRENT_TIMESTAMP","DATABASE","DATABASES","DAY_HOUR","DAY_MINUTE","DAY_SECOND","DEC","DECIMAL","DEFAULT","DELAYED","DELETE","DESC","DESCRIBE",
			"DISTINCT","DISTINCTROW","DIV","DOUBLE","DROP","ELSE","ENCLOSED","ERRORS","ESCAPED","EXISTS","EXPLAIN","FALSE","FIELDS","FLOAT","FOR","FORCE",
			"FOREIGN","FROM","FULLTEXT","FUNCTION","GRANT","GROUP","HASH","HAVING","HIGH_PRIORITY","HOUR_MINUTE","HOUR_SECOND","IF","IGNORE","IN","INDEX",
			"INFILE","INNER","INNODB","INSERT","INT","INTEGER","INTERVAL","INTO","IS","JOIN","KEY","KEYS","KILL","LEADING","LEFT","LIKE","LIMIT","LINES",
			"LOAD","LOCALTIME","LOCALTIMESTAMP","LOCK","LONG","LONGBLOB","LONGTEXT","LOW_PRIORITY","MASTER_SERVER_ID","MATCH","MEDIUMBLOB","MEDIUMINT",
			"MEDIUMTEXT","MIDDLEINT","MINUTE_SECOND","MOD","MRG_MYISAM","NATURAL","NOT","NULL","NUMERIC","ON","OPTIMIZE","OPTION","OPTIONALLY","OR","ORDER",
			"OUTER","OUTFILE","PRECISION","PRIMARY","PRIVILEGES","PROCEDURE","PURGE","READ","REAL","REFERENCES","REGEXP","RENAME","REPLACE","REQUIRE",
			"RESTRICT","RETURNS","REVOKE","RIGHT","RLIKE","RTREE","SELECT","SET","SHOW","SMALLINT","SOME","SONAME","SPATIAL","SQL_BIG_RESULT","SQL_CALC_FOUND_ROWS",
			"SQL_SMALL_RESULT","SSL","STARTING","STRAIGHT_JOIN","STRIPED","TABLE","TABLES","TERMINATED","THEN","TINYBLOB","TINYINT","TINYTEXT","TO","TRAILING",
			"TRUE","TYPES","UNION","UNIQUE","UNLOCK","UNSIGNED","UPDATE","USAGE","USE","USER_RESOURCES","USING","VALUES","VARBINARY","VARCHAR","VARCHARACTER",
			"VARYING","WARNINGS","WHEN","WHERE","WITH","WRITE","XOR","YEAR_MONTH","ZEROFILL"
	};
	static public final String[] oracleReservedWords = {
			"ACCESS","ADD","ALL","ALTER","AND","ANY","AS","ASC","AUDIT","BETWEEN","BY","CHAR","CHECK","CLUSTER","COLUMN","COMMENT","COMPRESS","CONNECT","CREATE",
			"CURRENT","DATE","DECIMAL","DEFAULT","DELETE","DESC","DISTINCT","DROP","ELSE","EXCLUSIVE","EXISTS","FILE","FLOAT","FOR","FROM","GRANT","GROUP","HAVING",
			"IDENTIFIED","IMMEDIATE","IN","INCREMENT","INDEX","INITIAL","INSERT","INTEGER","INTERSECT","INTO","IS","LEVEL","LIKE","LOCK","LONG","MAXEXTENTS","MINUS",
			"MLSLABEL","MODE","MODIFY","NOAUDIT","NOCOMPRESS","NOT","NOWAIT","NULL","NUMBER","OF","OFFLINE","ON","ONLINE","OPTION","OR","ORDER","PCTFREE",
			"PRIOR","PRIVILEGES","PUBLIC","RAW","RENAME","RESOURCE","REVOKE","ROW","ROWID","ROWNUM","ROWS","SELECT","SESSION","SET","SHARE","SIZE","SMALLINT",
			"START","SUCCESSFUL","SYNONYM","SYSDATE","TABLE","THEN","TO","TRIGGER","UID","UNION","UNIQUE","UPDATE","USER","VALIDATE","VALUES","VARCHAR","VARCHAR2",
			"VIEW","WHENEVER","WHERE","WITH"
	};
	static public final String[] sqlserverReservedWords = {
			"ADD","EXCEPT","PERCENT","ALL","EXEC","PLAN","ALTER","EXECUTE","PRECISION","AND","EXISTS","PRIMARY","ANY","EXIT","PRINT","AS","FETCH","PROC",
			"ASC","FILE","PROCEDURE","AUTHORIZATION","FILLFACTOR","PUBLIC","BACKUP","FOR","RAISERROR","BEGIN","FOREIGN","READ","BETWEEN","FREETEXT",
			"READTEXT","BREAK","FREETEXTTABLE","RECONFIGURE","BROWSE","FROM","REFERENCES","BULK","FULL","REPLICATION","BY","FUNCTION","RESTORE",
			"CASCADE","GOTO","RESTRICT","CASE","GRANT","RETURN","CHECK","GROUP","REVOKE","CHECKPOINT","HAVING","RIGHT","CLOSE","HOLDLOCK","ROLLBACK",
			"CLUSTERED","IDENTITY","ROWCOUNT","COALESCE","IDENTITY_INSERT","ROWGUIDCOL","COLLATE","IDENTITYCOL","RULE","COLUMN","IF","SAVE","COMMIT",
			"IN","SCHEMA","COMPUTE","INDEX","SELECT","CONSTRAINT","INNER","SESSION_USER","CONTAINS","INSERT","SET","CONTAINSTABLE","INTERSECT",
			"SETUSER","CONTINUE","INTO","SHUTDOWN","CONVERT","IS","SOME","CREATE","JOIN","STATISTICS","CROSS","KEY","SYSTEM_USER","CURRENT",
			"KILL","TABLE","CURRENT_DATE","LEFT","TEXTSIZE","CURRENT_TIME","LIKE","THEN","CURRENT_TIMESTAMP","LINENO","TO","CURRENT_USER","LOAD",
			"TOP","CURSOR","NATIONAL","TRAN","DATABASE","NOCHECK","TRANSACTION","DBCC","NONCLUSTERED","TRIGGER","DEALLOCATE","NOT","TRUNCATE",
			"DECLARE","NULL","TSEQUAL","DEFAULT","NULLIF","UNION","DELETE","OF","UNIQUE","DENY","OFF","UPDATE","DESC","OFFSETS","UPDATETEXT",
			"DISK","ON","USE","DISTINCT","OPEN","USER","DISTRIBUTED","OPENDATASOURCE","VALUES","DOUBLE","OPENQUERY","VARYING","DROP","OPENROWSET",
			"VIEW","DUMMY","OPENXML","WAITFOR","DUMP","OPTION","WHEN","ELSE","OR","WHERE","END","ORDER","WHILE","ERRLVL","OUTER","WITH","ESCAPE","OVER","WRITETEXT"
	};
	static public final String[] javaReservedWords = {
			"abstract","class","extends","implements","null","strictfp","true",
			"assert","const","false","import","package","super","try",
			"boolean","continue","final","instanceof","private","switch","void",
			"break","default","finally","int","protected","synchronized","volatile",
			"byte","do","float","interface","public","this","while",
			"case","double","for","long","return","throw",
			"catch","else","goto","native","short","throws",
			"char","enum","if","new","static","transient",
			"String","Integer","Boolean","Short","Double","Object","Float","Long","Date"
	};
	public static void checkDbReservedWord(String s) throws CgException{
		if (s==null) return;
		s=s.trim();
		String u=s.toUpperCase();
		String r="";
		if (Arrays.asList(mysqlReservedWords).contains(u)) r="mysql";
		if (Arrays.asList(sqlserverReservedWords).contains(u)) r+=(r.isEmpty() ? "" : ",")+"sqlserver";
		if (Arrays.asList(oracleReservedWords).contains(u)) r+=(r.isEmpty() ? "" : ",")+"oracle";
		if (r.isEmpty()) return;
		else throw new CgException( s+" 是 "+r+" 保留字");
	}
	public static void checkJavaReservedWord(String s) throws CgException {
		if (s==null) return;
		s=s.trim();
		if (Arrays.asList(javaReservedWords).contains(s)) throw new CgException(s+" 是java保留字");
	}
}
