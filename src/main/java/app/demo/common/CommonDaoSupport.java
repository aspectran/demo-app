package app.demo.common;

import com.aspectran.support.orm.mybatis.MyBatisDaoSupport;

public abstract class CommonDaoSupport extends MyBatisDaoSupport {

    public CommonDaoSupport() {
        super("sqlSessionTxAspect");
    }

}
