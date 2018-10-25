package app.demo.common.dao;

import com.aspectran.core.component.bean.annotation.AvoidAdvice;
import com.aspectran.core.component.bean.annotation.Bean;
import com.aspectran.core.component.bean.annotation.Component;
import com.aspectran.with.mybatis.SqlSessionTemplate;

@Component
@Bean("reuseSqlSession")
@AvoidAdvice
public class ReuseSqlSession extends SqlSessionTemplate {

    public ReuseSqlSession() {
        super("reuseTxAspect");
    }

}
