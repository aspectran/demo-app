package app.demo.common.dao;

import com.aspectran.core.component.bean.annotation.AvoidAdvice;
import com.aspectran.core.component.bean.annotation.Bean;
import com.aspectran.core.component.bean.annotation.Component;
import com.aspectran.support.orm.mybatis.MyBatisDaoSupport;

@Component
@Bean("defaultBatchDao")
@AvoidAdvice
public class DefaultBatchDao extends MyBatisDaoSupport {

    public DefaultBatchDao() {
        super("defaultBatchTxAspect");
    }

}
