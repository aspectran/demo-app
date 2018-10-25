package app.demo.examples.memo;

import app.demo.common.dao.SimpleSqlSession;
import com.aspectran.core.component.bean.annotation.Autowired;
import com.aspectran.core.component.bean.annotation.Bean;
import com.aspectran.core.component.bean.annotation.Component;

import java.util.List;
import java.util.Map;

@Component
@Bean("memoDao")
public class MemoDao {

    @Autowired
    public SimpleSqlSession sqlSession;

    public MemoDao() {
        super();
    }

    public Map<String, ?> getMemo(Map<String, Object> params) {
        return sqlSession.selectOne("memo.selectMemo", params);
    }

    public List<?> getList() {
        return sqlSession.selectList("memo.selectMemoList");
    }

    public int insertMemo(Map<String, Object> params) {
        sqlSession.insert("memo.insertMemo", params);
        return Integer.valueOf(params.get("id").toString());
    }

    public boolean deleteMemo(Map<String, Object> params) {
        return (sqlSession.delete("memo.deleteMemo", params) > 0);
    }

    public int deleteAllMemo() {
        return sqlSession.delete("memo.deleteAllMemo");
    }

}
