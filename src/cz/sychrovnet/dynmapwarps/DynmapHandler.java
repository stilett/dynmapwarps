package cz.sychrovnet.dynmapwarps;

import net.TheDgtl.Warpz0r.Locations;
import netscape.javascript.JSObject;
import org.dynmap.DynmapPlugin;
import java.io.BufferedOutputStream;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Location;
import org.dynmap.ClientUpdateEvent;
import org.dynmap.DynmapWorld;

import org.dynmap.web.HttpField;
import org.dynmap.web.HttpHandler;
import org.dynmap.web.HttpRequest;
import org.dynmap.web.HttpResponse;
import org.dynmap.web.HttpStatus;
import org.dynmap.web.Json;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import static org.dynmap.JSONUtils.*;


/**
 * User: stilett
 * Date: 6/10/11
 * Time: 1:12 PM
 */
public class DynmapHandler implements HttpHandler {
    private DynmapWarps dynmapWarps;
    private DynmapPlugin dynmapPlugin;

    public DynmapHandler(DynmapWarps dynmapWarps, DynmapPlugin dynmapPlugin) {
        this.dynmapWarps = dynmapWarps;
        this.dynmapPlugin = dynmapPlugin;
    }

    public void handle(String path, HttpRequest request, HttpResponse response) throws Exception {
        JSONObject jsonInfo = new JSONObject();
        jsonInfo.put("marks", formatMarks());

        byte[] bytes = jsonInfo.toJSONString().getBytes("UTF-8");

        String dateStr = new Date().toString();
        response.fields.put(HttpField.Date, dateStr);
        response.fields.put(HttpField.ContentType, "text/plain; charset=utf-8");
        response.fields.put(HttpField.Expires, "Thu, 01 Dec 1994 16:00:00 GMT");
        response.fields.put(HttpField.LastModified, dateStr);
        response.fields.put(HttpField.ContentLength, Integer.toString(bytes.length));
        response.status = HttpStatus.OK;

        BufferedOutputStream out = null;
        out = new BufferedOutputStream(response.getBody());
        out.write(bytes);
        out.flush();
    }

    
    public JSONArray formatMarks() {
        JSONArray jsonMarks = new JSONArray();
        List<Mark> marks = dynmapWarps.getMarks();

        for (Mark mark: marks) {
            JSONObject jsonMark = new JSONObject();
            jsonMark.put("world", mark.world);
            jsonMark.put("group", mark.group);
            jsonMark.put("name", mark.name);
            jsonMark.put("icon", mark.icon);
            jsonMark.put("x", mark.location.getX());
            jsonMark.put("y", mark.location.getY());
            jsonMark.put("z", mark.location.getZ());
            jsonMarks.add(jsonMark);
        }

        return jsonMarks;
    }
}
