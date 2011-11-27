/*
 * Copyright 2011 caleb.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.caleb.scaffolding.grid.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author caleb
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface GridMetaData {
    
    /**
     * this element must be ignored during grid construction
     * default false 
     */
    public Mode mode() default Mode.READ_WRITE;
    
    public String inputText() default ""; 
    
    public boolean isRequired() default false; 
   
    public String headerKey() default ""; 
    
    
    public enum Mode
    {
        READ_WRITE, READ, IGNORE
    }
    
}
