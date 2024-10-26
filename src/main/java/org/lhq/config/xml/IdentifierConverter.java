package org.lhq.config.xml;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.lhq.entity.book.calibre.Creator;
import org.lhq.entity.book.calibre.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdentifierConverter implements Converter {
    private static final Logger log = LoggerFactory.getLogger(IdentifierConverter.class);

    /**
     * Convert an object to textual data.
     *
     * @param source  The object to be marshalled.
     * @param writer  A stream to write to.
     * @param context A context that allows nested objects to be processed by XStream.
     */
    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        if (source instanceof Identifier identifier) {
            log.info("marshal Identifier:{}", identifier);
            writer.addAttribute("opf:scheme", identifier.getScheme());
            writer.addAttribute("id", identifier.getId());
            writer.setValue(identifier.getValue());
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Identifier identifier = new Identifier();
        identifier.setScheme(reader.getAttribute("opf:scheme"));
        identifier.setId(reader.getAttribute("id"));
        identifier.setValue(reader.getValue());
        return identifier;
    }

    /**
     * Determines whether the converter can marshall a particular type.
     *
     * @param type the Class representing the object type to be converted
     */
    @Override
    public boolean canConvert(Class type) {
        return type.equals(Identifier.class);

    }
}
