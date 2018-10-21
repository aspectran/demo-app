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
import com.aspectran.core.component.bean.ablility.FactoryBean;
import com.aspectran.core.component.bean.ablility.InitializableTransletBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;

/**
 * {@code FactoryBean} that creates an MyBatis {@code SqlSessionFactory} using default MyBatis Configuration.
 */
public class SimpleSqlSessionFactoryBean implements InitializableTransletBean, FactoryBean<SqlSessionFactory> {

    private String configLocation;

    private String environment;

    private SqlSessionFactory sqlSessionFactory;

    /**
     * Set the location of the MyBatis {@code SqlSessionFactory} config file.
     */
    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }

    /**
     * <b>NOTE:</b> This class <em>overrides</em> any {@code Environment} you
     * have set in the MyBatis config file. This is used only as a placeholder
     * name. The default value is
     * {@code SqlSessionFactoryBean.class.getSimpleName()}.
     *
     * @param environment the environment name
     */
    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    /**
     * Build a {@code SqlSessionFactory} instance.
     *
     * The default implementation uses the standard MyBatis
     * {@code XMLConfigBuilder} API to build a {@code SqlSessionFactory}
     * instance based on an Reader.
     *
     * @return SqlSessionFactory
     */
    protected SqlSessionFactory buildSqlSessionFactory(File configFile) {
        try (Reader reader = new FileReader(configFile)) {
            SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
            return sqlSessionFactoryBuilder.build(reader, environment);
        } catch(Exception ex) {
            throw new IllegalArgumentException("Failed to parse mybatis config resource: " + configLocation, ex);
        }
    }

    @Override
    public void initialize(Translet translet) throws Exception {
        if(sqlSessionFactory == null) {
            if(configLocation == null) {
                throw new IllegalArgumentException("Property 'configLocation' is required");
            }

            File configFile = translet.getEnvironment().toRealPathAsFile(configLocation);
            sqlSessionFactory = buildSqlSessionFactory(configFile);
        }
    }

    @Override
    public SqlSessionFactory getObject() {
        return sqlSessionFactory;
    }

}