package me.wuwenbin.noteblogv4.config.configuration;

import org.hibernate.dialect.InnoDBStorageEngine;
import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.MySQLStorageEngine;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.dialect.function.StaticPrecisionFspTimestampFunction;
import org.hibernate.type.StandardBasicTypes;

import java.sql.Types;

/**
 * 此类包含如下两个已过期的类的内容
 *
 * @author wuwenbin
 * @see org.hibernate.dialect.MySQL5InnoDBDialect
 * @see org.hibernate.dialect.MySQL57InnoDBDialect
 * 使用InnoDb引擎和事物，同时增加一个类型匹配规则（使BigInt返回Long类型）
 */
public class LocalMySQL57InnoDBDialect extends MySQL5Dialect {

    /**
     * 来自
     *
     * @return
     * @see org.hibernate.dialect.MySQL57InnoDBDialect
     */
    @Override
    protected MySQLStorageEngine getDefaultMySQLStorageEngine() {
        return InnoDBStorageEngine.INSTANCE;
    }

    public LocalMySQL57InnoDBDialect() {
        super();
        registerColumnType(Types.TIMESTAMP, "datetime(6)");
        registerColumnType(Types.JAVA_OBJECT, "json");
        final SQLFunction currentTimestampFunction = new StaticPrecisionFspTimestampFunction("now", 6);
        registerFunction("now", currentTimestampFunction);
        registerFunction("current_timestamp", currentTimestampFunction);
        registerFunction("localtime", currentTimestampFunction);
        registerFunction("localtimestamp", currentTimestampFunction);
        registerFunction("sysdate", new StaticPrecisionFspTimestampFunction("sysdate", 6));

        registerHibernateType(Types.BIGINT, StandardBasicTypes.LONG.getName());
    }

    /**
     * @return supports IN clause row value expressions
     * @see <a href="https://dev.mysql.com/worklog/task/?id=7019">MySQL 5.7 work log</a>
     * @see org.hibernate.dialect.MySQL57InnoDBDialect
     */
    @Override
    public boolean supportsRowValueConstructorSyntaxInInList() {
        return true;
    }

}