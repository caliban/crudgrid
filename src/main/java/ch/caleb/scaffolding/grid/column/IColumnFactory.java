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
package ch.caleb.scaffolding.grid.column;

import ch.caleb.scaffolding.grid.annotations.GridMetaData;
import com.inmethod.grid.IGridColumn;
import com.inmethod.grid.column.PropertyColumn;
import com.inmethod.grid.column.editable.EditablePropertyColumn;
import extensions.edu.umich.med.irs.column.DropDownPropertyColumn;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

/**
 *
 * @author caleb
 */
public class IColumnFactory {
    
    public List<IGridColumn> getColumns(Object o)  throws Exception
    {
        Map<String, Object> map = BeanUtils.describe(o);
        return null; 
    }
    public List<IGridColumn> getColumns(Object o, List<String> expressions)
    {
        Map<String, Object> map = new HashMap<String, Object>(); 
        for(String expression : expressions)
        {
            try {
                Object temp = BeanUtils.getProperty(o, expression);
                if(temp != null)
                {
                    map.put(expression, temp);
                }
            } catch (Exception ex) {
                //derp, unbekanntes property. 
                Logger.getLogger(IColumnFactory.class.getName()).log(Level.SEVERE, null, ex);
           
            }
        }
        return null; 
            
    }
    public Map<String, Object> filterForIgnore(Object source, Map<String, Object> input) throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>(); 
        for(String value : input.keySet())
        {
            
            Method method = input.getClass().getMethod(value);
            GridMetaData gmd = method.getAnnotation(GridMetaData.class);
            if(gmd == null || gmd.mode() != GridMetaData.Mode.IGNORE)
            {
                resultMap.put(value, input.get(value));
                        
            }
            else
            {
                // is marked ignore
            }
        }
        return resultMap; 
    }
    
    public IGridColumn makeForReadOnly(GridMetaData data, String key, Object value) throws Exception
    {
       //readonly is always propertycolumn
        return new PropertyColumn(getHeaderModel(data, key), key);
    }
    
    public IGridColumn makeForReadWrite(GridMetaData data, String key, Object value) throws Exception
    {
        Object o = BeanUtils.getProperty(value, key);
        if(o instanceof Enum)
        {
            //ok we have an enum. render it as a dropdown. 
            return makeForRwEnum(data, key, (Enum) o);
        }
        else
        {
            return new EditablePropertyColumn(getHeaderModel(data, key), key);
        }
    }
    private IModel<String> getHeaderModel(GridMetaData data, String key)
            {
                 if(StringUtils.isNotEmpty(data.headerKey()))
        {
           return new ResourceModel(key);
        }
        else
        {
            return new Model<String>(StringUtils.capitalize(key));
        }
            }
    private IGridColumn makeForRwEnum(GridMetaData data, String key, Enum  value)
    {
        //todo abfangen. liste ausgeben. 
        return new DropDownPropertyColumn(getHeaderModel(data, key), key, null, null, null);
    }
}
