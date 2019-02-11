package app.demo.examples.memo;

import com.aspectran.core.activity.Translet;
import com.aspectran.core.component.bean.annotation.Autowired;
import com.aspectran.core.component.bean.annotation.Bean;
import com.aspectran.core.component.bean.annotation.Component;

import java.util.List;
import java.util.Map;

@Component
@Bean("memoAction")
public class MemoAction {

    private MemoDao memoDao;

    private MemoBatchDao memoBatchDao;

    @Autowired
    public MemoAction(MemoDao memoDao, MemoBatchDao memoBatchDao) {
        this.memoDao = memoDao;
        this.memoBatchDao = memoBatchDao;
    }

    public List<?> getList() {
        return memoDao.getList();
    }

    public Map<String, ?> addMemo(Translet translet) {
        memoDao.insertMemo();
        return memoDao.getMemo();
    }

    public boolean delMemo() {
        return memoDao.deleteMemo();
    }

    public int delAllMemo() {
        return memoDao.deleteAllMemo();
    }

    public int addBulkMemo(Translet translet) {
        int repetitions = Integer.valueOf(translet.getParameter("repetitions"));
        int affected = 0;
        for (int i = 0; i < repetitions; i++) {
            memoBatchDao.insertBulkMemo();
            affected++;
        }
        return affected;
    }

}
