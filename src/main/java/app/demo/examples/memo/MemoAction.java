package app.demo.examples.memo;

import com.aspectran.core.activity.Translet;
import com.aspectran.core.component.bean.annotation.Autowired;
import com.aspectran.core.component.bean.annotation.Bean;
import com.aspectran.core.component.bean.annotation.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Bean("memoAction")
public class MemoAction {

    @Autowired
    public MemoDao memoDao;

    @Autowired
    public MemoBatchDao memoBatchDao;

    public List<?> getList() {
        return memoDao.getList();
    }

    public Map<String, ?> addMemo(Translet translet) {
        int id = memoDao.insertMemo(translet.getAllParameters());
        return memoDao.getMemo(new HashMap<String, Object>() {{
            put("id", id);
        }});
    }

    public boolean delMemo(Translet translet) {
        return memoDao.deleteMemo(translet.getAllParameters());
    }

    public int delAllMemo() {
        return memoDao.deleteAllMemo();
    }

    public int addBulkMemo(Translet translet) {
        int repetitions = Integer.valueOf(translet.getParameter("repetitions"));
        int affected = 0;
        for (int i = 0; i < repetitions; i++) {
            memoBatchDao.insertBulkMemo(translet.getAllParameters());
            affected++;
        }
        return affected;
    }

}
