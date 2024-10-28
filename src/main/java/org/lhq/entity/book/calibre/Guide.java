package org.lhq.entity.book.calibre;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class Guide {
    @XStreamImplicit(itemFieldName = "reference")
    private List<Reference> references;
}
