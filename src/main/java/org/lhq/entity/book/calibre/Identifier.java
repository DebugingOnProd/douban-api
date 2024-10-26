package org.lhq.entity.book.calibre;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Getter;
import lombok.Setter;

@XStreamAlias("dc:identifier")
@Setter
@Getter
public class Identifier {
    @XStreamAsAttribute
    @XStreamAlias(value = "opf:scheme")
    private String scheme;
    @XStreamAsAttribute
    @XStreamAlias(value = "id")
    private String id;
    private String value;
}
