package app.demo.common.dao;

import com.aspectran.support.orm.mybatis.MyBatisDaoSupport;

public abstract class AbstractReuseDao extends MyBatisDaoSupport {

    public AbstractReuseDao() {
        super("reuseTxAspect");
    }

}
