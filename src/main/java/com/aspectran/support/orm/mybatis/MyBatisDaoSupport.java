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

import com.aspectran.core.activity.Translet;
import org.apache.ibatis.session.SqlSession;

public abstract class MyBatisDaoSupport {

    private String relevantAspectId;

    public String getRelevantAspectId() {
        return relevantAspectId;
    }

    public void setRelevantAspectId(String relevantAspectId) {
        this.relevantAspectId = relevantAspectId;
    }

    public SqlSession getSqlSession(Translet translet) {
        return getSqlSession(translet, relevantAspectId);
    }

    public SqlSession getSqlSession(Translet translet, String relevantAspectId) {
        if (relevantAspectId == null) {
            throw new IllegalArgumentException("No Aspect associated with " + this);
        }

        SqlSessionTxAdvice advice = translet.getAspectAdviceBean(relevantAspectId);
        if (advice == null) {
            throw new IllegalArgumentException("SqlSessionTxAdvice is not defined");
        }

        SqlSession sqlSession = advice.getSqlSession();
        if (sqlSession == null) {
            throw new IllegalArgumentException("SqlSession is not opened");
        }

        return sqlSession;
    }

}
