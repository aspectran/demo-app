package app.demo.examples.memo;

import app.demo.common.dao.AbstractDefaultDao;
import com.aspectran.core.component.bean.annotation.Bean;
import com.aspectran.core.component.bean.annotation.Component;

import java.util.List;
import java.util.Map;

@Component
@Bean("memoDao")
public class MemoDao extends AbstractDefaultDao {

    public MemoDao() {
        super();
    }

    public Map<String, ?> getMemo(Map<String, Object> params) {
        return getSqlSession().selectOne("memo.selectMemo", params);
    }

    public List<?> getList() {
        return getSqlSession().selectList("memo.selectMemoList");
    }

    public int insertMemo(Map<String, Object> params) {
        getSqlSession().insert("memo.insertMemo", params);
        return Integer.valueOf(params.get("id").toString());
    }

    public boolean deleteMemo(Map<String, Object> params) {
        return (getSqlSession().delete("memo.deleteMemo", params) > 0);
    }

    public int deleteAllMemo() {
        return getSqlSession().delete("memo.deleteAllMemo");
    }

}
