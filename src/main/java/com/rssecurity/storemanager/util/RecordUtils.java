package com.rssecurity.storemanager.util;

import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rssecurity.storemanager.exception.RecordConversionException;

public class RecordUtils {
    public static Map<String, Object> recordToMap(Object record) {
        if (record == null || !record.getClass().isRecord()) {
            throw new RecordConversionException("Não é um Record");
        }
    
        Map<String, Object> map = new HashMap<>();
    
        try {
            RecordComponent[] components = record.getClass().getRecordComponents();
    
            for (RecordComponent component : components) {
                Method accessor = component.getAccessor();
                Object value = accessor.invoke(record);
    
                if (value == null) {
                    map.put(component.getName(), null);
                    continue;
                }
    
                if (value instanceof List<?> list) {
                    List<Object> convertedList = list.stream()
                        .map(item -> item != null && item.getClass().isRecord()
                            ? recordToMap(item)
                            : item)
                        .toList();
    
                    map.put(component.getName(), convertedList);
                } else if (value.getClass().isRecord()) {
                    map.put(component.getName(), recordToMap(value));
                } else {
                    map.put(component.getName(), value);
                }
            }
    
        } catch (Exception e) {
            throw new RecordConversionException("Erro ao converter Record em Map: " + record.getClass());
        }
    
        return map;
    }
}
