package app.demo.examples.memo;

import app.demo.common.dao.BatchSqlSession;
import com.aspectran.core.component.bean.annotation.Autowired;
import com.aspectran.core.component.bean.annotation.Bean;
import com.aspectran.core.component.bean.annotation.Component;

import java.util.Map;

@Component
@Bean("memoBatchDao")
public class MemoBatchDao {

    private final BatchSqlSession sqlSession;

    @Autowired
    public MemoBatchDao(BatchSqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public void insertBulkMemo(Map<String, Object> params) {
        sqlSession.insert("memo.insertMemo", params);
    }

}
