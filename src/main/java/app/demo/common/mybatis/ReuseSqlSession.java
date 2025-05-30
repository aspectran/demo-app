package app.demo.common.mybatis;

import com.aspectran.core.component.bean.annotation.Bean;
import com.aspectran.core.component.bean.annotation.Component;
import com.aspectran.mybatis.SqlSessionAgent;
import org.apache.ibatis.session.ExecutorType;

@Component
@Bean(id = "reuseSqlSession", lazyDestroy = true)
public class ReuseSqlSession extends SqlSessionAgent {

    public ReuseSqlSession() {
        super("reuseTxAspect");
        setExecutorType(ExecutorType.REUSE);
        setAutoParameters(true);
    }

}
