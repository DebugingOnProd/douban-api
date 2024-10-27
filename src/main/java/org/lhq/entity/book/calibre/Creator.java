package org.lhq.entity.book.calibre;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Getter;
import lombok.Setter;

@XStreamAlias("dc:creator")
@Getter
@Setter
public class Creator {
    @XStreamAsAttribute
    @XStreamAlias("opf:file-as")
    private String fileAs;
    @XStreamAsAttribute
    @XStreamAlias("opf:role")
    private String role;
    private String value;
}
