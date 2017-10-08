package hw16.message_system;

import com.google.gson.Gson;
import lombok.Data;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Data
public abstract class Message {
    public static final String CLASS_NAME_VARIABLE = "className";

    private final String className;

    private final Address from;
    private final Address to;

    public Message(Address from, Address to) {
        this.className = this.getClass().getName();
        this.from = from;
        this.to = to;
    }

    public abstract void exec(Addressee addressee);

    public static Message getMsgFromJSON(String json) throws ParseException, ClassNotFoundException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
        String className = (String) jsonObject.get(Message.CLASS_NAME_VARIABLE);
        Class<?> msgClass = Class.forName(className);
        return (Message) new Gson().fromJson(json, msgClass);
    }
}