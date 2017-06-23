/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.exampleproject.util;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author User
 */
public class PrintObject implements Comparable<PrintObject> {
    private String objectPath;
    private final Object object;
    private final List<PrintObject[]> fieldValuesList;

    public PrintObject(String objectPath, Object object, List<PrintObject[]> fieldValuesList) {
        this.objectPath = objectPath;
        this.object = object;
        this.fieldValuesList = fieldValuesList;
    }
    
    public String getParentPath()
    {
        if(objectPath != null)
        {
            int lastDotIndex = objectPath.lastIndexOf(".");
            if(lastDotIndex != -1)
            {
                return objectPath.substring(0, lastDotIndex);
            }
        }
        return "";
    }
    
    public String getFieldName()
    {
        if(objectPath != null)
        {
            int lastDotIndex = objectPath.lastIndexOf(".");
            return objectPath.substring(lastDotIndex + 1);
        }
        return null;
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        if(objectPath != null)
        {
            stringBuilder.append(objectPath);
            stringBuilder.append(" - ");
            stringBuilder.append(object);
            stringBuilder.append("\n");
            if(fieldValuesList != null)
            {
                for(PrintObject[] fieldValues : fieldValuesList)
                {
                    for(PrintObject fieldValue : fieldValues)
                    {
                        stringBuilder.append(fieldValue.toString());
                    }
                }
            }
        }
        return stringBuilder.toString();
    }
    
    public String toTableString(Map<String, String> classTabMap)
    {
        StringBuilder stringBuilder = new StringBuilder("        <table border=\"1\" style=\"width:100%\">\n");
        stringBuilder.append("          <tr>\n");
        stringBuilder.append("            <th>Object path</th>\n");
        stringBuilder.append("            <th>Class/Value</th>\n");
        stringBuilder.append("          </tr>\n");
        stringBuilder.append(getTableStringData(classTabMap));
        stringBuilder.append("        </table>\n");
        return stringBuilder.toString();
    }
    
    private String getTableStringData(Map<String, String> classTabMap)
    {
        StringBuilder stringBuilder = new StringBuilder();
        if(objectPath != null)
        {
            stringBuilder.append("          <tr>\n");
            stringBuilder.append("            <td>");
            stringBuilder.append(objectPath.replaceAll("\n", "<br>"));
            stringBuilder.append("</td>\n");
            stringBuilder.append("            <td>");
            String tab = (object != null) ? classTabMap.get(object.getClass().getCanonicalName()) : null;
            if(tab != null)
            {
                stringBuilder.append("<a href=\"#");
                stringBuilder.append(tab);
                stringBuilder.append("\" class=\"");
                stringBuilder.append(tab);
                stringBuilder.append("\">");
                stringBuilder.append(object.getClass().getCanonicalName());
                stringBuilder.append("</a>");
            }
            else
            {
                stringBuilder.append(object);
            }
            stringBuilder.append("</td>\n");
            stringBuilder.append("          </tr>\n");
            if(fieldValuesList != null)
            {
                for(PrintObject[] fieldValues : fieldValuesList)
                {
                    for(PrintObject fieldValue : fieldValues)
                    {
                        if(fieldValue != null)
                        {
                            stringBuilder.append(fieldValue.getTableStringData(classTabMap));
                        }
                    }
                }
            }
        }
        return stringBuilder.toString();
    }
    
    @Override
    public boolean equals(Object object)
    {
        if(object == null || !(object instanceof PrintObject))
        {
            return false;
        }
        PrintObject printObject = (PrintObject) object;
        return this.compareTo(printObject) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.objectPath);
    }
    
    @Override
    public int compareTo(PrintObject table) {
        if(table != null)
        {
            return this.objectPath.compareTo(table.getObjectPath());
        }
        return 1;
    }

    public String getObjectPath() {
        return objectPath;
    }

    public void setObjectPath(String objectPath) {
        this.objectPath = objectPath;
    }

    public Object getObject() {
        return object;
    }

    public List<PrintObject[]> getFieldValuesList() {
        return fieldValuesList;
    }
    
}
