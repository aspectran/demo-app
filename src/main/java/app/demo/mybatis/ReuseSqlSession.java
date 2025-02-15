package app.demo.mybatis;

import com.aspectran.core.component.bean.annotation.Bean;
import com.aspectran.core.component.bean.annotation.Component;
import com.aspectran.mybatis.SqlSessionAgent;

@Component
@Bean(id = "reuseSqlSession", lazyDestroy = true)
public class ReuseSqlSession extends SqlSessionAgent {

    public ReuseSqlSession() {
        super("reuseTxAspect");
        setAutoParameters(true);
    }

}
