package app.demo.examples.memo;

import app.demo.common.CommonDaoSupport;
import com.aspectran.core.component.bean.annotation.Bean;
import com.aspectran.core.component.bean.annotation.Component;

import java.util.List;
import java.util.Map;

@Component
@Bean("memoDao")
public class MemoDao extends CommonDaoSupport {

    public MemoDao() {
        super();
    }

    public Map<String, ?> getMemo(Map<String, Object> params) {
        return sqlSession().selectOne("memo.selectMemo", params);
    }

    public List<?> getList() {
        return sqlSession().selectList("memo.selectMemoList");
    }

    public int insertMemo(Map<String, Object> params) {
        sqlSession().insert("memo.insertMemo", params);
        return Integer.valueOf(params.get("id").toString());
    }

    public boolean deleteMemo(Map<String, Object> params) {
        return (sqlSession().delete("memo.deleteMemo", params) > 0);
    }

}
