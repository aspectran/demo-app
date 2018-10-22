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

import com.aspectran.core.component.bean.aware.ActivityContextAware;
import com.aspectran.core.context.ActivityContext;
import org.apache.ibatis.session.SqlSession;

public class MyBatisDaoSupport implements ActivityContextAware {

    private final String relevantAspectId;

    private ActivityContext context;

    public MyBatisDaoSupport(String relevantAspectId) {
        if (relevantAspectId == null) {
            throw new IllegalArgumentException("relevantAspectId can not be null");
        }
        this.relevantAspectId = relevantAspectId;
    }

    protected String getRelevantAspectId() {
        return relevantAspectId;
    }

    protected SqlSession getSqlSession() {
        return getSqlSession(relevantAspectId);
    }

    protected SqlSession getSqlSession(String relevantAspectId) {
        SqlSessionTxAdvice advice = getAspectAdviceBean(relevantAspectId);
        SqlSession sqlSession = advice.getSqlSession();
        if (sqlSession == null) {
            throw new IllegalArgumentException("SqlSession is not opened");
        }
        return sqlSession;
    }

    private SqlSessionTxAdvice getAspectAdviceBean(String relevantAspectId) {
        if (context == null) {
            throw new IllegalArgumentException("ActivityContext is not injected");
        }
        SqlSessionTxAdvice advice = context.getCurrentActivity().getTranslet().getAspectAdviceBean(relevantAspectId);
        if (advice == null) {
            if (context.getAspectRuleRegistry().getAspectRule(relevantAspectId) == null) {
                throw new IllegalArgumentException("Aspect '" + relevantAspectId + "' handling SqlSessionTxAdvice is undefined");
            } else {
                throw new IllegalArgumentException("SqlSessionTxAdvice is not defined");
            }
        }
        return advice;
    }

    @Override
    public void setActivityContext(ActivityContext context) {
        this.context = context;
    }

}
