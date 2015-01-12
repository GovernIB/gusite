package es.caib.gusite.micromodel.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tcerda on 04/11/2014.
 */
public class TraduccionAdapter extends XmlAdapter<TraduccionList, Map<String, Object>> {

    @Override
    public TraduccionList marshal(Map<String, Object> map) throws Exception {
        TraduccionList adaptedMap = new TraduccionList();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            TraduccionNode node = new TraduccionNode();
            node.setKey(entry.getKey());
            node.setValue(entry.getValue());
            adaptedMap.getEntries().add(node);
        }
        return adaptedMap;
    }

    @Override
    public Map<String, Object> unmarshal(TraduccionList adaptedMap) throws Exception {
        List<TraduccionNode> adaptedEntries = adaptedMap.getEntries();
        Map<String, Object> map = new HashMap<String, Object>(adaptedEntries.size());
        for (TraduccionNode node : adaptedEntries) {
            map.put(node.getKey(), node.getValue());
        }
        return map;
    }

}
