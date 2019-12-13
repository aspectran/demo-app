/*
 * Copyright (c) 2008-2019 The Aspectran Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package app.demo.examples.hello;

import com.aspectran.core.component.bean.annotation.Bean;
import com.aspectran.core.component.bean.annotation.Component;
import com.aspectran.core.component.bean.annotation.Description;
import com.aspectran.core.util.logging.Log;
import com.aspectran.core.util.logging.LogFactory;

@Component
@Bean("helloAction")
@Description("Defines a Hello Action Bean that contains the helloWorld() method.")
public class HelloAction {

    private static final Log log = LogFactory.getLog(HelloAction.class);

    public String helloWorld() {
        String msg = "Hello, World!";

        log.info("The message generated by my first action is: " + msg);

        return msg;
    }

}
