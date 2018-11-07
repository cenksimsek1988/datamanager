package com.softactive.core.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.softactive.core.object.MyError;
import com.softactive.core.object.Pullable;

import okhttp3.Response;

public abstract class AbstractXmlHandler<P extends Pullable>
extends AbstractHandler<P, Document, NodeList, Element> {
	private static final long serialVersionUID = 1702240267667438881L;
	protected Map<Integer, Integer> map;
	protected int dateIndex;
	protected int regionCodeIndex;

	@Override
	public boolean handle(Response r, Map<String, Object> sharedParams) {
		Document doc = onFormatResponse(r);
		boolean hasNext = false;
		try {
			metaMap = getMetaMap(doc);
			hasNext = hasNext(metaMap);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		List<P> list = getList(getArray(doc), sharedParams);
		if(list == null || list.size()>0) {
			onListSuccesfullyParsed(list, sharedParams);
		} else {
			onError(sharedParams);
		}
		return hasNext;
	}

	private void handleFile(FileInputStream xmlFile, Map<String, Object> sharedParams) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlFile);
		doc.getDocumentElement().normalize();
		List<P> list = getList(getArray(doc), sharedParams);
		if(list == null || list.isEmpty()) {
			onError(sharedParams);
		} else {
			onListSuccesfullyParsed(list, sharedParams);
		}
	}

	public void handle(String fileName, Map<String, Object> sharedParams) throws IOException {
		try {
			FileInputStream xmlFile = getFile(fileName);
			try {
				handleFile(xmlFile, sharedParams);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	private FileInputStream getFile(Response r) throws FileNotFoundException {
//		System.out.println("Response: " + r);
//		String fileName = getFileName(r);
//		return new FileInputStream(new File(fileName));
//
//	}

	private FileInputStream getFile(String fileName) throws FileNotFoundException {
		return new FileInputStream(new File(fileName));

	}

	protected Double getDoubleValue(Cell c) {
		CellType type = c.getCellTypeEnum();
		switch (type) {
		case STRING:
			String s = c.getStringCellValue();
			if (s != null && s.length() > 0) {
				try {
					return Double.valueOf(s);
				} catch (NumberFormatException e) {
					return null;
				}
			} else {
				return null;
			}
		case NUMERIC:
			return c.getNumericCellValue();
		case BLANK:
			return null;
		default:
			System.out.println("Cell type is strange: " + type);
			return null;
		}
	}

	protected abstract Date resolveDate(String dateString);

	@Override
	protected Document onFormatResponse(Response r) {
		try {
			FileInputStream xmlFile = getFile(getFileName(r));
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.normalize();
			return doc;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected Document onFormatResponse(String fileName) {
		try {
			FileInputStream xmlFile = getFile(fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			return dBuilder.parse(xmlFile);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		}
	}

	protected abstract String getFileName(Response r);

	protected abstract int getArrayStartIndex();

	protected abstract List<P> onEachRow(Row row);

	@Override
	protected NodeList getArray(Document r) {
		return r.getElementsByTagName(getArrayTag());
	}

	protected abstract String getArrayTag();

	//	System.out.println("Staff id : " + eElement.getAttribute("id"));
	//	System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
	//	System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
	//	System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
	//	System.out.println("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());

	@Override
	protected List<P> getList(NodeList array, Map<String, Object> sharedParams) {
		List<P> list = new ArrayList<>();
		P p = null;
		for (int i = 0; i < array.getLength(); i++) {
			Node node = array.item(i);					
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;
				try {
					p = getObject(eElement, sharedParams);
				} catch (MyException e) {
					MyError er = new MyError(ERROR_INVALID_VALUE, e.getMsg() + "/n" + node.toString());
					sharedParams.put(PARAM_ERROR, er);
				}
			} else {
				MyError er = new MyError(ERROR_INVALID_XML_NODE_TAG, "found type of node is not element but: " + node.getNodeType());
				sharedParams.put(PARAM_ERROR, er);
				return list;
			}
			if(p!=null) {
				list.add(p);
			}
		}
		return list;
	}
}
