package app.demo.examples.mybatis;

import com.aspectran.core.component.bean.annotation.Autowired;
import com.aspectran.core.component.bean.annotation.Bean;
import com.aspectran.core.component.bean.annotation.Component;

@Component
@Bean("sampleAction")
public class SampleAction {

    @Autowired
    public SampleDao sampleDao;

    public String getOne() {
        return sampleDao.getOne();
    }

}
