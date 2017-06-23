/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.exampleproject.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author User
 */
public class ClassUtilities {
    private static final Map<String, SortedSet<ComparableMethod>> classGetterMethodsMap = new HashMap<String, SortedSet<ComparableMethod>>();
    private static final String[][] notAllowedMethods = {{"java.lang.Object", "getClass"}};
    
    public static SortedSet<ComparableMethod> getGetterMethods(Object object)
    {
        try
        {
            String className = object.getClass().getName();
            SortedSet<ComparableMethod> getterMethodsSet = classGetterMethodsMap.get(className);
            if(getterMethodsSet == null)
            {
                getterMethodsSet = new TreeSet<ComparableMethod>();
                Class c = Class.forName(className);
                Method method[] = c.getMethods();

                methodLoop:
                for (int i = 0; i < method.length; i++) {
                    String methodName = method[i].getName();
                    boolean isGetMethod = false;
                    boolean isIsMethod = false;
                    if (method[i].getParameterAnnotations().length == 0 && !Modifier.isStatic(method[i].getModifiers())) {
                        if (methodName.startsWith("get") && methodName.length() > 3) {
                            isGetMethod = true;
                        } else if (methodName.startsWith("is") && methodName.length() > 2 && (method[i].getReturnType().equals(boolean.class) || method[i].getReturnType().equals(Boolean.class))) {
                            isIsMethod = true;
                        }
                    }
                    if (isGetMethod || isIsMethod) {
                        for (String[] pair : notAllowedMethods) {
                            if (methodName.equals(pair[1]) && Class.forName(pair[0]).isInstance(object)) {
                                continue methodLoop;
                            }
                        }
                        getterMethodsSet.add(new ComparableMethod(method[i]));
                    }
                }
                classGetterMethodsMap.put(className, getterMethodsSet);
            }
            return getterMethodsSet;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
