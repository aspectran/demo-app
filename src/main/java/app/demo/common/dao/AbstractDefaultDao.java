package app.demo.common.dao;

import com.aspectran.support.orm.mybatis.MyBatisDaoSupport;

public abstract class AbstractDefaultDao extends MyBatisDaoSupport {

    public AbstractDefaultDao() {
        super("defaultTxAspect");
    }

}
