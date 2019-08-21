package com.example.demo.cfg;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.type.StandardBasicTypes;

import java.sql.Types;

/**
 * Extend H2Dialect to force BigInts to be mapped back to Longs.
 * According to the docs this shouldn't be needed, so this class may become redundant in later versions
 */
public class CustomH2Dialect extends H2Dialect {

    public CustomH2Dialect() {
        super();
        registerHibernateType(Types.BIGINT, StandardBasicTypes.LONG.getName());
    }

}
