package app.demo.common.dao;

import com.aspectran.core.component.bean.annotation.AvoidAdvice;
import com.aspectran.core.component.bean.annotation.Bean;
import com.aspectran.core.component.bean.annotation.Component;
import com.aspectran.with.mybatis.SqlSessionTemplate;

@Component
@Bean("simpleSqlSessionTemplate")
@AvoidAdvice
public class SimpleSqlSessionTemplate extends SqlSessionTemplate {

    public SimpleSqlSessionTemplate() {
        super("simpleTxAspect");
    }

}
