package app.demo.common.dao;

import com.aspectran.core.component.bean.annotation.AvoidAdvice;
import com.aspectran.core.component.bean.annotation.Bean;
import com.aspectran.core.component.bean.annotation.Component;
import com.aspectran.support.orm.mybatis.MyBatisDaoSupport;

@Component
@Bean("defaultSimpleDao")
@AvoidAdvice
public class DefaultSimpleDao extends MyBatisDaoSupport {

    public DefaultSimpleDao() {
        super("defaultSimpleTxAspect");
    }

}
