package org.lhq.entity.book.calibre;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Getter;
import lombok.Setter;

@XStreamAlias("meta")
@Getter
@Setter
public class Meta {
    @XStreamAsAttribute
    private String name;
    @XStreamAsAttribute
    private String content;
}
