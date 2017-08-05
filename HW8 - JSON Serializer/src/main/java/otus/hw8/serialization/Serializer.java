package otus.hw8.serialization;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import otus.hw8.ReflectionHelper;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

public class Serializer {
    public String exec(Object o) {
        return ((JSONAware) getObjectValue(o)).toJSONString();
    }

    private JSONObject getMemberObjectValue(Object o) {
        JSONObject jsonObject = new JSONObject();
        for (Field field : o.getClass().getDeclaredFields())
            if(!field.isSynthetic())
                jsonObject.put(field.getName(), getObjectValue(ReflectionHelper.getFieldValue(o, field.getName())));

        return jsonObject;
    }

    private Object getObjectValue(Object o) {
        Object value;
        if (o.getClass().isMemberClass()) value = getMemberObjectValue(o);
        else if (o.getClass().isArray()) value = getArrayValue(o);
        else if(Collection.class.isAssignableFrom(o.getClass())) value = getCollectionValue((Collection) o);
        else if(Map.class.isAssignableFrom(o.getClass())) throw new SerializationNotSupported();
        else value = o;

        return value;
    }

    private JSONArray getCollectionValue(Collection value) {
        JSONArray jsonArray = new JSONArray();
        for (Object o : value) jsonArray.add(getObjectValue(o));

        return jsonArray;
    }

    private JSONArray getArrayValue(Object array) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < Array.getLength(array); i++) jsonArray.add(getObjectValue(Array.get(array, i)));

        return jsonArray;
    }
}
