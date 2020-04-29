package gov.nasa.pds.harvest.cfg.rd;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import gov.nasa.pds.harvest.cfg.model.XPathMap;
import gov.nasa.pds.harvest.cfg.model.XPathMapsCfg;
import gov.nasa.pds.harvest.util.xml.XPathUtils;
import gov.nasa.pds.harvest.util.xml.XmlDomUtils;

public class Rxpath
{
    public static XPathMapsCfg parseXPathMaps(Document doc) throws Exception
    {
        XPathUtils xpu = new XPathUtils();
        
        int count = xpu.getNodeCount(doc, "/harvest/xpathMaps");
        if(count == 0) return null;
        if(count > 1) throw new Exception("Could not have more than one '/harvest/xpathMaps' element.");
        
        XPathMapsCfg maps = new XPathMapsCfg();
        Node rootNode = xpu.getFirstNode(doc, "/harvest/xpathMaps");
        maps.baseDir = XmlDomUtils.getAttribute(rootNode, "baseDir");

        NodeList nodes = xpu.getNodeList(doc, "/harvest/xpathMaps/xpathMap");
        if(nodes == null || nodes.getLength() == 0) return maps;
        
        List<XPathMap> list = new ArrayList<>();
        for(int i = 0; i < nodes.getLength(); i++)
        {
            validateXPathMap(nodes.item(i));
            
            XPathMap xpm = new XPathMap();
            xpm.filePath = XmlDomUtils.getAttribute(nodes.item(i), "filePath");
            xpm.rootElement = XmlDomUtils.getAttribute(nodes.item(i), "rootElement");
            list.add(xpm);
        }

        maps.items = list;
        return maps;
    }
    
    
    private static void validateXPathMap(Node node) throws Exception
    {
        String filePath = XmlDomUtils.getAttribute(node, "filePath");
        if(filePath == null) throw new Exception("<xpathMap> element is missing 'filePath' attribute");        
        
        NamedNodeMap atts = XmlDomUtils.getAttributes(node);
        for(int i = 0; i < atts.getLength(); i++)
        {
            String attName = atts.item(i).getNodeName();
            if(!"filePath".equals(attName) && !"rootElement".equals(attName))
            {
                throw new Exception("<xpathMap> element has invalid attribute " + attName);
            }
        }
    }

}
