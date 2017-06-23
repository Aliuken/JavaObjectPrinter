/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.exampleproject.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author User
 */
public class Utilities {
    private static final String[] valueClasses = {
        "java.lang.Number", "java.lang.String", "java.lang.Character", "java.lang.Boolean", "java.lang.Enum",
        "java.util.Date", "java.lang.Class", "java.util.TimeZone", "java.util.Locale", "java.lang.StackTraceElement"};
    
    public static PrintObject getPrintObjectTree(String path, Object object, int remainingDepth) {
        try {
            if(remainingDepth <= 0)
            {
                return null;
            }

            if (object == null) {
                return new PrintObject(path, null, null);
            }

            for (String valueClass : valueClasses) {
                if (Class.forName(valueClass).isInstance(object)) {
                    return new PrintObject(path, object, null);
                }
            }

            if (object.getClass().isArray() || object instanceof Iterable || object instanceof Map || object instanceof Dictionary || object instanceof Enumeration) {
                List<PrintObject[]> childsList = new ArrayList<PrintObject[]>();
                if (object.getClass().isArray()) {
                    Object[] objectArray = convertToObjectArray(object);
                    for (int i = 0; i < objectArray.length; i++) {
                        childsList = addKeyValue(path, i, objectArray[i], remainingDepth, childsList);
                    }
                } else if (object instanceof Iterable) {
                    int i = 0;
                    for (Object iterationObject : (Iterable<Object>) object) {
                        childsList = addKeyValue(path, i, iterationObject, remainingDepth, childsList);
                        i++;
                    }
                } else if (object instanceof Map) {
                    int i = 0;
                    for (Map.Entry<Object, Object> entry : ((Map<Object, Object>) object).entrySet()) {
                        childsList = addKeyValue(path, entry.getKey(), entry.getValue(), remainingDepth, childsList);
                        i++;
                    }
                } else if (object instanceof Dictionary) {
                    Dictionary<Object, Object> dic = (Dictionary<Object, Object>) object;
                    int i = 0;
                    for (Enumeration<Object> e = dic.keys(); e.hasMoreElements();) {
                        Object keyDic = e.nextElement();
                        childsList = addKeyValue(path, keyDic, dic.get(keyDic), remainingDepth, childsList);
                        i++;
                    }
                } else if (object instanceof Enumeration) {
                    int i = 0;
                    for (Enumeration<Object> e = (Enumeration<Object>) object; e.hasMoreElements();) {
                        childsList = addKeyValue(path, i, e.nextElement(), remainingDepth, childsList);
                        i++;
                    }
                }
                return new PrintObject(path, object, childsList);
            } else {
                List<PrintObject[]> childs = new ArrayList<PrintObject[]>();
                
                SortedSet<ComparableMethod> getterMethods = ClassUtilities.getGetterMethods(object);
                if(getterMethods != null && !getterMethods.isEmpty())
                {
                    PrintObject[] childValues = new PrintObject[getterMethods.size()];
                    int i = 0;
                    for(ComparableMethod getterMethod : getterMethods)
                    {
                        Object methodResult = null;
                        try {
                            methodResult = getterMethod.getMethod().invoke(object);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        StringBuilder sb = new StringBuilder(path);
                        sb.append(".");
                        sb.append(getterMethod.getPropertyName());

                        childValues[i] = getPrintObjectTree(sb.toString(), methodResult, remainingDepth - 1);
                        i++;
                    }
                    childs.add(childValues);
                }
                return new PrintObject(path, object, childs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static List<PrintObject[]> addKeyValue(String path, Object key, Object value, int remainingDepth, List<PrintObject[]> valuesList)
    {
        StringBuilder sb = new StringBuilder(path);
        sb.append("[").append(key).append("]");
        PrintObject[] childs = new PrintObject[2];
        childs[0] = getPrintObjectTree(sb.toString() + ".key", key, remainingDepth - 1);
        childs[1] = getPrintObjectTree(sb.toString() + ".value", value, remainingDepth - 1);
        valuesList.add(childs);
        return valuesList;
    }
    
    private static Object[] convertToObjectArray(Object object)
    {
        Object[] objectArray = null;
        if (object instanceof Object[]) {
            objectArray = (Object[]) object;
        } else if (object instanceof byte[]) {
            byte[] primitiveArray = (byte[]) object;
            objectArray = new Object[primitiveArray.length];
            for (int i = 0; i < objectArray.length; i++) {
                objectArray[i] = primitiveArray[i];
            }
        } else if (object instanceof short[]) {
            short[] primitiveArray = (short[]) object;
            objectArray = new Object[primitiveArray.length];
            for (int i = 0; i < objectArray.length; i++) {
                objectArray[i] = primitiveArray[i];
            }
        } else if (object instanceof int[]) {
            int[] primitiveArray = (int[]) object;
            objectArray = new Object[primitiveArray.length];
            for (int i = 0; i < objectArray.length; i++) {
                objectArray[i] = primitiveArray[i];
            }
        } else if (object instanceof long[]) {
            long[] primitiveArray = (long[]) object;
            objectArray = new Object[primitiveArray.length];
            for (int i = 0; i < objectArray.length; i++) {
                objectArray[i] = primitiveArray[i];
            }
        } else if (object instanceof float[]) {
            float[] primitiveArray = (float[]) object;
            objectArray = new Object[primitiveArray.length];
            for (int i = 0; i < objectArray.length; i++) {
                objectArray[i] = primitiveArray[i];
            }
        } else if (object instanceof double[]) {
            double[] primitiveArray = (double[]) object;
            objectArray = new Object[primitiveArray.length];
            for (int i = 0; i < objectArray.length; i++) {
                objectArray[i] = primitiveArray[i];
            }
        } else if (object instanceof char[]) {
            char[] primitiveArray = (char[]) object;
            objectArray = new Object[primitiveArray.length];
            for (int i = 0; i < objectArray.length; i++) {
                objectArray[i] = primitiveArray[i];
            }
        } else if (object instanceof boolean[]) {
            boolean[] primitiveArray = (boolean[]) object;
            objectArray = new Object[primitiveArray.length];
            for (int i = 0; i < objectArray.length; i++) {
                objectArray[i] = primitiveArray[i];
            }
        }
        return objectArray;
    }
    
    public static void printObject(Object initialObject, int initialDepth, String filePath) throws IOException {
        StringBuilder sb = new StringBuilder("<!doctype html>\n");
        sb.append("<html>\n");
        sb.append("  <head>\n");
        sb.append("    <meta charset=\"utf-8\">\n");
        sb.append("    <title>Print Object result</title>\n");
        sb.append("    <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js\"></script>\n");
        sb.append("    <link rel=\"stylesheet\" href=\"https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/themes/smoothness/jquery-ui.css\">\n");
        sb.append("    <script src=\"https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js\"></script>\n");
        sb.append("    <link rel=\"stylesheet\" href=\"/resources/demos/style.css\">\n");
        sb.append("  </head>\n");
        sb.append("  <body>\n");

        PrintObject root = getPrintObjectTree("root", initialObject, initialDepth);
        SortedMap<String, SortedSet<PrintObject>> classValuesMap = getClassValuesMap(root, null);
        if(classValuesMap != null)
        {
            sb.append("    <div id=\"tabs\">\n");
            sb.append("      <ul>\n");
            sb.append("        <li><a href=\"#tabs-1\">Index</a></li>\n");
            Map<String, String> classTabMap = new TreeMap<String, String>();
            int i = 2;
            for(String className : classValuesMap.keySet())
            {
                String href = "tabs-" + i;
                sb.append("        <li><a href=\"#").append(href).append("\">").append(className).append("</a></li>\n");
                classTabMap.put(className, href);
                i++;
            }
            sb.append("      </ul>\n");
            sb.append("      <div id=\"tabs-1\">\n");
            sb.append(root.toTableString(classTabMap));
            sb.append("      </div>\n");
            i = 2;
            for(String className : classValuesMap.keySet())
            {
                String id = "tabs-" + i;
                sb.append("      <div id=\"").append(id).append("\">\n");
                SortedSet<PrintObject> printObjects = classValuesMap.get(className);
                if(printObjects != null && !printObjects.isEmpty())
                {
                    sb.append("        <table border=\"1\" style=\"width:100%\">\n");
                    sb.append("          <tr>\n");
                    sb.append("            <th>Object path</th>\n");
                    for(PrintObject field : printObjects.first().getFieldValuesList().get(0))
                    { 
                        sb.append("            <th>");
                        sb.append(field.getFieldName());
                        sb.append("</th>\n");
                    }
                    sb.append("          </tr>\n");
                    for(PrintObject printObject : printObjects)
                    {
                        for(PrintObject[] fields : printObject.getFieldValuesList())
                        {
                            sb.append("          <tr>\n");
                            sb.append("            <td>");
                            sb.append(printObject.getObjectPath().replaceAll("\n", "<br>"));
                            sb.append("</td>\n");
                            for(PrintObject field : fields)
                            {
                                sb.append("            <td>");
                                if(field.getObject() != null)
                                {
                                    String tab = (field.getObject() != null) ? classTabMap.get(field.getObject().getClass().getCanonicalName()) : null;
                                    if(tab != null)
                                    {
                                        sb.append("<a href=\"#");
                                        sb.append(tab);
                                        sb.append("\" class=\"");
                                        sb.append(tab);
                                        sb.append("\">");
                                        sb.append(field.getObject().getClass().getCanonicalName());
                                        sb.append("</a>");
                                    }
                                    else
                                    {
                                        sb.append(field.getObject().toString());
                                    }
                                }
                                else
                                {
                                    sb.append("null");
                                }
                                sb.append("</td>\n");
                            }
                            sb.append("          </tr>\n");
                        }
                    }
                    sb.append("        </table>\n");
                }
                sb.append("      </div>\n");
                i++;
            }
            sb.append("    </div>\n");
            sb.append("    <script>\n");
            sb.append("      $(document).ready(function () {\n");
            sb.append("        $('#tabs').tabs();\n");
            i = 1;
            for(String classname : classTabMap.keySet())
            {
                sb.append("        $('.");
                sb.append(classTabMap.get(classname));
                sb.append("').click(function() {\n");
		sb.append("          $('#tabs').tabs({active: ");
                sb.append(i);
                sb.append("});\n");
                sb.append("        });\n");
                i++;
            }
            sb.append("      });\n");
            sb.append("    </script>\n");
        }
        sb.append("  </body>\n");
        sb.append("</html>\n");

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(sb.toString());
        }
    }
    
    private static SortedMap<String, SortedSet<PrintObject>> getClassValuesMap(PrintObject printObject, SortedMap<String, SortedSet<PrintObject>> classValuesMap)
    {
        if(classValuesMap == null)
        {
            classValuesMap = new TreeMap<String, SortedSet<PrintObject>>();
        }
        
        if(printObject != null && printObject.getObject() != null && printObject.getFieldValuesList() != null && !printObject.getFieldValuesList().isEmpty())
        {
            PrintObject[] firstChild = printObject.getFieldValuesList().get(0);
            boolean maxDepthReached = (firstChild[0] == null);
            if(!maxDepthReached)
            {
                String className = printObject.getObject().getClass().getCanonicalName();
                SortedSet<PrintObject> valuesSet = classValuesMap.get(className);
                if(valuesSet == null)
                {
                    valuesSet = new TreeSet<PrintObject>();
                }
                PrintObject sameObject = null;
                for(PrintObject currentPrintObject : valuesSet)
                {
                    if(printObject.getObject().equals(currentPrintObject.getObject()))
                    {
                        sameObject = currentPrintObject;
                        break;
                    }
                }
                if(sameObject != null)
                {
                   sameObject.setObjectPath(sameObject.getObjectPath() + "\n" + printObject.getObjectPath());
                }
                else
                {
                    valuesSet.add(new PrintObject(printObject.getObjectPath(), printObject.getObject(), printObject.getFieldValuesList()));
                }
                classValuesMap.put(className, valuesSet);
                for(PrintObject[] fieldValues : printObject.getFieldValuesList())
                {
                    for(PrintObject fieldValue : fieldValues)
                    {
                        classValuesMap = getClassValuesMap(fieldValue, classValuesMap);
                    }
                }
            }
        }
        return classValuesMap;
    }
}
