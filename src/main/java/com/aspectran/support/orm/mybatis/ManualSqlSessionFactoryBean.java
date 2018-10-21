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
import com.aspectran.core.util.StringUtils;
import com.aspectran.core.util.logging.Log;
import com.aspectran.core.util.logging.LogFactory;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.TypeHandler;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

/**
 * {@code FactoryBean} that creates an MyBatis {@code SqlSessionFactory}.
 * This is the usual way to set up a shared MyBatis {@code SqlSessionFactory};
 * the SqlSessionFactory can then be passed to MyBatis-based DAOs via dependency injection.
 *
 * @author Putthibong Boonbong
 * @author Hunter Presnall
 * @author Eduardo Macarron
 */
public class ManualSqlSessionFactoryBean implements InitializableTransletBean, FactoryBean<SqlSessionFactory> {

    private static final String CONFIG_LOCATION_DELIMITERS = ",;";

    private static final Log log = LogFactory.getLog(ManualSqlSessionFactoryBean.class);

    private DataSource dataSource;

    private TransactionFactory transactionFactory;

    private Properties configurationProperties;

    private SqlSessionFactory sqlSessionFactory;

    private String environment = ManualSqlSessionFactoryBean.class.getSimpleName();

    private Interceptor[] plugins;

    private TypeHandler<?>[] typeHandlers;

    private String typeHandlersPackage;

    private Class<?>[] typeAliases;

    private String typeAliasesPackage;

    private Class<?> typeAliasesSuperType;

    private DatabaseIdProvider databaseIdProvider;

    private ObjectFactory objectFactory;

    private ObjectWrapperFactory objectWrapperFactory;

    private String[] mapperLocations;

    /**
     * Sets the ObjectFactory.
     *
     * @param objectFactory
     */
    public void setObjectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    /**
     * Sets the ObjectWrapperFactory.
     *
     * @param objectWrapperFactory
     */
    public void setObjectWrapperFactory(ObjectWrapperFactory objectWrapperFactory) {
        this.objectWrapperFactory = objectWrapperFactory;
    }

    /**
     * Gets the DatabaseIdProvider
     *
     * @return
     */
    public DatabaseIdProvider getDatabaseIdProvider() {
        return databaseIdProvider;
    }

    /**
     * Sets the DatabaseIdProvider.
     *
     * @param databaseIdProvider
     */
    public void setDatabaseIdProvider(DatabaseIdProvider databaseIdProvider) {
        this.databaseIdProvider = databaseIdProvider;
    }

    /**
     * Mybatis plugin list.
     *
     * @param plugins list of plugins
     */
    public void setPlugins(Interceptor[] plugins) {
        this.plugins = plugins;
    }

    /**
     * Packages to search for type aliases.
     *
     * @param typeAliasesPackage package to scan for domain objects
     */
    public void setTypeAliasesPackage(String typeAliasesPackage) {
        this.typeAliasesPackage = typeAliasesPackage;
    }

    /**
     * Super class which domain objects have to extend to have a type alias
     * created. No effect if there is no package to scan configured.
     *
     * @param typeAliasesSuperType super class for domain objects
     */
    public void setTypeAliasesSuperType(Class<?> typeAliasesSuperType) {
        this.typeAliasesSuperType = typeAliasesSuperType;
    }

    /**
     * Packages to search for type handlers.
     *
     * @param typeHandlersPackage package to scan for type handlers
     */
    public void setTypeHandlersPackage(String typeHandlersPackage) {
        this.typeHandlersPackage = typeHandlersPackage;
    }

    /**
     * Set type handlers. They must be annotated with {@code MappedTypes} and
     * optionally with {@code MappedJdbcTypes}
     *
     * @param typeHandlers Type handler list
     */
    public void setTypeHandlers(TypeHandler<?>[] typeHandlers) {
        this.typeHandlers = typeHandlers;
    }

    /**
     * List of type aliases to register. They can be annotated with
     * {@code Alias}
     *
     * @param typeAliases Type aliases list
     */
    public void setTypeAliases(Class<?>[] typeAliases) {
        this.typeAliases = typeAliases;
    }

    /**
     * Set optional properties to be passed into the SqlSession configuration,
     * as alternative to a {@code &lt;properties&gt;} tag in the configuration
     * xml file. This will be used to resolve placeholders in the config file.
     */
    public void setConfigurationProperties(
            Properties sqlSessionFactoryProperties) {
        this.configurationProperties = sqlSessionFactoryProperties;
    }

    /**
     * Set the JDBC {@code DataSource} that this instance should manage
     * transactions for. The {@code DataSource} should match the one used by the
     * {@code SqlSessionFactory}: for example, you could specify the same JNDI
     * DataSource for both.
     *
     * A transactional JDBC {@code Connection} for this {@code DataSource} will
     * be provided to application code accessing this {@code DataSource}
     * directly via {@code DataSourceUtils} or
     * {@code DataSourceTransactionManager}.
     *
     * The {@code DataSource} specified here should be the target
     * {@code DataSource} to manage transactions for, not a
     * {@code TransactionAwareDataSourceProxy}. Only data access code may work
     * with {@code TransactionAwareDataSourceProxy}, while the transaction
     * manager needs to work on the underlying target {@code DataSource}. If
     * there's nevertheless a {@code TransactionAwareDataSourceProxy} passed in,
     * it will be unwrapped to extract its target {@code DataSource}.
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Set the MyBatis TransactionFactory to use.
     *
     * @param transactionFactory the MyBatis TransactionFactory
     */
    public void setTransactionFactory(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    /**
     * Set locations of MyBatis mapper files that are going to be merged into
     * the {@code SqlSessionFactory} configuration at runtime.
     */
    public void setMapperLocations(String[] mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    /**
     * Build a {@code SqlSessionFactory} instance.
     *
     * @return SqlSessionFactory
     */
    protected SqlSessionFactory buildSqlSessionFactory(InputStream[] mapperInputStreams) {
        Configuration configuration = new Configuration();
        configuration.setVariables(this.configurationProperties);

        if(this.objectFactory != null) {
            configuration.setObjectFactory(this.objectFactory);
        }

        if(this.objectWrapperFactory != null) {
            configuration.setObjectWrapperFactory(this.objectWrapperFactory);
        }

        if(StringUtils.hasLength(this.typeAliasesPackage)) {
            String[] typeAliasPackageArray = StringUtils.tokenize(this.typeAliasesPackage, CONFIG_LOCATION_DELIMITERS);
            for(String packageToScan : typeAliasPackageArray) {
                configuration.getTypeAliasRegistry().registerAliases(
                        packageToScan,
                        typeAliasesSuperType == null ? Object.class : typeAliasesSuperType);
                if(log.isDebugEnabled()) {
                    log.debug("Scanned package: '" + packageToScan + "' for aliases");
                }
            }
        }

        if(this.typeAliases != null && this.typeAliases.length > 0) {
            for(Class<?> typeAlias : this.typeAliases) {
                configuration.getTypeAliasRegistry().registerAlias(typeAlias);
                if(log.isDebugEnabled()) {
                    log.debug("Registered type alias: '" + typeAlias + "'");
                }
            }
        }

        if(this.plugins != null && this.plugins.length > 0) {
            for(Interceptor plugin : this.plugins) {
                configuration.addInterceptor(plugin);
                if(log.isDebugEnabled()) {
                    log.debug("Registered plugin: '" + plugin + "'");
                }
            }
        }

        if(StringUtils.hasLength(this.typeHandlersPackage)) {
            String[] typeHandlersPackageArray = StringUtils.tokenize(this.typeHandlersPackage, CONFIG_LOCATION_DELIMITERS);
            for(String packageToScan : typeHandlersPackageArray) {
                configuration.getTypeHandlerRegistry().register(packageToScan);
                if(log.isDebugEnabled()) {
                    log.debug("Scanned package: '" + packageToScan + "' for type handlers");
                }
            }
        }

        if(this.typeHandlers != null && this.typeHandlers.length > 0) {
            for(TypeHandler<?> typeHandler : this.typeHandlers) {
                configuration.getTypeHandlerRegistry().register(typeHandler);
                if(log.isDebugEnabled()) {
                    log.debug("Registered type handler: '" + typeHandler + "'");
                }
            }
        }

        if(this.transactionFactory == null) {
            this.transactionFactory = new JdbcTransactionFactory();
        }

        configuration.setEnvironment(new Environment(this.environment, this.transactionFactory, this.dataSource));

        if(this.databaseIdProvider != null) {
            try {
                configuration.setDatabaseId(this.databaseIdProvider.getDatabaseId(this.dataSource));
            } catch(SQLException e) {
                throw new IllegalArgumentException("Failed getting a databaseId", e);
            }
        }

        if(mapperInputStreams != null && mapperInputStreams.length > 0) {
            for(int i = 0; i < mapperInputStreams.length; i++) {
                if(mapperInputStreams[i] == null) {
                    continue;
                }

                try {
                    XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(
                            mapperInputStreams[i], configuration, mapperLocations[i], configuration.getSqlFragments());
                    xmlMapperBuilder.parse();
                } catch(Exception e) {
                    throw new IllegalArgumentException("Failed to parse mapping resource: '" + mapperLocations[i] + "'", e);
                } finally {
                    ErrorContext.instance().reset();
                }

                if(log.isDebugEnabled()) {
                    log.debug("Parsed mapper file: '" + mapperLocations[i] + "'");
                }
            }
        } else {
            if(log.isDebugEnabled()) {
                log.debug("Property 'mapperLocations' was not specified or no matching resources found");
            }
        }

        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        return sqlSessionFactoryBuilder.build(configuration);
    }

    @Override
    public void initialize(Translet translet) throws Exception {
        if(sqlSessionFactory == null) {
            if(dataSource == null) {
                throw new IllegalArgumentException("Property 'dataSource' is required");
            }

            InputStream[] mapperInputStreams = null;
            if(mapperLocations != null && mapperLocations.length > 0) {
                mapperInputStreams = new InputStream[mapperLocations.length];
                for(int i = 0; i < mapperLocations.length; i++) {
                    if(mapperLocations[i] != null) {
                        File file = translet.getEnvironment().toRealPathAsFile(mapperLocations[i]);
                        mapperInputStreams[i] = new FileInputStream(file);
                    }
                }
            }

            sqlSessionFactory = buildSqlSessionFactory(mapperInputStreams);
        }
    }

    @Override
    public SqlSessionFactory getObject() {
        return this.sqlSessionFactory;
    }

}