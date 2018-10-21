/*
 * Copyright (c) 2008-2018 The Aspectran Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aspectran.support.orm.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.sql.Connection;

/**
 * Advice for SqlSession Transactions.
 *
 * <blockquote cite="https://mybatis.github.io/mybatis-3/java-api.html">
 * By default MyBatis does not actually commit unless it detects
 * that the database has been changed by a call to insert, update or delete.
 * If you've somehow made changes without calling these methods,
 * then you can pass true into the commit and rollback methods to guarantee
 * that it will be committed (note, you still can't force a session in auto-commit mode,
 * or one that is using an external transaction manager).
 * </blockquote>
 *
 * @author Juho Jeong
 * @since 2015. 04. 03.
*/
public class SqlSessionTxAdvice {

    private final SqlSessionFactory sqlSessionFactory;

    private boolean autoCommit;

    private SqlSession sqlSession;

    public SqlSessionTxAdvice(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public SqlSession getSqlSession() {
        return sqlSession;
    }

    public SqlSession open() {
        if(sqlSession == null) {
            sqlSession = sqlSessionFactory.openSession(autoCommit);
        }
        return sqlSession;
    }

    public SqlSession open(boolean autoCommit) {
        this.autoCommit = autoCommit;
        return open();
    }

    /**
     * Flushes batch statements and commits database connection.
     * Note that database connection will not be committed if no updates/deletes/inserts were called.
     * To force the commit call {@link #commit(boolean)}
     */
    public void commit() {
        checkSession();
        sqlSession.commit();
    }

    /**
     * Flushes batch statements and commits database connection.
     *
     * @param force forces connection commit
     */
    public void commit(boolean force) {
        checkSession();
        sqlSession.commit(force);
    }

    /**
     * Discards pending batch statements and rolls database connection back.
     * Note that database connection will not be rolled back if no updates/deletes/inserts were called.
     * To force the rollback call {@link #rollback(boolean)}
     */
    public void rollback() {
        checkSession();
        sqlSession.rollback();
    }

    /**
     * Discards pending batch statements and rolls database connection back.
     * Note that database connection will not be rolled back if no updates/deletes/inserts were called.
     *
     * @param force forces connection rollback
     */
    public void rollback(boolean force) {
        checkSession();
        sqlSession.rollback(force);
    }

    /**
     * Closes the session.
     */
    public void close() {
        checkSession();
        sqlSession.close();
        sqlSession = null;
    }

    /**
     * Retrieves a mapper.
     *
     * @param <T> the mapper type
     * @param type Mapper interface class
     * @return a mapper bound to this SqlSession
     */
    public <T> T getMapper(Class<T> type) {
        checkSession();
        return sqlSession.getMapper(type);
    }

    /**
     * Retrieves inner database connection
     *
     * @return the connection
     */
    public Connection getConnection() {
        checkSession();
        return sqlSession.getConnection();
    }

    private void checkSession() {
        if(sqlSession == null) {
            throw new IllegalStateException("The SqlSession is not open");
        }
    }

}
