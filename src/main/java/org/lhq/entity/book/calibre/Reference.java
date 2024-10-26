package org.lhq.entity.book.calibre;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Getter;
import lombok.Setter;

@XStreamAlias("reference")
@Getter
@Setter
public class Reference {
    @XStreamAsAttribute
    private String type;
    @XStreamAsAttribute
    private String title;
    @XStreamAsAttribute
    private String href;
}
