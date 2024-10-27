package org.lhq.entity.book.calibre;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Getter;
import lombok.Setter;

@XStreamAlias("dc:contributor")
@Setter
@Getter
public class Contributor {
    @XStreamAsAttribute
    private String fileAs;
    @XStreamAsAttribute
    private String role;
    private String value;
}
