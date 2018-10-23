package app.demo.examples.memo;

import app.demo.common.dao.AbstractBatchDao;
import com.aspectran.core.component.bean.annotation.Bean;
import com.aspectran.core.component.bean.annotation.Component;

import java.util.Map;

@Component
@Bean("memoBatchDao")
public class MemoBatchDao extends AbstractBatchDao {

    public MemoBatchDao() {
        super();
    }

    public void insertBulkMemo(Map<String, Object> params) {
        getSqlSession().insert("memo.insertMemo", params);
    }

}
