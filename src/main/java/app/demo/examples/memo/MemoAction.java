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
    public MemoDao sampleDao;

    public List<?> getList() {
        return sampleDao.getList();
    }

    public Map<String, ?> addMemo(Translet translet) {
        int id = sampleDao.insertMemo(translet.getAllParameters());
        return sampleDao.getMemo(new HashMap<String, Object>() {{
            put("id", id);
        }});
    }

    public boolean delMemo(Translet translet) {
        return sampleDao.deleteMemo(translet.getAllParameters());
    }

}
