package org.lhq.service.gen.impl;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import org.lhq.config.xml.CreatorConverter;
import org.lhq.config.xml.IdentifierConverter;
import org.lhq.entity.book.BookInfo;
import org.lhq.entity.book.calibre.Package;
import org.lhq.service.gen.Gen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;

public class GenBookInfoXml implements Gen<BookInfo> {
    private static final Logger log = LoggerFactory.getLogger(GenBookInfoXml.class);

    @Override
    public void genFile(BookInfo bookInfo, File taskFile) {
        try {
            Package aPackage = bookInfo.toPackage();
            XStream xStream = new XStream(new StaxDriver());
            xStream.registerConverter(new CreatorConverter());
            xStream.registerConverter(new IdentifierConverter());
            xStream.alias("package", Package.class);
            xStream.processAnnotations(Package.class);
            xStream.autodetectAnnotations(true);
            String xml = xStream.toXML(aPackage);
            xml = prettyPrintByTransformer(xml);
            log.info("gen xml:{}", xml);
            Files.write(taskFile.toPath(), xml.getBytes());
            log.info("gen xml success path:{}", taskFile.getAbsolutePath());
        } catch (IOException e) {
            log.info("gen xml error", e);
        }
    }
    private String prettyPrintByTransformer(String xmlString){
        try {
            InputSource src = new InputSource(new StringReader(xmlString));
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 4  );
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no"); // 包含XML声明

            Writer out = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(out));
            return out.toString();
        } catch (Exception e) {
            log.error("Error occurs when pretty-printing xml", e);
            return xmlString;
        }
    }


}
