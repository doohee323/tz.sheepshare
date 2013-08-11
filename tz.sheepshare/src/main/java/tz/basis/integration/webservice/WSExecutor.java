package tz.basis.integration.webservice;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import tz.extend.iam.UserInfo;
import tz.extend.util.LogUtil;
import tz.extend.util.StringUtil;
import tz.extend.util.XmlUtil;

import org.apache.xpath.XPathAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;

/**
 *
 * @author TZ
 *
 */
public class WSExecutor {

    private static Logger logger = LoggerFactory.getLogger(WSExecutor.class);

    private String prefix = "net";

    private String namespaceURI = "";

    /**
     *
     * @param <T>
     * @param wsdl
     * @param soapAction
     * @param parameter
     * @param inputClass
     * @param outputClass
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T executeForObjectToResult(String wsdl, final String soapAction, Object parameter, Class<?> inputClass,
            Class<?> outputClass){
        WebServiceTemplate template = WSTemplateFactory.getWSTemplate(wsdl);
        Object result = null;
        try{
            JAXBContext jc = JAXBContext.newInstance(inputClass);
            Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

            StringWriter wr = new StringWriter();
            m.marshal(parameter, wr);
            StringWriter finalResponseWriter = new StringWriter();
            logger.debug(LogUtil.logFormat(this, wr.toString()));

            StreamSource webServiceInput = new StreamSource(new StringReader(wr.toString()));
            StreamResult webServiceOutput = new StreamResult(finalResponseWriter);

//            template.sendSourceAndReceiveToResult(wsdl, webServiceInput, new WebServiceMessageCallback() {
//
//                public void doWithMessage(WebServiceMessage message){
//                    ((SoapMessage)message).setSoapAction(soapAction);
//                }
//            }, webServiceOutput);
            JAXBContext jaxc = JAXBContext.newInstance(outputClass);
            logger.debug(LogUtil.logFormat(this, finalResponseWriter.toString()));

            result = (T)jaxc.createUnmarshaller().unmarshal(new StringReader(finalResponseWriter.toString()));
        }catch(JAXBException e){
            e.printStackTrace();
        }
        return (T)result;
    }

    /**
     *
     * @param <T>
     * @param wsdl
     * @param soapAction
     * @param parameter
     * @param xmlns
     * @return
     */
    public String executeForMapToString(String wsdl, final String soapAction, String xmlns,
            Map<String, Object> parameter){
        WebServiceTemplate template = WSTemplateFactory.getWSTemplate(wsdl);
        String result = null;
        try{
            String ElNm = soapAction.substring(soapAction.lastIndexOf("/") + 1, soapAction.length());
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element root = (Element)doc.createElementNS(xmlns, ElNm);
            doc.appendChild(root);

            Set dataKeySet = parameter.keySet();
            Iterator dataIterator = dataKeySet.iterator();
            while(dataIterator.hasNext()){
                String name = dataIterator.next().toString();
                String value = parameter.get(name).toString();
                Element el = doc.createElement(name);
                el.setTextContent(value);
                root.appendChild(el);
            }

            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result2 = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result2);
            String xmlString = writer.toString();
            logger.debug(LogUtil.logFormat(this, xmlString));

            StringWriter finalResponseWriter = new StringWriter();
            StreamSource webServiceInput = new StreamSource(new StringReader(xmlString));
            StreamResult webServiceOutput = new StreamResult(finalResponseWriter);

//            template.sendSourceAndReceiveToResult(wsdl, webServiceInput, new WebServiceMessageCallback() {
//
//                public void doWithMessage(WebServiceMessage message){
//                    ((SoapMessage)message).setSoapAction(soapAction);
//                }
//            }, webServiceOutput);
            result = finalResponseWriter.toString();
            logger.debug(LogUtil.logFormat(this, result));
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     *
     * @param <T>
     * @param wsdl
     * @param soapAction
     * @param parameter
     * @param xmlns
     * @return
     */
    public Map<String, Object> executeForMapToMap(String wsdl, final String soapAction, String xmlns,
            Map<String, Object> parameter){
        Map<String, Object> result = new HashMap<String, Object>();
        try{
            String aXmlStr = executeForMapToString(wsdl, soapAction, xmlns, parameter);
            Document doc = XmlUtil.parsing(aXmlStr, "UTF-8", false);
            doc.getDocumentElement().normalize();
            NodeIterator nl = XPathAPI.selectNodeIterator(doc, "/");
            Node childNode;
            while((childNode = nl.nextNode()) != null){
                NodeList children = childNode.getChildNodes();
                for(int i = 0; i < children.getLength(); i++){
                    if(children.item(i).hasChildNodes()){
                        NodeList children2 = children.item(i).getChildNodes();
                        for(int j = 0; j < children2.getLength(); j++){
                            result.put(children2.item(i).getNodeName(), children2.item(j).getTextContent());
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     *
     * @param prefix
     */
    public void setPrefix(String prefix){
        this.prefix = prefix;
    }

    /**
     *
     * @param namespaceURI
     */
    public void setNamespaceURI(String namespaceURI){
        this.namespaceURI = namespaceURI;
    }
}
