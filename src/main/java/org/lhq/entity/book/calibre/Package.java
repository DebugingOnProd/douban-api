package org.lhq.entity.book.calibre;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@XStreamAlias("package")
@Setter
@Getter
public class Package {
    @XStreamAsAttribute
    private String xmlns;
    @XStreamAsAttribute
    @XStreamAlias(value = "unique-identifier")
    private String uniqueIdentifier;
    @XStreamAsAttribute
    private String version;

    @XStreamAlias("metadata")
    private Metadata metadata;

    @XStreamAlias("guide")
    private Guide guide;
}
