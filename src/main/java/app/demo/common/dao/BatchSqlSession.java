package app.demo.common.dao;

import com.aspectran.core.component.bean.annotation.AvoidAdvice;
import com.aspectran.core.component.bean.annotation.Bean;
import com.aspectran.core.component.bean.annotation.Component;
import com.aspectran.with.mybatis.SqlSessionTemplate;

@Component
@Bean("batchSqlSession")
@AvoidAdvice
public class BatchSqlSession extends SqlSessionTemplate {

    public BatchSqlSession() {
        super("batchTxAspect");
    }

}
