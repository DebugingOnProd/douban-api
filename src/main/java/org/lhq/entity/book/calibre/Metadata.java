package org.lhq.entity.book.calibre;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@XStreamAlias("metadata")
public class Metadata {
    @XStreamAsAttribute
    @XStreamAlias(value = "xmlns:opf")
    private String xmlnsOpf;
    @XStreamImplicit(itemFieldName = "dc:identifier")
    private List<Identifier> identifiers;
    @XStreamAlias("dc:title")
    private String title;
    @XStreamAlias("dc:creator")
    private Creator creator;
    @XStreamAlias("dc:contributor")
    private Contributor contributor;
    @XStreamAlias("dc:date")
    private String date;
    @XStreamAlias("dc:description")
    private String description;
    @XStreamAlias("dc:publisher")
    private String publisher;
    @XStreamAlias("dc:language")
    private String language;
    @XStreamImplicit(itemFieldName = "dc:subject")
    private List<String> subjects;
    @XStreamImplicit(itemFieldName = "meta")
    private List<Meta> metas;
}
