package app.demo.examples.memo;

import app.demo.common.dao.DefaultBatchDao;
import com.aspectran.core.component.bean.annotation.Autowired;
import com.aspectran.core.component.bean.annotation.Bean;
import com.aspectran.core.component.bean.annotation.Component;

import java.util.Map;

@Component
@Bean("memoBatchDao")
public class MemoBatchDao {

    @Autowired
    public DefaultBatchDao dao;

    public MemoBatchDao() {
        super();
    }

    public void insertBulkMemo(Map<String, Object> params) {
        dao.insert("memo.insertMemo", params);
    }

}
