/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.exampleproject.util;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 *
 * @author User
 */
public class ComparableMethod implements Comparable<ComparableMethod> {
    private final Method method;

    public ComparableMethod(Method method) {
        this.method = method;
    }
    
    public String getPropertyName()
    {
        String methodName = method.getName();
        if (methodName.startsWith("get")) {
            methodName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
        } else if (methodName.startsWith("is")) {
            methodName = methodName.substring(2, 3).toLowerCase() + methodName.substring(3);
        }
        return methodName;
    }
    
    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder(method.getDeclaringClass().getCanonicalName());
        stringBuilder.append(".");
        stringBuilder.append(method.getName());
        return stringBuilder.toString();
    }
    
    @Override
    public boolean equals(Object object)
    {
        if(object == null || !(object instanceof ComparableMethod))
        {
            return false;
        }
        ComparableMethod comparableMethod = (ComparableMethod) object;
        return this.compareTo(comparableMethod) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(method.getDeclaringClass().getCanonicalName(), method.getName());
    }
    
    @Override
    public int compareTo(ComparableMethod comparableMethod) {
        if(comparableMethod != null)
        {
            int comparation = method.getDeclaringClass().getCanonicalName().compareTo(comparableMethod.getMethod().getDeclaringClass().getCanonicalName());
            comparation = (comparation == 0) ? method.getName().compareTo(comparableMethod.getMethod().getName()) : comparation;
            return comparation;
        }
        return 1;
    }

    public Method getMethod() {
        return method;
    }
    
}
