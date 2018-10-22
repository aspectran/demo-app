package app.demo.examples.mybatis;

import com.aspectran.core.component.bean.annotation.Bean;
import com.aspectran.core.component.bean.annotation.Component;

@Component
@Bean("sampleDao")
public class SampleDao extends CommonDaoSupport {

    public SampleDao() {
        super();
    }

    public String getOne() {
        return getSqlSession().selectOne("sample.getOne");
    }

}
