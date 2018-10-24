package app.demo.examples.memo;

import app.demo.common.dao.BatchSqlSessionTemplate;
import com.aspectran.core.component.bean.annotation.Autowired;
import com.aspectran.core.component.bean.annotation.Bean;
import com.aspectran.core.component.bean.annotation.Component;

import java.util.Map;

@Component
@Bean("memoBatchDao")
public class MemoBatchDao {

    @Autowired
    public BatchSqlSessionTemplate sqlSessionTemplate;

    public MemoBatchDao() {
        super();
    }

    public void insertBulkMemo(Map<String, Object> params) {
        sqlSessionTemplate.insert("memo.insertMemo", params);
    }

}
