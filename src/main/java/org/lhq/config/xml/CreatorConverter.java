package org.lhq.config.xml;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.lhq.entity.book.calibre.Creator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreatorConverter implements Converter {
    private static final Logger log = LoggerFactory.getLogger(CreatorConverter.class);

    /**
     * Convert an object to textual data.
     *
     * @param source  The object to be marshalled.
     * @param writer  A stream to write to.
     * @param context A context that allows nested objects to be processed by XStream.
     */
    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        if (source instanceof Creator creator) {
            log.info("marshal creator:{}", creator);
            writer.addAttribute("opf:file-as", creator.getFileAs());
            writer.addAttribute("opf:role", creator.getRole());
            writer.setValue(creator.getValue());
        }

    }

    /**
     * Convert textual data back into an object.
     *
     * @param reader  The stream to read the text from.
     * @param context
     * @return The resulting object.
     */
    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Creator creator = new Creator();
        creator.setFileAs(reader.getAttribute("opf:file-as"));
        creator.setRole(reader.getAttribute("opf:role"));
        creator.setValue(reader.getValue());
        return creator;
    }

    /**
     * Determines whether the converter can marshall a particular type.
     *
     * @param type the Class representing the object type to be converted
     */
    @Override
    public boolean canConvert(Class type) {
        return type.equals(Creator.class);
    }
}
