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
package app.demo;

import com.aspectran.core.util.ResourceUtils;
import com.aspectran.shell.jline.JLineAspectranShell;

import java.io.File;
import java.io.IOException;

/**
 * Test for Demo App.
 */
public class DemoAppTest {

    public static void main(String[] args) {
        try {
            File current = ResourceUtils.getResourceAsFile("");
            File root = new File(current, "../../app");
            JLineAspectranShell.main(new String[] { root.getCanonicalPath(), "config/aspectran-config.apon" });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}