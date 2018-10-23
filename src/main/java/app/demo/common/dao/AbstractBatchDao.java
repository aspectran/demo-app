package app.demo.common.dao;

import com.aspectran.support.orm.mybatis.MyBatisDaoSupport;

public abstract class AbstractBatchDao extends MyBatisDaoSupport {

    public AbstractBatchDao() {
        super("batchTxAspect");
    }

}
