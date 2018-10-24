package app.demo.examples.memo;

import app.demo.common.dao.SimpleSqlSessionTemplate;
import com.aspectran.core.component.bean.annotation.Autowired;
import com.aspectran.core.component.bean.annotation.Bean;
import com.aspectran.core.component.bean.annotation.Component;

import java.util.List;
import java.util.Map;

@Component
@Bean("memoDao")
public class MemoDao {

    @Autowired
    public SimpleSqlSessionTemplate sqlSessionTemplate;

    public MemoDao() {
        super();
    }

    public Map<String, ?> getMemo(Map<String, Object> params) {
        return sqlSessionTemplate.selectOne("memo.selectMemo", params);
    }

    public List<?> getList() {
        return sqlSessionTemplate.selectList("memo.selectMemoList");
    }

    public int insertMemo(Map<String, Object> params) {
        sqlSessionTemplate.insert("memo.insertMemo", params);
        return Integer.valueOf(params.get("id").toString());
    }

    public boolean deleteMemo(Map<String, Object> params) {
        return (sqlSessionTemplate.delete("memo.deleteMemo", params) > 0);
    }

    public int deleteAllMemo() {
        return sqlSessionTemplate.delete("memo.deleteAllMemo");
    }

}
