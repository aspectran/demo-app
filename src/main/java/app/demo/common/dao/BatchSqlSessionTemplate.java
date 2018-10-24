package app.demo.common.dao;

import com.aspectran.core.component.bean.annotation.AvoidAdvice;
import com.aspectran.core.component.bean.annotation.Bean;
import com.aspectran.core.component.bean.annotation.Component;
import com.aspectran.with.mybatis.SqlSessionTemplate;

@Component
@Bean("batchSqlSessionTemplate")
@AvoidAdvice
public class BatchSqlSessionTemplate extends SqlSessionTemplate {

    public BatchSqlSessionTemplate() {
        super("batchTxAspect");
    }

}
